package com.khb.k8sjavaconsumer.consumer

import com.khb.k8sjavaconsumer.cache.CacheService
import com.khb.k8sjavaconsumer.dto.Article
import com.khb.k8sjavaconsumer.repository.ArticleRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class NewConsumer(
    private val articleRepository: ArticleRepository,
    private val cacheService: CacheService

) {

    @KafkaListener(
        topics = ["raw-article"],
        groupId = "group_1",
        concurrency = "3"
    )
    fun listener(data: ConsumerRecord<String, String>) {

        try {
            val article = Article.fromString(data.value()) ?: return //TODO: DLQ 처리

            if(cacheService.hasKey(article.articleId)) {
                return
            }

            cacheService.set(article.articleId, article.articleId)
            articleRepository.save(article)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO: DLQ 처리
        }
    }
}