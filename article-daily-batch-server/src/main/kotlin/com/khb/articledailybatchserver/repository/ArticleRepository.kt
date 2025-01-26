package com.khb.articledailybatchserver.repository

import com.khb.articledailybatchserver.entity.Article
import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleRepository: MongoRepository<Article, String>