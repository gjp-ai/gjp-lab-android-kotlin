package com.ganjianping.lab.ak.features.httpclient.httpurlconnection.model

data class HttpResponse(
    val statusCode: Int,
    val body: String,
    val headers: Map<String, String>
)
