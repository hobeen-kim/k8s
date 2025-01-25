package com.khb.articlerealtimepublishserver.service

import org.springframework.stereotype.Component

@Component
class StompEventPublisher {
    private val listeners = mutableListOf<(TransportErrorEvent) -> Unit>()

    fun subscribe(listener: (TransportErrorEvent) -> Unit) {
        listeners.add(listener)
    }

    fun publishTransportError(event: TransportErrorEvent) {
        listeners.forEach { it(event) }
    }
}