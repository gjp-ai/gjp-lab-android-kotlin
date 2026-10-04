package com.ganjianping.lab.ak.shell

import androidx.compose.runtime.Composable
import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpResponse
import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpURLConnectionRepository
import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpURLConnectionScreen
import com.ganjianping.lab.ak.features.integration.firebase.FirebaseFeatureScreen
import com.ganjianping.lab.ak.features.integration.firebase.FirebaseIntegration
import com.ganjianping.lab.ak.features.others.deviceinfo.DeviceInfoRepository
import com.ganjianping.lab.ak.features.others.deviceinfo.DeviceInfoScreen
import com.ganjianping.lab.ak.features.security.blockappduringcalls.BlockAppDuringCallsController
import com.ganjianping.lab.ak.features.security.blockappduringcalls.BlockAppDuringCallsScreen
import com.ganjianping.lab.ak.shell.navigation.FeatureRoute

/** The Koin-provided objects the feature screens need; `MainActivity` injects them and passes them down. */
class FeatureDependencies(
    val deviceInfoRepository: DeviceInfoRepository,
    val httpURLConnectionRepository: HttpURLConnectionRepository,
    val firebaseIntegration: FirebaseIntegration,
    val callBlocker: BlockAppDuringCallsController
)

/** The one place that maps a [FeatureRoute] to its screen. */
@Composable
fun FeatureDestination(
    route: FeatureRoute,
    dependencies: FeatureDependencies,
    onResponse: (HttpResponse) -> Unit
) {
    when (route) {
        FeatureRoute.DeviceInfo -> DeviceInfoScreen(dependencies.deviceInfoRepository)
        FeatureRoute.HttpURLConnection -> HttpURLConnectionScreen(dependencies.httpURLConnectionRepository, onResponse)
        FeatureRoute.Firebase -> FirebaseFeatureScreen(dependencies.firebaseIntegration)
        FeatureRoute.BlockAppDuringCalls -> BlockAppDuringCallsScreen(dependencies.callBlocker)
    }
}
