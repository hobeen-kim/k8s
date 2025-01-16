package com.khb.javaserver.controller

import com.khb.javaserver.controller.dto.ApiResponse
import com.khb.javaserver.controller.dto.PostApiCreateRequest
import com.khb.javaserver.controller.dto.PostApiUpdateRequest
import com.khb.javaserver.service.PostService
import com.khb.javaserver.service.dto.PostServiceResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun getPosts(
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<PostServiceResponse>>> {

        val posts = postService.getPosts(pageable)

        return ResponseEntity.ok(ApiResponse.ok(posts))
    }

    @GetMapping("/posts/{postId}")
    fun getPostById(
        @PathVariable postId: Long
    ): ResponseEntity<ApiResponse<PostServiceResponse>> {

        val post = postService.getPost(postId)

        return ResponseEntity.ok(ApiResponse.ok(post))
    }

    @PostMapping("/posts")
    fun createPost(
        @RequestBody request: PostApiCreateRequest
    ): ResponseEntity<ApiResponse<Long>> {
        val postId = postService.createPost(request.toServiceRequest())

        return ResponseEntity
            .created(URI.create("/posts/$postId"))
            .body(ApiResponse.created());
    }

    @PatchMapping("/posts/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody request: PostApiUpdateRequest
    ): ResponseEntity<Unit> {
        postService.updatePost(request.toServiceRequest(postId))

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/posts/{postId}")
    fun deletePost(
        @PathVariable postId: Long
    ): ResponseEntity<Unit> {
        postService.deletePost(postId)

        return ResponseEntity.noContent().build()
    }
}