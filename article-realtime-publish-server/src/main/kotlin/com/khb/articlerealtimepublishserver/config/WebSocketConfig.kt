package com.khb.articlerealtimepublishserver.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient

@Configuration
class WebSocketConfig {

    @Bean
    fun stompClient(): WebSocketStompClient {
        val stompClient = WebSocketStompClient(StandardWebSocketClient())

        val messageConverter = MappingJackson2MessageConverter().apply {
            objectMapper.registerModule(JavaTimeModule())
        }
        stompClient.messageConverter = messageConverter

        return stompClient
    }
}