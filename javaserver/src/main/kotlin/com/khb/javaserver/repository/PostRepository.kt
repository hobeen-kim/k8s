package com.khb.javaserver.repository

import com.khb.javaserver.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
}