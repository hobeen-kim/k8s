package com.khb.articlerefineserver.service.gpt

fun interface GptService {

    fun summarizeArticle(text: String): GptResponse
}