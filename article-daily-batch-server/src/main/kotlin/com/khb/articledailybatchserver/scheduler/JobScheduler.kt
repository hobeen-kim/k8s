package com.khb.articledailybatchserver.scheduler

import com.khb.articledailybatchserver.SECTIONS
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class JobScheduler(
    private val dailySectionRankJob: Job,
    private val jobLauncher: JobLauncher,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "0 * * * * *")
    fun runDailyReport() {
        runDailySectionRankJob()

        sendReportJob()
    }

    private fun runDailySectionRankJob() {
        for (section in SECTIONS) {
            val jobParameters = JobParametersBuilder()
                .addString("section", section)
                .toJobParameters()

            val jobExecution = jobLauncher.run(dailySectionRankJob, jobParameters)

            logger.info("$section Job Execution: ${jobExecution.status}")
        }
    }

    private fun sendReportJob() {

    }
}