package com.khb.articlerealtimepublishserver.entity

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
    @JsonProperty("summary") var summary: String,
) {
    fun groupBy(): String {
        return "all"
    }
}