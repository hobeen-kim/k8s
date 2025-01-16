package com.khb.javaserver.service.dto

import com.khb.javaserver.repository.PostRepository
import com.khb.javaserver.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    private val postRepository: PostRepository
): PostService {

    override fun getPosts(page: Pageable): Page<PostServiceResponse> {
        val posts = postRepository.findAll(page)

        return posts.map { PostServiceResponse.of(it) }
    }

    override fun getPost(postId: Long): PostServiceResponse {
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }

        return PostServiceResponse.of(post)
    }

    override fun createPost(request: PostServiceCreateRequest): Long {
        val post = postRepository.save(request.toEntity())

        return post.postId ?: throw IllegalArgumentException("Invalid post id")
    }

    override fun updatePost(request: PostServiceUpdateRequest): Long {
        val post = postRepository.findById(request.postId).orElseThrow { IllegalArgumentException("Post not found") }

        post.update(
            title = request.title,
            content = request.content
        )

        return post.postId ?: throw IllegalArgumentException("Invalid post id")
    }

    override fun deletePost(postId: Long) {
        postRepository.deleteById(postId)
    }
}