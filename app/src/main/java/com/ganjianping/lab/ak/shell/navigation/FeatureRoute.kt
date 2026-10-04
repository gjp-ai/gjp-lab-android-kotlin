package com.ganjianping.lab.ak.shell.navigation

import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpResponse

/** A catalogue topic; the selected one is shown in the feature pane. */
enum class FeatureRoute {
    DeviceInfo,
    HttpURLConnection,
    Firebase,
    BlockAppDuringCalls
}

/** A screen pushed on top of a feature inside the feature pane. */
sealed interface DetailRoute {
    data class Response(val response: HttpResponse) : DetailRoute
}
