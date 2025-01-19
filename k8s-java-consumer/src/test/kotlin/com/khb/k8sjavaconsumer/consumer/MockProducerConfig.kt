package com.khb.k8sjavaconsumer.consumer

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
@Profile("test")
class MockProducerConfig {

    @Value("\${spring.kafka.producer.bootstrap-servers}")
    private val kafkaUrl: String = "localhost:19092"

    @Bean
    fun factory(): ProducerFactory<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(factory())
    }
}