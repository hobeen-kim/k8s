package com.khb.articlerealtimepublishserver.service

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@Component
class StompSessionProviderImpl(
    private val stompClient: WebSocketStompClient,
    private val environment: Environment,
    private val customStompSessionHandler: CustomStompSessionHandler,
    private val eventPublisher: StompEventPublisher,

    @Value("\${custom.stomp.url}")
    private val stompUrl: String,
    @Value("\${custom.stomp.port}")
    private val stompPort: Int
): StompSessionProvider {

    private lateinit var stompSession: StompSession
    private val initialReconnectCount = 3
    private val reconnectRemains = AtomicInteger(initialReconnectCount)

    private val logger = LoggerFactory.getLogger(StompSessionProviderImpl::class.java)

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

        eventPublisher.subscribe { event ->
            reconnect()
        }

        initializeConnection()
    }

    @PreDestroy
    fun cleanup() {
        if (stompSession.isConnected) {
            stompSession.disconnect()
        }
    }

    private fun initializeConnection() {

        logger.info("initialize connection")

        stompSession = stompClient.connectAsync(
            "ws://$stompUrl:$stompPort/ws-connect",
            customStompSessionHandler
        ).get(5, TimeUnit.SECONDS)
    }

    private fun reconnect() {
        try {
            if(reconnectRemains.getAndDecrement() >= 0) {
                logger.error("Reconnecting... remains: ${reconnectRemains.get()}")
                initializeConnection()
            } else {
                reconnectRemains.set(initialReconnectCount)
                logger.error("Reconnection failed: Maximum reconnection count reached")
            }
        } catch (e: Exception) {
            logger.error("Reconnection failed: ${e.message}")
        }
    }
    private fun invalidProfile() = !environment.activeProfiles.contains("prod") && !environment.activeProfiles.contains("local")
}