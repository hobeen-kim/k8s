package com.khb.articledailybatchserver.service.member.dto

data class MemberSubscription (
    val memberId: String,
    val email: String,
    val kakaoId: String,
    val sections: List<String>
)