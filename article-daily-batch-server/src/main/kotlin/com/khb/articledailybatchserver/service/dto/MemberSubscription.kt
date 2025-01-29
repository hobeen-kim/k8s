package com.khb.articledailybatchserver.service.dto

data class MemberSubscription (
    val memberId: String,
    val sections: List<String>
)