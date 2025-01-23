package com.khb.articlerealtimepublishserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class WebSocketConfig {

    @Bean
    fun stompClient(): WebSocketStompClient {
        val stompClient = WebSocketStompClient(StandardWebSocketClient())
        stompClient.messageConverter = MappingJackson2MessageConverter()

        return stompClient
    }
}