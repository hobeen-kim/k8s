package com.khb.k8sjavaconsumer.producer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

@Configuration
class KafkaProducerConfig {

    @Value("\${spring.kafka.producer.bootstrap-servers}")
    private val bootstrapServers: String = "localhost:9092"

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val config = mutableMapOf<String, Any>()

        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(config)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String>{
        return KafkaTemplate(producerFactory());
    }

    @Bean("producerObjectMapper")
    fun producerObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        return objectMapper
    }

}