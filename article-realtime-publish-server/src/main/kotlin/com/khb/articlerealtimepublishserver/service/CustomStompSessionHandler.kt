package com.khb.articlerealtimepublishserver.service

import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.stereotype.Component

@Component
class CustomStompSessionHandler(
    private val eventPublisher: StompEventPublisher
): StompSessionHandlerAdapter() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handleTransportError(session: StompSession, exception: Throwable) {
        // 연결 에러 처리
        logger.error("Transport error: ${exception.message}")
        exception.printStackTrace()
        eventPublisher.publishTransportError(TransportErrorEvent(session, exception))
    }

    override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
        logger.info("Connected to the server")
    }
}