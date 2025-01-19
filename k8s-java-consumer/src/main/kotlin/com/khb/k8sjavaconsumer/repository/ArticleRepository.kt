package com.khb.k8sjavaconsumer.repository

import com.khb.k8sjavaconsumer.dto.Article
import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleRepository: MongoRepository<Article, String> {
}