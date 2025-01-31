package com.khb.articledailybatchserver.service.mail

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MockMailSender: MailSender {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun send(title: String, text: String, to: List<String>) {
        logger.info("Sending mail to $to")
        logger.info("Title: $title")
        logger.info("Text: $text")
    }

    override fun send(title: String, text: String, to: String) {
        logger.info("Sending mail to $to")
        logger.info("Title: $title")
        logger.info("Text: $text")
    }


}