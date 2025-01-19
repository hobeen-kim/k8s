package com.khb.k8sjavaconsumer.service.gpt

fun interface GptService {

    fun summarizeArticle(text: String): String
}