package com.khb.articlerefineserver.utils.exception

abstract class BusinessException: RuntimeException {

    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}