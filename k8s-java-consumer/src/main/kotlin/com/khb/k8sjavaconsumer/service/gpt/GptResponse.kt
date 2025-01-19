package com.khb.k8sjavaconsumer.service.gpt

data class GptResponse (
    val summary: String,
    val tags: List<String>
)