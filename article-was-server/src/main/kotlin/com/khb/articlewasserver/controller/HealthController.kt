package com.khb.articlewasserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class HealthController {

    @GetMapping("/")
    fun healthCheck(): String {
        return "OK"
    }

    @GetMapping("/info")
    fun info(): Map<String, String> {
        // 환경 변수에서 Pod 이름 가져오기
        val podName = System.getenv("HOSTNAME") ?: "unknown"

        // Node 이름 가져오기
        val nodeName = System.getenv("NODE_NAME") ?: "unknown"

        return mapOf(
            "pod" to podName,
            "node" to nodeName
        )
    }
}