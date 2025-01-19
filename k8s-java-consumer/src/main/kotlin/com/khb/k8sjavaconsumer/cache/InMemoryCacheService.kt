package com.khb.k8sjavaconsumer.cache

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class InMemoryCacheService: CacheService {
    private val cache: MutableMap<String, LocalDateTime> = mutableMapOf()

    override fun hasKey(key: String): Boolean {
        return cache.containsKey(key)
    }

    override fun set(key: String, value: LocalDateTime) {
        cache[key] = value
    }

    //매일 이전 24시간 이전의 데이터 삭제
    @Scheduled(cron = "0 0 0 * * *")
    fun clearCache() {
        val now = LocalDateTime.now()
        cache.entries.removeIf { it.value.isBefore(now.minusDays(1)) }
    }
}