package com.khb.articlewasserver.interceptor

import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class WebSocketInterceptor: ChannelInterceptor {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {

        val accessor = StompHeaderAccessor.wrap(message)
        val command = accessor.command

        if (command == StompCommand.SUBSCRIBE) {
            log.info("SUBSCRIBED: ${accessor.destination}")
        }

        return message
    }
}