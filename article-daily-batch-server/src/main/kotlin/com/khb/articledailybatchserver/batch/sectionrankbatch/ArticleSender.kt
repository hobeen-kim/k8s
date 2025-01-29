package com.khb.articledailybatchserver.batch.sectionrankbatch

import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.ArticleReport
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import java.time.LocalDate

open class ArticleSender(
    private val date: LocalDate,
): ItemWriter<ArticleReport> {
    override fun write(chunk: Chunk<out ArticleReport>) {

        val articleReports = chunk.items

        val sb = StringBuilder()

        sb.append("Article Daily Report\n")
        sb.append("Date: $date\n\n")

        for (articleReport in articleReports) {
            sb.append(articleReport.toEmailReportFormat())
        }

        println("$sb")
    }
}