package com.khb.k8sjavaconsumer.producer

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class DeadLetterQueue(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {

    @Value("\${custom.kafka.dlq.topic}")
    private val topic: String = "raw-article-dlq"

    fun send(rawArticle: String) {
        kafkaTemplate.send(topic, rawArticle)
    }
}