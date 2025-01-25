package com.khb.articlerealtimepublishserver.connector.stomp

import com.khb.articlerealtimepublishserver.connector.StreamConnector
import com.khb.articlerealtimepublishserver.entity.Article
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.stereotype.Service

/**
 * stomp를 이용한 실시간 스트리밍 커넥터
 * connection closed 가 간헐적으로 일어나서 kafka 에 바로 넣는걸로 대체
 */
@Service
@Profile("stomp")
class StompStreamConnector(
    private val stompSessionProvider: StompSessionProvider,
): StreamConnector {

    private lateinit var stompSession: StompSession

    private val logger = LoggerFactory.getLogger(StompStreamConnector::class.java)

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