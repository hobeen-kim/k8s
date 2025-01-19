package com.khb.k8sjavaconsumer.service.gpt

class ChatGpt: GptService {
    override fun summarizeArticle(text: String): String {
        return "This is a summarized article"
    }
}