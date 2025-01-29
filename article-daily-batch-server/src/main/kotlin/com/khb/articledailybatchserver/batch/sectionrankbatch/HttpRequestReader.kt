package com.khb.articledailybatchserver.batch.sectionrankbatch

import java.net.http.HttpRequest

interface HttpRequestReader<T> {

    fun request(httpRequest: HttpRequest)

    fun read(): T?


}