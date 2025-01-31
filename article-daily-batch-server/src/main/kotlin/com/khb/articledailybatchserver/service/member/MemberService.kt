package com.khb.articledailybatchserver.service.member

import com.khb.articledailybatchserver.service.member.dto.MemberSubscription

interface MemberService {

    fun findMemberSubscriptionInfos(): List<MemberSubscription>
}