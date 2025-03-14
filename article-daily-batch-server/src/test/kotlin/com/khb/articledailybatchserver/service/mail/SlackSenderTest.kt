package com.khb.articledailybatchserver.service.mail

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SlackSenderTest {

    @Value("\${sender.slack.url}")
    private val slackUrl: String = "https://test.com"

@Test
fun `send with recipient list`() {
    // Arrange
    val slackSender = SlackSender(slackUrl)
    val title = "Test Title"
    val text = "<h1>Test Text</h1>"
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
}