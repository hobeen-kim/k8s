package com.khb.articledailybatchserver.service.kakao

import org.springframework.stereotype.Service

@Service
class MockKakaoSender: KakaoSender {
    override fun send(title: String, text: String, to: List<String>) {
        println("Sending kakao to $to")
        println("Title: $title")
        println("Text: $text")
    }

    override fun send(title: String, text: String, to: String) {
        println("Sending kakao to $to")
        println("Title: $title")
        println("Text: $text")
    }
}