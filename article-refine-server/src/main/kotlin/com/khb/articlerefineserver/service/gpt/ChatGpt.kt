package com.khb.articlerefineserver.service.gpt

import org.springframework.stereotype.Component

@Component
class ChatGpt: GptService {
    override fun summarizeArticle(text: String): GptResponse {

        //2 ~ 4 초 사이에 sleep (gpt api 호출 시간)
        Thread.sleep((2000..4000).random().toLong())

        return GptResponse(
            summary = "GPT API를 통해 요약된 텍스트입니다."
        )
    }
}