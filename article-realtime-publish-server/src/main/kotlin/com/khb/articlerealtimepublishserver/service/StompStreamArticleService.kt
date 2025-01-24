package com.khb.articlerealtimepublishserver.service

import com.khb.articlerealtimepublishserver.entity.Article
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.stereotype.Service
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@Service
class StompStreamArticleService(
    private val stompClient: WebSocketStompClient,
): StreamArticleService {

    private lateinit var stompSession: StompSession

    private val logger = LoggerFactory.getLogger(StompStreamArticleService::class.java)

    private val reconnectCount = AtomicLong(3)

    init {
        // 초기 연결 설정
        initializeConnection()
    }

    override fun streamToRealTimeSubscribers(articles: List<Article>) {
        try {
            if (!stompSession.isConnected) {
                reconnect()
            }
            stompSession.send("/publish/chat.1", articles)
        } catch (e: Exception) {
            logger.error("Failed to send message: ${e.message}")
            reconnect()
        }
    }

    private fun initializeConnection() {
        stompSession = stompClient.connectAsync(
            "ws://localhost:8080/ws-connect",
            object : StompSessionHandlerAdapter() {
                override fun handleTransportError(session: StompSession, exception: Throwable) {
                    // 연결 에러 처리
                    logger.error("Transport error: ${exception.message}")
                    reconnect()
                }

                override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                    reconnectCount.set(3)
                }
            }
        ).get(5, TimeUnit.SECONDS)
    }

    private fun reconnect() {
        try {
            if(reconnectCount.getAndDecrement() >= 0) {
                initializeConnection()
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