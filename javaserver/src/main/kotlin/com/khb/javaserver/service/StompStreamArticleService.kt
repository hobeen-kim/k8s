package com.khb.javaserver.service

import com.khb.javaserver.entity.Article
import com.khb.javaserver.service.dto.StreamResponse
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class StompStreamArticleService(
    private val simpMessagingTemplate: SimpMessagingTemplate
): StreamArticleService {

    override fun streamToRealTimeSubscribers(articles: List<Article>) {

        val response = StreamResponse.of(articles)

        simpMessagingTemplate.convertAndSend(
            "/subscribe/chat.1",  // 구독할 destination
            response
        )
    }
}