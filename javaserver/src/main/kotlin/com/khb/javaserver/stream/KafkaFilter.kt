package com.khb.javaserver.stream

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.khb.javaserver.entity.Article
import com.khb.javaserver.service.StreamArticleService
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.WindowStore
import java.time.Duration

val objectMapper: ObjectMapper = ObjectMapper()
    .registerModule(KotlinModule.Builder().build())
    .registerModule(JavaTimeModule())
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

fun KStream<String, String>.setHandler(
    streamArticleService: StreamArticleService
) {

    this.map { _, value ->
        val article = objectMapper.readValue<Article>(value)
        KeyValue.pair(article.groupBy(), article)
    }.groupByKey(
        Grouped.with(
            Serdes.String(), // Key에 대한 Serde
            JsonSerde(Article::class.java) // Value에 대한 Serde
        )
    ).windowedBy(
        TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1))
    ).aggregate(
        { mutableListOf() }, // 초기값으로 빈 리스트 생성
        { key, newer, accumulator ->
            accumulator.apply {
                add(newer)  // 새로운 Article을 리스트에 추가
            }
        },
        buildWindowPersistentStore()
    ).suppress(
        Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded())
    ).toStream()
        .foreach { key, value ->
            streamArticleService.streamToRealTimeSubscribers(value)
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

private fun buildWindowPersistentStore(): Materialized<String, MutableList<Article>, WindowStore<Bytes, ByteArray>> {

    return Materialized.`as`<String, MutableList<Article>, WindowStore<Bytes, ByteArray>>(WindowStore::class.java.name)
        .withKeySerde(Serdes.String())
        .withValueSerde(ArticleListSerde())
        .withRetention(Duration.ofMinutes(2))
        .withCachingDisabled()
}