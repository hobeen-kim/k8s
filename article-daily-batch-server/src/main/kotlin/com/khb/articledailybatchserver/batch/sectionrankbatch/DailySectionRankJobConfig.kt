package com.khb.articledailybatchserver.batch.sectionrankbatch

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.YnaArticleRankList
import com.khb.articledailybatchserver.entity.ArticleRank
import com.khb.articledailybatchserver.repository.ArticleRankRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder
import org.springframework.batch.item.json.JacksonJsonObjectReader
import org.springframework.batch.item.json.JsonItemReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.data.domain.Sort
import org.springframework.transaction.PlatformTransactionManager
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDate
import java.util.*


@Configuration
class DailySectionRankJobConfig(
    private val articleRankRepository: ArticleRankRepository,
    private val objectMapper: ObjectMapper,
) {

    companion object {
        const val JOB_NAME = "dailySectionRankJob"
        const val STEP_NAME = "dailySectionRankStep"
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Job 등록
     */
    @Bean
    fun dailySectionRankJob(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): Job {
        return JobBuilder(JOB_NAME, jobRepository)
        .incrementer(RunIdIncrementer()) // sequential id
            .start(dailySectionRankStep(jobRepository, transactionManager)) // step 설정
            .build();
    }

    @Bean
    @JobScope
    fun dailySectionRankStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder(STEP_NAME, jobRepository).chunk<YnaArticleRankList, ArticleRank>(100, transactionManager)
            .reader(articleRankReader())
            .processor(articleRankProcessor())
            .writer(articleRankWriter())
            .build();
    }

    @Bean
    @StepScope
    fun articleRankReader(
        @Value("#{jobParameters['section']}") section: String? = null
    ): ArticleRankItemReader {
        return ArticleRankItemReader(
            objectMapper = objectMapper,
            section = section ?: ""
        )
    }

    @Bean
    @StepScope
    fun articleRankProcessor(
        @Value("#{jobParameters['section']}") section: String? = "all"
    ): ItemProcessor<YnaArticleRankList, ArticleRank> {

        return ItemProcessor<YnaArticleRankList, ArticleRank> { ynaArticleRankList ->

            if(ynaArticleRankList.results.isEmpty()) {
                logger.info("No articles in section: $section")
                return@ItemProcessor null
            }

            val articles = ynaArticleRankList.results

            val articleIds = articles.map { it.cr_id }

            val date = LocalDate.now()

            ArticleRank(
                sectionDateId = "${section ?: "all"}-$date",
                section = section ?: "all",
                date = date,
                articleIds = articleIds
            )
        }
    }

    @Bean
    @StepScope
    fun articleRankWriter(): RepositoryItemWriter<ArticleRank> {

        return RepositoryItemWriterBuilder<ArticleRank>()
            .repository(articleRankRepository)
            .methodName("insert")
            .build()
    }

}