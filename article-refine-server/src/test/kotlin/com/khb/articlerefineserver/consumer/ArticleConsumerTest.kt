package com.khb.articlerefineserver.consumer

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.khb.articlerefineserver.dto.Article
import com.khb.articlerefineserver.producer.dlq.DeadLetterQueue
import com.khb.articlerefineserver.producer.refinedarticleproducer.RefinedArticleProducer
import com.khb.articlerefineserver.repository.ArticleRepository
import com.khb.articlerefineserver.service.gpt.GptService
import com.khb.articlerefineserver.service.gpt.GptResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import java.lang.Thread.sleep
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SpringBootTest
//여기 localhost:19092 를 수정하면 application-test.yml 도 수정해야 함
@EmbeddedKafka(partitions = 1,
    brokerProperties = [
        "listeners=PLAINTEXT://localhost:19092",
        "auto.create.topics.enable=true",
        "offsets.topic.replication.factor=1",
        "group.initial.rebalance.delay.ms=0"  // 리밸런싱 지연 제거
   ],
    ports = [19092],

)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ArticleConsumerTest {

    @Autowired
    lateinit var articleConsumer: ArticleConsumer
    @Autowired
    lateinit var mockProducer: MockProducer
    //verify를 위해 mock 객체로 만들어줌 나중에 deperecated 되면 바꾸자 ㅎ
    @MockBean
    lateinit var articleRepository: ArticleRepository
    @MockBean
    lateinit var refinedArticleProducer: RefinedArticleProducer
    @MockBean
    lateinit var deadLetterQueue: DeadLetterQueue
    @MockBean
    lateinit var gptService: GptService

    @Test
    @DisplayName("Kafka Listener 테스트 - ArticleConsumer")
    fun listenerTest() {
        //given
        given(articleRepository.save(any())).willReturn(Article(
            articleId = "AKR20250119018300004",
            title = "title",
            url = "https://www.yna.co.kr/view/AKR20250119018300004?1234",
            time = LocalDateTime.of(2025, 1, 17, 10, 52),
            section = "정치",
            content = "content",
            tags = emptyList(),
        ))

        given(articleRepository.existsById("AKR20250119018300004")).willReturn(false)
        given(gptService.summarizeArticle("content")).willReturn(GptResponse("summary"))

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
        //wait for initializing producer config
        sleep(2000)

        mockProducer.send("raw-article-dev", payload)

        //wait for receiving message
        latch.await(10, TimeUnit.SECONDS)  // 10초 동안 대기 (10초 안에 latch 가 0이 되면 10초 이전에 테스트 진행)

        //then
        verify(articleRepository, times(1)).save(any())
        verify(gptService, times(1)).summarizeArticle("content")
    }
}