package com.khb.articlerealtimepublishserver.service

import com.khb.articlerealtimepublishserver.entity.Article
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TestService(
    private val streamArticleService: StreamArticleService
) {

    @Scheduled(
        initialDelay = 10_000L,
        fixedRate = 10_000L
    )
    fun test() {
        streamArticleService.streamToRealTimeSubscribers(
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