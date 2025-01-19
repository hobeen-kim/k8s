package com.khb.k8sjavaconsumer.service.gpt

import org.springframework.stereotype.Component

@Component
class ChatGpt: GptService {
    override fun summarizeArticle(text: String): GptResponse {
        return GptResponse("This is a summary", listOf("tag1", "tag2"))
    }
}