package com.khb.articledailybatchserver.service

interface MailSender {

    fun send(title: String, text: String, to: List<String>)

    fun send(title: String, text: String, to: String)
}