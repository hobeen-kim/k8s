package com.khb.articlerealtimepublishserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ArticleRealtimePublishServerApplication

fun main(args: Array<String>) {
    runApplication<ArticleRealtimePublishServerApplication>(*args)
}
