package com.khb.articlewasserver.controller

import com.khb.articlewasserver.controller.dto.StreamResponse
import com.khb.articlewasserver.entity.Article
import com.khb.articlewasserver.publisher.ArticlePublisher
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(
    private val articlePublisher: ArticlePublisher,
) {

    private val log = LoggerFactory.getLogger(MessageController::class.java)

    @MessageMapping("/chat.{chatRoomId}")
    fun sendMessage(
        articles: List<Article>,
        @DestinationVariable chatRoomId: Long
    ): StreamResponse {

        articlePublisher.publish(articles)

        return StreamResponse.of(articles)
    }

    @MessageExceptionHandler
    fun handleException(e: Exception) {
        log.error("Exception occurred", e)
    }
}