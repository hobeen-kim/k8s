package com.khb.javaserver.controller.dto

import com.khb.javaserver.service.dto.PostServiceUpdateRequest

data class PostApiUpdateRequest (
    val title: String,
    val content: String
) {
    fun toServiceRequest(postId: Long): PostServiceUpdateRequest {
        return PostServiceUpdateRequest(
            postId = postId,
            title = title,
            content = content
        )
    }
}