package com.khb.k8sjavaconsumer.repository

import com.khb.k8sjavaconsumer.dto.Article
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

interface ArticleRepository: MongoRepository<Article, String> {

    fun deleteArticlesByTimeIsBefore(time: LocalDateTime)
}