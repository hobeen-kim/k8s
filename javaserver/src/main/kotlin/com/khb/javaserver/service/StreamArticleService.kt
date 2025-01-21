package com.khb.javaserver.service

import com.khb.javaserver.entity.Article
import com.khb.javaserver.service.dto.StreamResponse


interface StreamArticleService {

    fun streamToRealTimeSubscribers(articles: List<Article>)
}