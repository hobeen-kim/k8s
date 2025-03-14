package com.khb.articledailybatchserver.service.mail

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
@Primary
class SlackSender(
    @Value("\${sender.slack.url}")
    private val slackWebhookUrl: String,
): MailSender {
    override fun send(title: String, text: String, to: List<String>) {

        println("SlackSender.send : $title\n$text")

        val httpClient = HttpClient.newHttpClient()

        // Kotlin 표준 라이브러리의 JSON 사용
        val jsonObject = buildJsonObject {
            put("text", JsonPrimitive("$title\n$text"))
        }

        println("slackWebhookUrl : $slackWebhookUrl")

        val request = HttpRequest.newBuilder()
            .uri(URI.create(slackWebhookUrl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if(response.statusCode() != 200) {
            println("response is not 200, $response")
        }
    }

    override fun send(title: String, text: String, to: String) {
        send(title, text, listOf());
    }
}