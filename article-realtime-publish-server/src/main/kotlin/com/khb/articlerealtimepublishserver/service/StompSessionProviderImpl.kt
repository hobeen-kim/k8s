package com.khb.articlerealtimepublishserver.service

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@Component
class StompSessionProviderImpl(
    private val stompClient: WebSocketStompClient,
    private val environment: Environment,

    @Value("\${custom.stomp.url}")
    private val stompUrl: String,
    @Value("\${custom.stomp.port}")
    private val stompPort: Int
): StompSessionProvider {

    private lateinit var stompSession: StompSession

    private val logger = LoggerFactory.getLogger(StompSessionProviderImpl::class.java)

    private val initialReconnectCount = 3
    private val reconnectRemains = AtomicInteger(initialReconnectCount)

    override fun getSession(): StompSession {

        if (!stompSession.isConnected) {
            initializeConnection()
        }

        return stompSession
    }

    @PostConstruct
    fun initialize() {

        if (invalidProfile()) {
            return
        }

        val newHttpClient = HttpClient.newHttpClient()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://$stompUrl:$stompPort/info"))
            .build()

        //response
        val response = newHttpClient.send(request, HttpResponse.BodyHandlers.ofString())

        logger.info("http response = ${response.body()}")

        if (environment.activeProfiles.contains("prod") || environment.activeProfiles.contains("local")) {
            initializeConnection()
        }
    }

    private fun invalidProfile() = !environment.activeProfiles.contains("prod") && !environment.activeProfiles.contains("local")

    private fun initializeConnection() {
        stompSession = stompClient.connectAsync(
            "ws://$stompUrl:$stompPort/ws-connect",
            object : StompSessionHandlerAdapter() {
                override fun handleTransportError(session: StompSession, exception: Throwable) {
                    // 연결 에러 처리
                    logger.error("Transport error: ${exception.message}")
                    reconnect()
                }

                override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                    reconnectRemains.set(initialReconnectCount)
                }
            }
        ).get(5, TimeUnit.SECONDS)
    }

    private fun reconnect() {
        try {
            if(reconnectRemains.getAndDecrement() >= 0) {
                initializeConnection()
            } else {
                reconnectRemains.set(initialReconnectCount)
                logger.error("Reconnection failed: Maximum reconnection count reached")
            }
        } catch (e: Exception) {
            logger.error("Reconnection failed: ${e.message}")
        }
    }

    @PreDestroy
    fun cleanup() {
        if (stompSession.isConnected) {
            stompSession.disconnect()
        }
    }
}