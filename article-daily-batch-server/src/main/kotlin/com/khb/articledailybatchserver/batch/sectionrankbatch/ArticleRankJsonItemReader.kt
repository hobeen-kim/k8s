package com.khb.articledailybatchserver.batch.sectionrankbatch

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articledailybatchserver.SECTIONS
import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.YnaArticleRankList
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader
import java.net.URI
import java.net.http.HttpRequest

open class ArticleRankJsonItemReader(
    private val objectMapper: ObjectMapper
): AbstractItemCountingItemStreamItemReader<YnaArticleRankList>() {

    private val articleRequestReader: HttpRequestReader<YnaArticleRankList> = ArticleRequestReader(objectMapper)

    override fun doRead(): YnaArticleRankList? {

        return articleRequestReader.read()
    }

    init {
        super.setExecutionContextName("articleRankJsonItemReader")
    }

    override fun doOpen() {

        println("read article rank data")

        for (section in SECTIONS) {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://rts.cdp.yna.co.kr/api/v2/rts-stats?size=5&${getSectionQuery(section)}&${getTimeQuery()}"))
                .GET()
                .build()
            articleRequestReader.request(request)
        }
    }

    override fun doClose() {
        // do nothing
    }

    private fun getSectionQuery(section: String?): String {
        return if (section.isNullOrBlank() || section == "전체") {
            ""
        } else {
            "section=$section"
        }
    }

    private fun getTimeQuery(): String {
        return "upload-minute-from=4000&upload-minute-to=400"
    }
}