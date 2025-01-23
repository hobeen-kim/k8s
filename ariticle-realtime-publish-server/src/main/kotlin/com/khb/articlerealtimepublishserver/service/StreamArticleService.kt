package com.khb.articlerealtimepublishserver.service

import com.khb.articlerealtimepublishserver.entity.Article

interface StreamArticleService {

    fun streamToRealTimeSubscribers(articles: List<Article>)
}