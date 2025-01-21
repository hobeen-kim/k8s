package com.khb.javaserver.stream

import org.apache.kafka.streams.KafkaStreams
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class KafkaRunner(
    private val kafkaStreams: KafkaStreams,
): CommandLineRunner {
    override fun run(vararg args: String?) {
        kafkaStreams.start()
    }
}