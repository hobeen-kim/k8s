package com.khb.javaserver.entity

import jakarta.persistence.*

@Entity
@Table(name = "post")
class Post (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val postId: Long?,
    var title: String,
    var content: String
) {

    companion object {
        fun of(title: String, content: String): Post {
            return Post(
                postId = null,
                title = title,
                content = content
            )
        }
    }

    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }
}