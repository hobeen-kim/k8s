package com.khb.javaserver.controller.dto

import com.khb.javaserver.service.dto.PostServiceCreateRequest

data class PostApiCreateRequest (
    val title: String,
    val content: String
) {
    fun toServiceRequest(): PostServiceCreateRequest {
        return PostServiceCreateRequest(title, content)
    }
}