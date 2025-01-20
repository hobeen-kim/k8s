package com.khb.javaserver.stream

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class KafkaTestStreamConfig(
    private val simpMessagingTemplate: SimpMessagingTemplate  // STOMP 메시지 전송을 위한 템플릿
) {

    @Scheduled(fixedRate = 10000L)
    fun streams() {
        simpMessagingTemplate.convertAndSend(
            "/subscribe/chat.1",  // 구독할 destination
            "Hello, World!"  // 전송할 메시지
        )
    }
}