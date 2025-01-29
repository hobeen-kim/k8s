package com.khb.articledailybatchserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.time.LocalDate

@Configuration
class MongoConfig {
    @Bean
    fun customConversions(): MongoCustomConversions {
        val converters = listOf(
            object : Converter<LocalDate, String> {
                override fun convert(source: LocalDate): String = source.toString()
            },
            object : Converter<String, LocalDate> {
                override fun convert(source: String): LocalDate = LocalDate.parse(source)
            }
        )
        return MongoCustomConversions(converters)
    }}