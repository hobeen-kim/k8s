package com.khb.articlerefineserver.producer.dlq

fun interface DeadLetterQueue{

    fun send(rawArticle: String)
}