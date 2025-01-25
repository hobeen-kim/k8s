package com.khb.articlewasserver.publisher

import com.khb.articlewasserver.entity.Article

interface ArticlePublisher {

    fun publish(articles: List<Article>)
}