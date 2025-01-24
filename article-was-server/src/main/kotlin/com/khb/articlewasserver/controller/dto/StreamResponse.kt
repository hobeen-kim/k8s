package com.khb.articlewasserver.controller.dto

import com.khb.articlewasserver.entity.Article

data class StreamResponse (
    val sectionResponse: MutableList<SectionResponse>
) {
    companion object {
        fun of(articles: List<Article>): StreamResponse {
            val response = StreamResponse(
                sectionResponse = mutableListOf()
            )

            articles
                .groupBy { it.section }
                .forEach { (key, value) ->
                    response.sectionResponse.add(
                        SectionResponse.of(key, value)
                    )
                }

            return response
        }
    }
}