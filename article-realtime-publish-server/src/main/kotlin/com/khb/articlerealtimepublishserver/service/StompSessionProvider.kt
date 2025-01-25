package com.khb.articlerealtimepublishserver.service

import org.springframework.messaging.simp.stomp.StompSession

interface StompSessionProvider {

    fun getSession(): StompSession
}