package com.khb.javaserver.stream

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.khb.javaserver.entity.Article
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.WindowStore
import org.slf4j.LoggerFactory
import java.time.Duration

val logger = LoggerFactory.getLogger("StreamHandler")

val objectMapper = ObjectMapper()
    .registerModule(KotlinModule.Builder().build())
    .registerModule(JavaTimeModule())
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

fun KStream<String, String>.setHandler() {

    this.map { _, value ->
        val article = objectMapper.readValue<Article>(value)
        KeyValue.pair(article.groupBy(), article)
    }.groupByKey(
        Grouped.with(
            Serdes.String(), // Key에 대한 Serde
            JsonSerde(Article::class.java) // Value에 대한 Serde
        )
    ).windowedBy(
        TimeWindows.ofSizeAndGrace(Duration.ofMinutes(1), Duration.ofSeconds(30))
    ).aggregate(
        { mutableListOf<Article>() }, // 초기값으로 빈 리스트 생성
        { key, newer, accumulator ->
            println("Window start")
            accumulator.apply {
                add(newer)  // 새로운 Article을 리스트에 추가
            }
        },
        Materialized.with(
            Serdes.String(), // Key Serde
            ArticleListSerde() // Value Serde (커스텀 Serde)
        )
    ).suppress(
        Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded())
    ).toStream()
    .foreach { key, value ->
        println("Window start: ${key.window().startTime()}")
        println("Window end: ${key.window().endTime()}")
        println("ArticleId: ${key.key()}")
        println("Grouped messages: $value")
        println("-------------------")
    }
}

private fun buildWindowPersistentStore(): Materialized<String, MutableList<Article>, WindowStore<Bytes, ByteArray>> {
    return Materialized.`as`<String, MutableList<Article>, WindowStore<Bytes, ByteArray>>(
        "store-name" // 저장소의 고유 이름
    ).withKeySerde(Serdes.String()) // 키의 Serializer/Deserializer
        .withValueSerde(
            // Article 리스트를 위한 커스텀 Serde
            CustomSerde()
        )
}

class CustomSerde : Serde<MutableList<Article>> {
    override fun serializer(): Serializer<MutableList<Article>> {
        return Serializer { topic, data ->
            objectMapper.writeValueAsBytes(data)
        }
    }

    override fun deserializer(): Deserializer<MutableList<Article>> {
        return Deserializer { topic, data ->
            objectMapper.readValue(data, object : TypeReference<MutableList<Article>>() {})
        }
    }
}

// Article 리스트를 위한 커스텀 Serde
class ArticleListSerde : Serde<MutableList<Article>> {
    override fun serializer(): Serializer<MutableList<Article>> {
        return Serializer { _, data ->
            objectMapper.writeValueAsBytes(data)
        }
    }

    override fun deserializer(): Deserializer<MutableList<Article>> {
        return Deserializer { _, data ->
            objectMapper.readValue(data, object : TypeReference<MutableList<Article>>() {})
        }
    }
}

// 단일 Article을 위한 JsonSerde
class JsonSerde<T>(private val type: Class<T>) : Serde<T> {
    override fun serializer(): Serializer<T> {
        return Serializer { _, data ->
            objectMapper.writeValueAsBytes(data)
        }
    }

    override fun deserializer(): Deserializer<T> {
        return Deserializer { _, data ->
            objectMapper.readValue(data, type)
        }
    }
}