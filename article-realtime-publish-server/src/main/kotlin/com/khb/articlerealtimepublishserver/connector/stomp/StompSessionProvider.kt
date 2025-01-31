package com.khb.articlerealtimepublishserver.connector.stomp

import org.springframework.messaging.simp.stomp.StompSession

interface StompSessionProvider {

    fun getSession(): StompSession
}