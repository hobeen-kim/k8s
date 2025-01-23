package com.khb.articlerealtimepublishserver.service

import com.khb.articlerealtimepublishserver.entity.Article
import com.khb.articlerealtimepublishserver.service.dto.StreamResponse
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.net.http.HttpClient

@Service
class StompStreamArticleService: StreamArticleService {

    val client = HttpClient.newBuilder().build()

    override fun streamToRealTimeSubscribers(articles: List<Article>) {

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://api.example.com/data"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}