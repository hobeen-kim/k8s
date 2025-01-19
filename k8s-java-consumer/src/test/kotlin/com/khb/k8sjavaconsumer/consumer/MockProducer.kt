package com.khb.k8sjavaconsumer.consumer

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component


@Component
@Profile("test")
class MockProducer(
    @Qualifier("kafkaTestTemplate")
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    fun send(topic: String?, payload: Any) {
        println("Send to $topic: $payload")
        kafkaTemplate.send(topic!!, payload)
    }
}