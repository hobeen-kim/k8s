package com.khb.articlewasserver.controller.dto

import com.khb.articlewasserver.entity.Article

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