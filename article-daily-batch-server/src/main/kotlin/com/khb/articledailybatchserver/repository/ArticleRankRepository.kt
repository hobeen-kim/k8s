package com.khb.articledailybatchserver.repository

import com.khb.articledailybatchserver.entity.ArticleRank
import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleRankRepository: MongoRepository<ArticleRank, String>