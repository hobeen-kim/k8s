package com.khb.articlerealtimepublishserver.connector

import com.khb.articlerealtimepublishserver.entity.Article

interface StreamConnector {

    fun streamToRealTimeSubscribers(articles: List<Article>)
}