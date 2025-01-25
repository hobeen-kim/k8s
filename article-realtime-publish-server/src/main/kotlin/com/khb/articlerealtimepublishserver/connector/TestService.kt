package com.khb.articlerealtimepublishserver.connector

import com.khb.articlerealtimepublishserver.entity.Article
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@Profile("local")
class TestService(
    private val streamConnector: StreamConnector
) {

    @Scheduled(
        initialDelay = 10_000L,
        fixedRate = 10_000L
    )
    fun test() {

        println("send test article for local profile")

        streamConnector.streamToRealTimeSubscribers(
            listOf(Article(
                articleId = "test",
                title = "test",
                content = "test",
                url = "test",
                time = LocalDateTime.now(),
                section = "test",
                tags = listOf("test"),
                summary = "test"
            ))
        )
    }
}