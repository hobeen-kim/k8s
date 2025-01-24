package com.khb.articlerefineserver.service.expirer

import com.khb.articlerefineserver.repository.ArticleRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class Expirer(
    private val articleRepository: ArticleRepository
) {

    /**
     * 2025.01.19
     * 하루마다 이틀 전꺼 만료시킴
     * 아직은 disable, 굳이 만료가 필요없음
     * 만료가 필요한 상황 = 데이터가 많아서 검색이 느려지거나 용량이 커져서 요금이 많오거나 스토리지가 부족할 때
     */

    @Scheduled(cron = "0 0 0 * * *")
    fun expire() {

        val criteria = LocalDateTime.now().minusDays(2)

//        articleRepository.deleteArticlesByTimeIsBefore(criteria)
    }

}