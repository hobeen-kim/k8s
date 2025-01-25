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
            checkConnection()

            logger.info("Sending message... : ${articles.size}")

            val receipt = stompSession.send("/publish/chat.1", articles)

            logger.info("receipt Id : ${receipt.receiptId}")
            logger.info("message sent : ${articles.size}")
        } catch (e: Exception) {
            logger.error("Failed to send message: ${e.message}")
        }
    }

    private fun checkConnection() {

        if (!::stompSession.isInitialized || !stompSession.isConnected) {
            logger.info("Connection is not established, try to connect")
            stompSession = stompSessionProvider.getSession()
        }
    }
}