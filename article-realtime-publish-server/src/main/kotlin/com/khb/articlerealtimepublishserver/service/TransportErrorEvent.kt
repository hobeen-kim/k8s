package com.khb.articlerealtimepublishserver.service

import org.springframework.messaging.simp.stomp.StompSession

class TransportErrorEvent(val session: StompSession, val exception: Throwable)
