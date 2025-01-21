package com.khb.javaserver.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Article (
    @JsonProperty("articleId") val articleId: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("url") val url: String,
    @JsonProperty("time") val time: LocalDateTime,
    @JsonProperty("section") val section: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("tags") var tags: List<String>,
    @JsonProperty("summary") var summary: String? = null,
) {

    companion object {
        fun empty(): Article {
            return Article(
                articleId = "",
                title = "",
                url = "",
                time = LocalDateTime.now(),
                section = "",
                content = "",
                tags = emptyList(),
                summary = ""
            )
        }
    }

    fun groupBy(): String {
        return section
    }
}