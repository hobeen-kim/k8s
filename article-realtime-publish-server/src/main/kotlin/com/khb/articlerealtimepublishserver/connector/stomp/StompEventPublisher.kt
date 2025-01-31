package com.khb.articlerealtimepublishserver.connector.stomp

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("stomp")
class StompEventPublisher {
    private val listeners = mutableListOf<(TransportErrorEvent) -> Unit>()

    fun subscribe(listener: (TransportErrorEvent) -> Unit) {
        listeners.add(listener)
    }

    fun publishTransportError(event: TransportErrorEvent) {
        listeners.forEach { it(event) }
    }
}