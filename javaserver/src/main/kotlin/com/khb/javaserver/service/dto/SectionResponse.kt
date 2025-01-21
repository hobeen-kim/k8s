package com.khb.javaserver.service.dto

import com.khb.javaserver.entity.Article
import java.time.LocalDateTime

data class SectionResponse (
    val section: String,
    val articles: List<ArticleResponse>
) {
    companion object {
        fun of(section: String, article: List<Article>): SectionResponse {
            return SectionResponse(
                section = section,
                articles = article.map { ArticleResponse.of(it) }
            )
        }
    }
}