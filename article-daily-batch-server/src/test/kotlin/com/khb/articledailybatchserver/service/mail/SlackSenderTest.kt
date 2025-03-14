package com.khb.articledailybatchserver.service.mail

import com.khb.articledailybatchserver.batch.sectionrankbatch.dto.ArticleReport
import com.khb.articledailybatchserver.entity.Article
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class SlackSenderTest {

    @Value("\${sender.slack.url}")
    private val slackUrl: String = "https://test.com"

@Test
fun `send with recipient list`() {
    // Arrange
    val title = "Article Daily Report"
    val articleReports = listOf(
        ArticleReport("정치", listOf(
            Article("articleId", "전 재산 10억 고대의료원 기부 한종섭 할머니 '국민포장 석류장'", "url", LocalDateTime.now(), "정치", "content", listOf("tag1", "tag2"), "GPT API를 통해 요약된 텍스트입니다."),
            Article("articleId", "수업중 교사의 '尹대통령' 욕설 의혹에 교육당국, 사실관계 조사", "url", LocalDateTime.now(), "정치", "content", listOf("tag1", "tag2"), "GPT API를 통해 요약된 텍스트입니다."),
            Article("articleId", "헌재 신뢰도 53％…선관위 44％, 공수처 29％, 검찰 26％[한국갤럽]", "url", LocalDateTime.now(), "정치", "content", listOf("tag1", "tag2"), "GPT API를 통해 요약된 텍스트입니다."),
        )),
        ArticleReport("경제", listOf(
            Article("articleId", "CJ올리브영, 보유 지분 22.58%로 늘려…'지배력 강화'", "url", LocalDateTime.now(), "경제", "content", listOf("tag1", "tag2"), "GPT API를 통해 요약된 텍스트입니다."),
            Article("articleId", "중국 '내수 살리기' 예고에 증시도 급등…연고점 찍어", "url", LocalDateTime.now(), "경제", "content", listOf("tag1", "tag2"), "GPT API를 통해 요약된 텍스트입니다."),
            Article("articleId", "홈플러스 담은 5개 리츠 자산 1조2천억원…국토부 점검", "url", LocalDateTime.now(), "경제", "content", listOf("tag1", "tag2"), "GPT API를 통해 요약된 텍스트입니다."),
        )),
    )

    val text = createEmailReport(articleReports, listOf("정치", "경제"))

    val slackSender = SlackSender(slackUrl)
    val to = listOf("Test Channel 1", "Test Channel 2")

    // Act
    slackSender.send(title, text, to)

    // Assert
    // (Assertions or verifications would go here if mocking was applied)
}

    @Test
    fun `send with single recipient string`() {
        // Arrange
        val slackSender = SlackSender("https://test.com")
        val title = "Test Title"
        val text = "Test Text"
        val to = "Test Channel"

        // Act
        slackSender.send(title, text, to)

        // Assert
        // (Assertions or verifications would go here if mocking was applied)
    }

    private fun createEmailReport(articleReports: List<ArticleReport>, targetSection: List<String>): String {

        val date = LocalDate.now()
        val sb = StringBuilder()

        sb.append("Article Daily Report\n")
        sb.append("Date: $date\n\n")

        for (articleReport in articleReports) {

            if (!targetSection.contains(articleReport.section)) {
                continue
            }

            sb.append(articleReport.toEmailReportFormat())
        }

        return sb.toString()
    }
}