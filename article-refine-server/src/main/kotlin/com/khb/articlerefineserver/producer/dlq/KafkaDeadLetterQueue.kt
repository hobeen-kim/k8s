package com.khb.articlerefineserver.producer.dlq

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaDeadLetterQueue(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${custom.kafka.dlq.topic}")
    private val topic: String = "raw-article-dlq"
): DeadLetterQueue {

    override fun send(rawArticle: String) {
        kafkaTemplate.send(topic, rawArticle)
    }
}