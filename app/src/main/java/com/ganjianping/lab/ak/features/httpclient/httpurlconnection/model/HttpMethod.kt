package com.ganjianping.lab.ak.features.httpclient.httpurlconnection.model

enum class HttpMethod(val supportsPayload: Boolean) {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false)
}
