package com.khb.articledailybatchserver.batch.sectionrankbatch.dto

import com.khb.articledailybatchserver.entity.Article


data class SectionArticleList (
    val section: String = "",
    val articles: List<Article> = listOf()
)