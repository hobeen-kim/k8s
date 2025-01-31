package com.khb.articledailybatchserver.service.member

import com.khb.articledailybatchserver.service.member.dto.MemberSubscription
import org.springframework.stereotype.Service

@Service
class MockMemberService: MemberService {

    override fun findMemberSubscriptionInfos(): List<MemberSubscription> {
        return listOf(
            MemberSubscription("1", "sksjsksh32@gmail.com", "kakao1", listOf("전체", "정치")),
            MemberSubscription("2", "ghqls38@naver.com", "kakao2", listOf("정치", "경제")),
        )
    }


}