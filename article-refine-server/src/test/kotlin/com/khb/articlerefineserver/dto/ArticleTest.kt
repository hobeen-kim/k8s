package com.khb.articlerefineserver.dto

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.time.LocalDateTime


class ArticleTest {

    @Test
    @DisplayName("json 데이터에서 Article 객체를 생성한다.")
    fun fromString() {
        // given
        val json = """
            {
                "title": "title",
                "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                "time": "01-17 10:52",
                "section": "정치",
                "content": "content"
            }
        """.trimIndent()

        //when
        val article = Article.fromString(json)

        //then
        Assertions.assertThat(article).isNotNull
        Assertions.assertThat(article?.articleId).isEqualTo("AKR20250119018300004")
        Assertions.assertThat(article?.title).isEqualTo("title")
        Assertions.assertThat(article?.url).isEqualTo("https://www.yna.co.kr/view/AKR20250119018300004?1234")
        Assertions.assertThat(article?.time).isEqualTo(LocalDateTime.of(2025, 1, 17, 10, 52))
        Assertions.assertThat(article?.section).isEqualTo("정치")
        Assertions.assertThat(article?.content).isEqualTo("content")

    }

    @TestFactory
    @DisplayName("json 데이터에 특정 필드가 없다면 Article 이 null 이다.")
    fun fromStringWithNull(): List<DynamicTest> {
        return listOf(
            dynamicTest("title 필드가 없을 때") {
                // given
                val json = """
                    {
                        "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                        "time": "01-17 10:52",
                        "section": "정치",
                        "content": "content"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            },
            dynamicTest("url 필드가 없을 때") {
                // given
                val json = """
                    {
                        "title": "title",
                        "time": "01-17 10:52",
                        "section": "정치",
                        "content": "content"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            },
            dynamicTest("time 필드가 없을 때") {
                // given
                val json = """
                    {
                        "title": "title",
                        "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                        "section": "정치",
                        "content": "content"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            },
            dynamicTest("section 필드가 없을 때") {
                // given
                val json = """
                    {
                        "title": "title",
                        "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                        "time": "01-17 10:52",
                        "content": "content"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            },
            dynamicTest("content 필드가 없을 때") {
                // given
                val json = """
                    {
                        "title": "title",
                        "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                        "time": "01-17 10:52",
                        "section": "정치"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            }
        )
    }

    @TestFactory
    @DisplayName("json 데이터에 특정 필드가 블랭크라면 Article 이 null 이다.")
    fun fromStringWithBlank(): List<DynamicTest> {
        return listOf(
            dynamicTest("title 필드가 공백일 때") {
                // given
                val json = """
                    {
                        "title": "  ",
                        "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                        "time": "01-17 10:52",
                        "section": "정치",
                        "content": "content"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            },
            dynamicTest("url 필드가 공백일 때") {
                // given
                val json = """
                    {
                        "title": "title",
                        "url": "  ",
                        "time": "01-17 10:52",
                        "section": "정치",
                        "content": "content"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            },
            dynamicTest("time 필드가 공백일 때") {
                // given
                val json = """
                    {
                        "title": "title",
                        "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                        "time": "  ",
                        "section": "정치",
                        "content": "content"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            },
            dynamicTest("section 필드가 공백일 때") {
                // given
                val json = """
                    {
                        "title": "title",
                        "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                        "time": "01-17 10:52",
                        "section": "  ",
                        "content": "content"
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            },
            dynamicTest("content 필드가 공백일 때") {
                // given
                val json = """
                    {
                        "title": "title",
                        "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                        "time": "01-17 10:52",
                        "section": "정치",
                        "content": "  "
                    }
                """.trimIndent()

                //when
                val article = Article.fromString(json)

                //then
                Assertions.assertThat(article).isNull()
            }
        )
    }

    @Test
    @DisplayName("json 데이터에 시간 필드가 형식이 맞지 않다면 Article 이 null 이다.")
    fun fromStringWithInvalidTime() {
        //given
        val json = """
            {
                "title": "title",
                "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                "time": "01월17일 10:52:00",
                "section": "정치",
                "content": "content"
            }
        """.trimIndent()

        //when
        val article = Article.fromString(json)

        //then
        Assertions.assertThat(article).isNull()
    }


}