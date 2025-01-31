package com.khb.articledailybatchserver.batch.sectionrankbatch.writer

import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.ArticleReport
import com.khb.articledailybatchserver.service.kakao.KakaoSender
import com.khb.articledailybatchserver.service.mail.MailSender
import com.khb.articledailybatchserver.service.member.MemberService
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import java.time.LocalDate

open class ArticleSender(
    private val date: LocalDate,
    private val memberService: MemberService,
    private val mailSender: MailSender,
    private val kakaoSender: KakaoSender,
): ItemWriter<ArticleReport> {
    override fun write(chunk: Chunk<out ArticleReport>) {

        val articleReports = chunk.items

        val subscriptionInfos = memberService.findMemberSubscriptionInfos()

        subscriptionInfos.forEach { info ->
            val personalReport = createEmailReport(articleReports, info.sections)

            mailSender.send("Article Daily Report", personalReport, info.email)
            kakaoSender.send("Article Daily Report", personalReport, info.kakaoId)
        }
    }

    private fun createEmailReport(articleReports: List<ArticleReport>, targetSection: List<String>): String {
        val sb = StringBuilder()

        sb.append("Article Daily Report\n")
        sb.append("Date: $date\n\n")

        for (articleReport in articleReports) {

            if (!targetSection.contains(articleReport.section)) {
                continue
            }

            sb.append(articleReport.toEmailReportFormat())
        }

        println("$sb")

        return sb.toString()
    }
}