package com.khb.articlewasserver.sender

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articlewasserver.controller.dto.StreamResponse
import com.khb.articlewasserver.entity.Article
import jakarta.annotation.PreDestroy
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class KafkaArticleSender(
    private val messagingTemplate: SimpMessagingTemplate,
    @Qualifier("kafkaObjectMapper")
    private val objectMapper: ObjectMapper,
    private val kafkaProperties: KafkaProperties,
    @Value("\${spring.kafka.consumer.group-id}")
    private val groupId: String
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = ["\${custom.kafka.realtime-article.topic}"],
    )
    fun send(data: ConsumerRecord<String, String>) {

        val articles = objectMapper.readValue(data.value(), Array<Article>::class.java).toList()

        messagingTemplate.convertAndSend("/subscribe/chat.1", StreamResponse.of(articles))
    }

    //predestroy 시 kafka 에서 consumer group 삭제
    @PreDestroy
    fun cleanup() {
        try {
            val adminClient = AdminClient.create(kafkaProperties.buildConsumerProperties())

            adminClient.use { client ->
                client.deleteConsumerGroups(listOf(groupId))
                    .all()
                    .get(10, TimeUnit.SECONDS)

                logger.info("Successfully deleted consumer group: $groupId")
            }
        } catch (e: Exception) {
            logger.error("Failed to delete consumer group: $groupId", e)
        }
    }

}