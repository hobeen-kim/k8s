package com.khb.k8sjavaconsumer.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.k8sjavaconsumer.dto.Article
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class RefinedArticleProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Qualifier("producerObjectMapper")
    private val objectMapper: ObjectMapper
) {

    @Value("\${custom.kafka.producer.topic}")
    private val topic: String = "refined-article"

    fun send(article: Article) {

        val articleAsString = objectMapper.writeValueAsString(article)

        kafkaTemplate.send(topic, articleAsString)
    }
}