package com.khb.articledailybatchserver.service.mail

interface MailSender {

    fun send(title: String, text: String, to: List<String>)

    fun send(title: String, text: String, to: String)
}