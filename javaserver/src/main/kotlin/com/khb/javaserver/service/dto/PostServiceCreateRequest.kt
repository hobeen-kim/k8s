package com.khb.javaserver.service.dto

import com.khb.javaserver.entity.Post

data class PostServiceCreateRequest (
    val title: String,
    val content: String
) {
    fun toEntity(): Post {
        return Post.of(title, content)
    }
}