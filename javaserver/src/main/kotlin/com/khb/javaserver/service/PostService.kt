package com.khb.javaserver.service

import com.khb.javaserver.service.dto.PostServiceCreateRequest
import com.khb.javaserver.service.dto.PostServiceResponse
import com.khb.javaserver.service.dto.PostServiceUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostService {

    fun getPosts(
        page: Pageable
    ): Page<PostServiceResponse>

    fun getPost(postId: Long): PostServiceResponse

    fun createPost(request: PostServiceCreateRequest): Long

    fun updatePost(request: PostServiceUpdateRequest): Long

    fun deletePost(postId: Long)
}