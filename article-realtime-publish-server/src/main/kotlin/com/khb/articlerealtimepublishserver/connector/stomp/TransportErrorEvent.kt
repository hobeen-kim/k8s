package com.khb.articlerealtimepublishserver.connector.stomp

import org.springframework.messaging.simp.stomp.StompSession

class TransportErrorEvent(val session: StompSession, val exception: Throwable)
