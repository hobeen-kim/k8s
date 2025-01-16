package com.khb.javaserver.service.dto

data class PostServiceUpdateRequest (
    val postId: Long,
    val title: String,
    val content: String
)