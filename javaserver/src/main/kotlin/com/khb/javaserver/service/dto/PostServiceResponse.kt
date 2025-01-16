package com.khb.javaserver.service.dto

import com.khb.javaserver.entity.Post

data class PostServiceResponse (
    val postId: Long,
    val title: String,
    val content: String
) {

    companion object {
        fun of(post: Post): PostServiceResponse {

            requireNotNull(post.postId) { "postId must not be null" }

            return PostServiceResponse(post.postId!!, post.title, post.content)
        }
    }
}