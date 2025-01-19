package com.khb.k8sjavaconsumer.utils.exception

class InvalidDataException: BusinessException {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}