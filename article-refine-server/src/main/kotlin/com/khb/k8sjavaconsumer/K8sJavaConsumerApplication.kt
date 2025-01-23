package com.khb.k8sjavaconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
class K8sJavaConsumerApplication

fun main(args: Array<String>) {
	runApplication<K8sJavaConsumerApplication>(*args)
}
