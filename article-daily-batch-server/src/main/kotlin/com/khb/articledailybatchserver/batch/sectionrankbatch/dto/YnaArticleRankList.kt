package com.khb.articledailybatchserver.batch.sectionrankbatch.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class YnaArticleRankList(
    @JsonProperty("size") val size: Int = 0,
    @JsonProperty("dataTime") val dataTime: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("inflowChannel") val inflowChannel: String? = null,
    @JsonProperty("section") val section: String? = null,
    @JsonProperty("results") val results: List<YnaArticleRank> = emptyList()
)