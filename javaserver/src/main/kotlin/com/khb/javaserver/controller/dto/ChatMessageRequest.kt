package com.khb.javaserver.controller.dto

data class ChatMessageRequest(
    val username: String,
    val content: String
)