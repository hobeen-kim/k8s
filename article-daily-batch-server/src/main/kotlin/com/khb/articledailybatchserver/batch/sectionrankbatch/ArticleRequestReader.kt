package com.khb.articledailybatchserver.batch.sectionrankbatch

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articledailybatchserver.SECTIONS
import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.YnaArticleRankList
import org.springframework.batch.item.json.JsonObjectReader
import org.springframework.core.io.Resource
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ArticleRequestReader(
    private val objectMapper: ObjectMapper,
): HttpRequestReader<YnaArticleRankList> {

    private val data = mutableListOf<YnaArticleRankList>()

    override fun request(httpRequest: HttpRequest) {

        val httpClient = HttpClient.newHttpClient()

        val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())

        val sectionResponse = objectMapper.readValue(response.body(), YnaArticleRankList::class.java)

        data.add(sectionResponse)
    }

    override fun read(): YnaArticleRankList? {
        return if (data.isEmpty()) {
            null
        } else {
            data.removeAt(0)
        }
    }
}