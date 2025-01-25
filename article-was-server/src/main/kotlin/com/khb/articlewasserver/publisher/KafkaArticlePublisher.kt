package com.khb.articlewasserver.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articlewasserver.entity.Article
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaArticlePublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Qualifier("kafkaObjectMapper")
    private val objectMapper: ObjectMapper,
    @Value("\${custom.kafka.realtime-article.topic}")
    private val topic: String
): ArticlePublisher {
    override fun publish(articles: List<Article>) {
        val articleAsString = objectMapper.writeValueAsString(articles)

        kafkaTemplate.send(topic, articleAsString)
    }
}