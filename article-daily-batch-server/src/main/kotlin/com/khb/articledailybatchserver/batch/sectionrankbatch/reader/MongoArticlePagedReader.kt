package com.khb.articledailybatchserver.batch.sectionrankbatch.reader

import com.khb.articledailybatchserver.entity.ArticleRank
import com.khb.articledailybatchserver.repository.ArticleRankRepository
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader
import org.springframework.data.domain.PageRequest

open class MongoArticlePagedReader(
    private val articleRankRepository: ArticleRankRepository,
): AbstractPaginatedDataItemReader<ArticleRank>() {

    init {
        super.setExecutionContextName("mongoArticlePagedReader")
    }

    override fun doPageRead(): MutableIterator<ArticleRank> {

        val pageRequest = PageRequest.of(page, pageSize)

        return articleRankRepository.findAll(pageRequest).iterator()
    }
}