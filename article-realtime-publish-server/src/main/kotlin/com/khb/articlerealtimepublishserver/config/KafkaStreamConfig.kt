package com.khb.articlerealtimepublishserver.config

import com.khb.articlerealtimepublishserver.service.StreamArticleService
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import java.util.*
import com.khb.articlerealtimepublishserver.stream.setHandler
import org.springframework.beans.factory.annotation.Autowired

@Configuration
@EnableKafka
class KafkaStreamConfig(
    @Value("\${spring.kafka.streams.bootstrap-servers}")
    private val bootstrapServers: String,
    @Value("\${spring.kafka.streams.application-id}")
    private val applicationId: String,
    @Value("\${spring.kafka.streams.topic-name}")
    private val topicName: String,
) {

    @Autowired lateinit var streamArticleService: StreamArticleService

    private val props = mapOf(
        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
        StreamsConfig.APPLICATION_ID_CONFIG to applicationId,
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG to Serdes.String()::class.java,
        StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG to Serdes.String()::class.java
    )

    @Bean
    fun kafkaStreams(): KafkaStreams {
        val prop = Properties().apply {
            putAll(props)
        }

        val streamsBuilder = StreamsBuilder()
        val stream: KStream<String, String> = streamsBuilder.stream(topicName)
        stream.setHandler(
            streamArticleService
        )

        val topology = streamsBuilder.build()

        val kafkaStreams = KafkaStreams(topology, prop)

        kafkaStreams.start()

        return kafkaStreams
    }
}