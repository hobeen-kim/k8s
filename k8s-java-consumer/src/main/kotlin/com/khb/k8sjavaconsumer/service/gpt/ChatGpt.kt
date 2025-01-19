package com.khb.k8sjavaconsumer.service.gpt

import org.springframework.stereotype.Component

@Component
class ChatGpt: GptService {
    override fun summarizeArticle(text: String): String {
        return "This is a summarized article "
    }
}