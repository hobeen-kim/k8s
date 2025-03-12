package com.khb.articledailybatchserver.batch.sectionrankbatch

import com.fasterxml.jackson.databind.ObjectMapper
import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.ArticleReport
import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.YnaArticleRankList
import com.khb.articledailybatchserver.batch.sectionrankbatch.writer.ArticleSender
import com.khb.articledailybatchserver.entity.ArticleRank
import com.khb.articledailybatchserver.repository.ArticleRankRepository
import com.khb.articledailybatchserver.repository.ArticleRepository
import com.khb.articledailybatchserver.service.kakao.KakaoSender
import com.khb.articledailybatchserver.service.mail.MailSender
import com.khb.articledailybatchserver.service.member.MemberService
import com.khb.articledailybatchserver.service.member.MockMemberService
import org.slf4j.LoggerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate


@Configuration
class DailySectionRankJobConfig(
    private val articleRankRepository: ArticleRankRepository,
    private val articleRepository: ArticleRepository,
    private val objectMapper: ObjectMapper,
    private val jobLauncher: JobLauncher,
    private val memberService: MemberService,
    private val mailSender: MailSender,
    private val kakaoSender: KakaoSender,
) {

    companion object {
        const val JOB_NAME = "dailySectionRankJob"
        const val EXTRACT_STEP_NAME = "dailyExtractRankStep"
        const val REPORT_STEP_NAME = "dailyReportRankStep"
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun jobParameters(): JobParameters {
        return JobParametersBuilder()
            .toJobParameters()
    }

    @Bean
    fun runJob(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): JobExecution {
        val parameters = jobParameters()
        return jobLauncher.run(dailySectionRankJob(
            jobRepository,
            transactionManager
        ), parameters)
    }

    /**
     * Job 등록
     */
    fun dailySectionRankJob(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .incrementer(RunIdIncrementer()) // sequential id
            .start(dailyExtractRankStep(jobRepository, transactionManager))
            .next(dailyReportRankStep(jobRepository, transactionManager))
            .build()
    }

    @Bean
    @JobScope
    fun dailyExtractRankStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder(EXTRACT_STEP_NAME, jobRepository).chunk<YnaArticleRankList, ArticleRank>(100, transactionManager)
            .reader(articleRankHttpReader())
            .processor(articleRankProcessor())
            .writer(articleRankWriter())
            .build();
    }

    @Bean
    @JobScope
    fun dailyReportRankStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder(REPORT_STEP_NAME, jobRepository).chunk<ArticleRank, ArticleReport>(100, transactionManager)
            .reader(articleRankReader())
            .processor(articleMappingProcessor())
            .writer(articleReportSender())
            .build();
    }

    @Bean
    @StepScope
    fun articleRankHttpReader(
    ): ArticleRankJsonItemReader {
        return ArticleRankJsonItemReader(
            objectMapper = objectMapper,
        )
    }

    @Bean
    @StepScope
    fun articleRankProcessor(
    ): ItemProcessor<YnaArticleRankList, ArticleRank> {

        return ItemProcessor<YnaArticleRankList, ArticleRank> { ynaArticleRankList ->

            if(ynaArticleRankList.results.isEmpty()) {
                return@ItemProcessor null
            }

            val articles = ynaArticleRankList.results
            val section = ynaArticleRankList.section

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

        logger.info("articleRankWriter")

        return RepositoryItemWriterBuilder<ArticleRank>()
            .repository(articleRankRepository)
            .methodName("insert")
            .build()
    }

    @Bean
    @StepScope
    fun articleRankReader(
    ): MongoArticlePagedReader {
        return MongoArticlePagedReader(
            articleRankRepository = articleRankRepository,
        )
    }

    @Bean
    @StepScope
    fun articleMappingProcessor(): ItemProcessor<ArticleRank, ArticleReport> {
        return ItemProcessor<ArticleRank, ArticleReport> { articleRank ->

            val section = articleRank.section
            val articleIds = articleRank.articleIds

            val articles = articleRepository.findAllById(articleIds)

            ArticleReport(
                section = section,
                articles = articles,
            )
        }
    }

    @Bean
    @StepScope
    fun articleReportSender(): ArticleSender {

        logger.info("articleReportSender")

        return ArticleSender(
            date = LocalDate.now(),
            memberService = memberService,
            mailSender = mailSender,
            kakaoSender = kakaoSender,
        )
    }
}