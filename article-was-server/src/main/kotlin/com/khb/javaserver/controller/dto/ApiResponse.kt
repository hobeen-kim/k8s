package com.khb.javaserver.controller.dto

data class ApiResponse<T> (
    val status: Int,
    val message: String,
    val data: T?
) {
    companion object {
        fun <K> ok(data: K?): ApiResponse<K> {
            return ApiResponse(200, "OK", data)
        }

        fun <K> created(): ApiResponse<K> {
            return ApiResponse(201, "CREATED", null)
        }

        fun <K> error(message: String): ApiResponse<K> {
            return ApiResponse(500, message, null)
        }
    }
}