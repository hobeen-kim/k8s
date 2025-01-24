package com.khb.articlerefineserver.repository

import com.khb.articlerefineserver.dto.Article
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

interface ArticleRepository: MongoRepository<Article, String> {

    fun deleteArticlesByTimeIsBefore(time: LocalDateTime)
}