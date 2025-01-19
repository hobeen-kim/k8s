package com.khb.k8sjavaconsumer.consumer

import com.khb.k8sjavaconsumer.dto.Article
import com.khb.k8sjavaconsumer.repository.ArticleRepository
import com.mongodb.DuplicateKeyException
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ArticleConsumer(
    private val articleRepository: ArticleRepository,
) {

    final val logger = LoggerFactory.getLogger(ArticleConsumer::class.java)

    @KafkaListener(
        topics = ["raw-article"],
        concurrency = "3"
    )
    fun listener(data: ConsumerRecord<String, String>) {

        try {
            val article = Article.fromString(data.value()) ?: return //TODO: DLQ 처리

            if(articleRepository.existsById(article.articleId)) {
                return
            }

            articleRepository.save(article)
            logger.info("Save article: $article")
        } catch (e: DuplicateKeyException) {
            logger.error("Duplicated article: ${data.value()}")
        } catch (e: Exception) {
            logger.error("Error: ${e.message}")
            //TODO: DLQ 처리
        }
    }
}