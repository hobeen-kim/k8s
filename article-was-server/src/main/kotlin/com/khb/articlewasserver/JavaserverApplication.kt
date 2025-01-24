package com.khb.articlewasserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class JavaserverApplication

fun main(args: Array<String>) {
    runApplication<JavaserverApplication>(*args)
}
