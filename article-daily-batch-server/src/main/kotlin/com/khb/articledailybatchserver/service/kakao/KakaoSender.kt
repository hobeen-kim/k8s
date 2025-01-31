package com.khb.articledailybatchserver.service.kakao

interface KakaoSender {

    fun send(title: String, text: String, to: List<String>)

    fun send(title: String, text: String, to: String)
}