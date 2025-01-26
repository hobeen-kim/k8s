package com.khb.articledailybatchserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "\${custom.data.mongodb.article-collection}")
data class Article (
    @Id
    val articleId: String,
    val title: String,
    val url: String,
    val time: LocalDateTime,
    val section: String,
    val content: String,
    var tags: List<String>,
    var summary: String? = null,
)