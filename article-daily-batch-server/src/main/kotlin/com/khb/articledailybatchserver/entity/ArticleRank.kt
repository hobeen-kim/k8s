package com.khb.articledailybatchserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "\${custom.data.mongodb.article-rank-collection}")
data class ArticleRank (
    @Id
    val sectionDateId: String,
    val section: String,
    val date: LocalDate,
    val articleIds: List<String>,
)