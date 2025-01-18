package com.khb.k8sjavaconsumer.dto

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "article")
data class Article (
    val articleId: String,
    val title: String,
    val url: String,
    val time: LocalDateTime,
    val section: String,
    val content: String,
)