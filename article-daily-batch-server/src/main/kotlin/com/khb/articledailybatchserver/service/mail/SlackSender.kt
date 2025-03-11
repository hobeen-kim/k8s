package com.khb.articledailybatchserver.service.mail

import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.YnaArticleRankList
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
@Primary
class SlackSender(
): MailSender {
    override fun send(title: String, text: String, to: List<String>) {
        val httpClient = HttpClient.newHttpClient()

        val jsonBody = """
        {
            "text": "$title\n$text"
        }
    """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://hooks.slack.com/services/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"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build()

        httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }

    override fun send(title: String, text: String, to: String) {
        send(title, text, listOf());
    }
}