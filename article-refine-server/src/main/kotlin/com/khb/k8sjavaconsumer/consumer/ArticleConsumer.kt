package com.khb.k8sjavaconsumer.consumer

import com.khb.k8sjavaconsumer.dto.Article
import com.khb.k8sjavaconsumer.producer.dlq.DeadLetterQueue
import com.khb.k8sjavaconsumer.producer.refinedarticleproducer.RefinedArticleProducer
import com.khb.k8sjavaconsumer.repository.ArticleRepository
import com.khb.k8sjavaconsumer.service.gpt.GptService
import com.khb.k8sjavaconsumer.utils.exception.BusinessException
import com.khb.k8sjavaconsumer.utils.exception.InvalidDataException
import com.mongodb.DuplicateKeyException
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ArticleConsumer(
    private val articleRepository: ArticleRepository,
    private val articleProducer: RefinedArticleProducer,
    private val dlQueue: DeadLetterQueue,
    private val gptService: GptService
) {

    private val logger = LoggerFactory.getLogger(ArticleConsumer::class.java)

    @KafkaListener(
        topics = ["\${custom.kafka.consumer.topic}"],
        concurrency = "3"
    )
    fun listener(data: ConsumerRecord<String, String>) {

        try {
            val article = getArticleFrom(data.value())

            if(article.exists()) {
                return
            }

            article.appendGptSummary()

            articleRepository.save(article)

            articleProducer.send(article)

        } catch (e: BusinessException) {
            logger.error("Business error: ${e.message}")
        } catch (e: DuplicateKeyException) {
            logger.error("Duplicated article: ${data.value()}")
        } catch (e: Exception) {
            logger.error("Error: ${e.message}")
            dlQueue.send(data.value())
        }
    }

    private fun getArticleFrom(data: String): Article {
        val article = Article.fromString(data)

        if(article == null) {
            dlQueue.send(data)
            throw InvalidDataException("Invalid data: $data")
        }

        return article
    }

    private fun Article.exists(): Boolean {
        return articleRepository.existsById(this.articleId)
    }

    private fun Article.appendGptSummary() {
        val gptResponse = gptService.summarizeArticle(this.content)

        this.updateGptSummary(
            gptResponse.summary,
        )

    }
}