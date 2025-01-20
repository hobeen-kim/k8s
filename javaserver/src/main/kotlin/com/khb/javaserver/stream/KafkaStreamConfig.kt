package com.khb.javaserver.stream

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.support.serializer.JsonSerde
import java.time.Duration
import java.util.*

@Configuration
@EnableKafka
class KafkaStreamConfig {

    @Value("\${spring.kafka.streams.bootstrap-servers}")
    private val bootstrapServers: String = "localhost:9092"
    @Value("\${spring.kafka.streams.application-id}")
    private val applicationId: String = "web-stream-dev"

//    @Bean
    fun kafkaStreams(): KafkaStreams {
        val prop = Properties()
        prop[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        prop[StreamsConfig.APPLICATION_ID_CONFIG] = applicationId
        prop[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
        prop[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass

        val streamsBuilder = StreamsBuilder()

        val objectMapper = ObjectMapper()

        // String Serde 명시적 선언
        val stringSerde = Serdes.String()
        // Map을 위한 Serde 생성
        val mapSerde = JsonSerde(Map::class.java, objectMapper)
        // List<Map>을 위한 Serde 생성
        val listSerde = JsonSerde(object : TypeReference<List<Map<String, Any>>>() {}, objectMapper)


        val streamLog = streamsBuilder.stream<String, String>("refined-article-dev")

        streamLog
            .mapValues { value ->
                try {
                    objectMapper.readValue(value, Map::class.java)
                } catch (e: Exception) {
                    println("Error parsing JSON: $value")
                    println("Error: ${e.message}")
                    mapOf<String, Any>()
                }
            }
            .groupBy(
                { _, value -> "test" },
                Grouped.with(stringSerde, mapSerde)
            )
            .windowedBy(TimeWindows.of(Duration.ofMinutes(1)))
            .aggregate(
                { mutableListOf() },
                { _, value, aggregate ->
                    aggregate as MutableList<Map<String, Any>>

                    val articleId = value["articleId"] as? String ?: ""

                    val map: Map<String, Any> = mapOf(
                        "articleId" to articleId,
                    )

                    aggregate.add(map)
                    aggregate
                },
                Materialized.with(stringSerde, listSerde)
            )
            .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded())) // <- 요거
            .toStream()
            .foreach { key, value ->
                println("Window start: ${key.window().startTime()}")
                println("Window end: ${key.window().endTime()}")
                println("ArticleId: ${key.key()}")
                println("Grouped messages: $value")
                println("-------------------")
            }

        // 3. 스트림즈 생성
        val kafkaStreams = KafkaStreams(streamsBuilder.build(), prop)
        kafkaStreams.start()

        return kafkaStreams
    }
}