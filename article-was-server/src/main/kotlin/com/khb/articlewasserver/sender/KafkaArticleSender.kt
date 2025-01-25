package com.khb.articlewasserver.sender

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articlewasserver.controller.dto.StreamResponse
import com.khb.articlewasserver.entity.Article
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class KafkaArticleSender(
    private val messagingTemplate: SimpMessagingTemplate,
    @Qualifier("kafkaObjectMapper")
    private val objectMapper: ObjectMapper,
) {

    @KafkaListener(
        topics = ["\${custom.kafka.realtime-article.topic}"],
    )
    fun send(data: ConsumerRecord<String, String>) {

        val articles = objectMapper.readValue(data.value(), Array<Article>::class.java).toList()

        messagingTemplate.convertAndSend("/subscribe/chat.1", StreamResponse.of(articles))
    }

}