package com.khb.k8sjavaconsumer.producer.dlq

fun interface DeadLetterQueue{

    fun send(rawArticle: String)
}