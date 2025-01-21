package com.khb.javaserver.service.dto

import com.khb.javaserver.entity.Article
import java.time.LocalDateTime

data class ArticleResponse (
    val articleId: String,
    val title: String,
    val url: String,
    val time: LocalDateTime,
    val section: String,
    val tags: List<String>,
    val summary: String,
) {
    companion object {
        fun of(article: Article): ArticleResponse {
            return ArticleResponse(
                articleId = article.articleId,
                title = article.title,
                url = article.url,
                time = article.time,
                section = article.section,
                tags = article.tags,
                summary = article.summary
            )
        }
    }
}