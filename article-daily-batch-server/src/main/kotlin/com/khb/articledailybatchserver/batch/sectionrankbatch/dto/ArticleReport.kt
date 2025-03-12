package com.khb.articledailybatchserver.batch.sectionrankbatch.dto

import com.khb.articledailybatchserver.entity.Article
import java.time.LocalDate

data class ArticleReport (
    val section: String,
    val articles: List<Article> = listOf(),
) {
    fun toEmailReportFormat(): String {

        val sb = StringBuilder()

        for (article in articles) {
            sb.append("""
                <h2>${article.title}</h2>
                <p>${article.summary}</p>
            """.trimIndent())
        }

        return """
            <h1>$section</h1>
            $sb
        """.trimIndent()
    }
}