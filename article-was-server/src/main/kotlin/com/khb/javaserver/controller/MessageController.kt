package com.khb.javaserver.controller

import com.khb.javaserver.controller.dto.StreamResponse
import com.khb.javaserver.entity.Article
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController {

    private val log = LoggerFactory.getLogger(MessageController::class.java)

    @MessageMapping("/chat.{chatRoomId}")
    @SendTo("/subscribe/chat.{chatRoomId}")
    fun sendMessage(
        articles: List<Article>,
        @DestinationVariable chatRoomId: Long
    ): StreamResponse {

        return StreamResponse.of(articles)
    }

    @MessageExceptionHandler
    fun handleException(e: Exception) {
        log.error("Exception occurred", e)
    }
}