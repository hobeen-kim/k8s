package com.khb.k8sjavaconsumer.dto

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

val mapper = jacksonObjectMapper()

@Document(collection = "article")
data class Article (
    @Id
    val articleId: String,
    val title: String,
    val url: String,
    val time: LocalDateTime,
    val section: String,
    val content: String,
) {

    companion object {

        fun fromString(data: String): Article? {
            val values = mapper.readValue(data, Map::class.java)

            val title = values["title"] as String?
            val url = values["url"] as String?
            val time = runCatching { (values["time"] as String?)?.let { toLocalDateTime(it) } }.getOrNull()
            val section = values["section"] as String?
            val content = values["content"] as String?

            if(title.isNullOrBlank() || url.isNullOrBlank() || time == null || section.isNullOrBlank() || content.isNullOrBlank()) {
                //TODO: DLQ
                println("Invalid data's title: $title")
                return null
            }

            val articleId = getArticleIdFrom(url)

            return Article(articleId, title, url, time, section, content)

        }

        private fun getArticleIdFrom(title: String): String {
            val titleRemovedFront = title.replace("https://www.yna.co.kr/view/", "")

            // 그다음 ? 뒤에 있는 문자열 제거
            return titleRemovedFront.split("?")[0]
        }

        private fun toLocalDateTime(timeString: String): LocalDateTime {

            val date = timeString.split(" ")[0]
            val time = timeString.split(" ")[1]

            val year = LocalDate.now().year
            val month = date.split("-")[0].toInt()
            val day = date.split("-")[1].toInt()

            val hour = time.split(":")[0].toInt()
            val minute = time.split(":")[1].toInt()

            return LocalDateTime.of(year, month, day, hour, minute)
        }
    }
}