package com.khb.articledailybatchserver.batch.sectionrankbatch

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.YnaArticleRankList
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

open class ArticleRankItemReader(
    private val objectMapper: ObjectMapper,
    private val section: String?
): ItemReader<YnaArticleRankList> {
    private var dataRead = false

    override fun read(): YnaArticleRankList? {
        if (dataRead) return null

        println("read article rank data, section: $section")

        val httpClient = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://rts.cdp.yna.co.kr/api/v2/rts-stats?size=20&${getSectionQuery()}"))
            .GET()
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        dataRead = true

        return objectMapper.readValue(response.body(), YnaArticleRankList::class.java)
    }

    private fun getSectionQuery(): String {
        return if (section.isNullOrBlank() || section == "전체") {
            ""
        } else {
            "section=$section"
        }
    }
}