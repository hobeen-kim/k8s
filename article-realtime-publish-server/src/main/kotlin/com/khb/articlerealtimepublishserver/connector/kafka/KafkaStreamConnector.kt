package com.khb.articlerealtimepublishserver.connector.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articlerealtimepublishserver.connector.StreamConnector
import com.khb.articlerealtimepublishserver.entity.Article
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import kotlin.math.log

@Service
@Primary
class KafkaStreamConnector(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${spring.kafka.producer.topic-name}")
    private val topic: String,
    @Qualifier("kafkaObjectMapper")
    private val objectMapper: ObjectMapper,
): StreamConnector {

    private val logger = LoggerFactory.getLogger(KafkaStreamConnector::class.java)

    override fun streamToRealTimeSubscribers(articles: List<Article>) {

        logger.info("Sending message... : ${articles.size}")

        val articleAsString = objectMapper.writeValueAsString(articles)

        kafkaTemplate.send(topic, articleAsString)

        logger.info("message sent : ${articles.size}")
    }
}