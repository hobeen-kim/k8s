package com.khb.articlerealtimepublishserver.service

import com.khb.articlerealtimepublishserver.entity.Article
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.stereotype.Service
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@Service
class StompStreamArticleService(
    private val stompSessionProvider: StompSessionProvider,
): StreamArticleService {

    private lateinit var stompSession: StompSession

    private val logger = LoggerFactory.getLogger(StompStreamArticleService::class.java)

    override fun streamToRealTimeSubscribers(articles: List<Article>) {
        try {

            logger.info("Message prepared to send to subscribers, size: ${articles.size}")

            checkConnection()

            stompSession.send("/publish/chat.1", articles)

            logger.info("Message sent to subscribers, size: ${articles.size}")
        } catch (e: Exception) {
            logger.error("Failed to send message: ${e.message}")
        }
    }

    private fun checkConnection() {

        if (!::stompSession.isInitialized || !stompSession.isConnected) {
            stompSession = stompSessionProvider.getSession()
        }
    }
}