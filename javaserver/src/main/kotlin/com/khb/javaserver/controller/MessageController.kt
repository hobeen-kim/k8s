package com.khb.javaserver.controller

import com.khb.javaserver.controller.dto.ChatMessageRequest
import com.khb.javaserver.controller.dto.ChatMessageResponse
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController {

    private val log = LoggerFactory.getLogger(MessageController::class.java)

    @SendTo("/subscribe/chat.{chatRoomId}")
    fun sendMessage(
        request: ChatMessageRequest,
        @DestinationVariable chatRoomId: Long
    ): ChatMessageResponse {

        if(request.username == "error") {
            throw RuntimeException("username is error")
        }

        return ChatMessageResponse(
            username = request.username,
            content = request.content
        )
    }

    @MessageExceptionHandler
    fun handleException(e: Exception) {
        log.error("Exception occurred", e)
    }
}