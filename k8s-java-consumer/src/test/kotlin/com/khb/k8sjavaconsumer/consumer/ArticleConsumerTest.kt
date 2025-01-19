package com.khb.k8sjavaconsumer.consumer

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.khb.k8sjavaconsumer.cache.CacheService
import com.khb.k8sjavaconsumer.dto.Article
import com.khb.k8sjavaconsumer.repository.ArticleRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SpringBootTest
//여기 localhost:19092 를 수정하면 application-test.yml 도 수정해야 함
@EmbeddedKafka(partitions = 1,
    brokerProperties = ["listeners=PLAINTEXT://localhost:19092"],
    ports = [19092]
)
@ActiveProfiles("test")
class ArticleConsumerTest {

    @Autowired
    lateinit var articleConsumer: ArticleConsumer
    @Autowired
    lateinit var mockProducer: MockProducer
    @Mock
    lateinit var articleRepository: ArticleRepository
    @Mock
    lateinit var cacheService: CacheService

    @Test
    @DisplayName("Kafka Listener 테스트 - ArticleConsumer")
    fun listenerTest() {
        //given
        given(cacheService.hasKey(anyString())).willReturn(false)
        given(articleRepository.save(any())).willReturn(Article(
            articleId = "AKR20250119018300004",
            title = "title",
            url = "https://www.yna.co.kr/view/AKR20250119018300004?1234",
            time = LocalDateTime.of(2025, 1, 17, 10, 52),
            section = "정치",
            content = "content"
        ))

        val latch = CountDownLatch(1)

        // articleRepository.save가 호출될 때 latch를 감소시키도록 설정
        willAnswer {
            latch.countDown()
            null
        }.given(articleRepository).save(any())

        val data = """
            {
                "title": "title",
                "url": "https://www.yna.co.kr/view/AKR20250119018300004?1234",
                "time": "01-17 10:52",
                "section": "정치",
                "content": "content"
            }
        """.trimIndent()

        val payload = jsonMapper().readTree(data)

        //when
        mockProducer.send("raw-article", payload)

        //wait
        assertTrue(latch.await(100, TimeUnit.SECONDS))  // 10초 동안 대기

        //then
        verify(articleRepository, times(1)).save(any())

    }
}