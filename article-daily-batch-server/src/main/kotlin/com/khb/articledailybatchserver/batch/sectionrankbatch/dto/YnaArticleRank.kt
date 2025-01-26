package com.khb.articledailybatchserver.batch.sectionrankbatch.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class YnaArticleRank(
    @JsonProperty("count") val count: Int = 0,
    @JsonProperty("rank") val rank: Int = 0,
    @JsonProperty("countDiff") val countDiff: Int = 0,
    @JsonProperty("rankDiff") val rankDiff: Int = 0,
    @JsonProperty("cr_id") val cr_id: String = "",
    @JsonProperty("cr_title") val cr_title: String = "",
    @JsonProperty("url") val url: String = "",
    @JsonProperty("cr_img") val cr_img: String? = null,
    @JsonProperty("inflowChannel") val inflowChannel: String? = null,
    @JsonProperty("cr_sec") val cr_sec: String? = null,
    @JsonProperty("cr_kw") val cr_kw: String? = null,
    @JsonProperty("cr_ddt") val cr_ddt: LocalDateTime = LocalDateTime.now()
)