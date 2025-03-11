package com.khb.articledailybatchserver.service.mail

import org.junit.jupiter.api.Test

class SlackSenderTest {

@Test
fun `send with recipient list`() {
    // Arrange
    val slackSender = SlackSender("https://test.com")
    val title = "Test Title"
    val text = "Test Text"
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