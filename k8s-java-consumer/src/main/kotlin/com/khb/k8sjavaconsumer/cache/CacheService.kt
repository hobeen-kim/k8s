package com.khb.k8sjavaconsumer.cache

import java.time.LocalDate
import java.time.LocalDateTime

interface CacheService {

    fun hasKey(key: String): Boolean

    fun set(key: String, value: LocalDateTime)
}