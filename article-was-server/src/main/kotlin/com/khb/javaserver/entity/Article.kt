package com.khb.javaserver.entity

import java.time.LocalDateTime

data class Article (
    val articleId: String,
    val title: String,
    val url: String,
    val time: LocalDateTime,
    val section: String,
    val content: String,
    var tags: List<String>,
    var summary: String,
) {
    fun groupBy(): String {
        return "all"
    }
}