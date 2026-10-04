package com.ganjianping.lab.ak.shell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpURLConnectionRepository
import com.ganjianping.lab.ak.features.integration.firebase.FirebaseIntegration
import com.ganjianping.lab.ak.features.others.deviceinfo.DeviceInfoRepository
import com.ganjianping.lab.ak.features.security.blockappduringcalls.BlockAppDuringCallsController
import com.ganjianping.lab.ak.features.security.blockappduringcalls.CallBlockingHost
import com.ganjianping.lab.ak.shell.startup.MaintenanceScreen
import org.koin.android.ext.android.inject

/** The app's only navigation Activity: maintenance, or `ContentView` with every category, topic, and feature. */
class MainActivity : ComponentActivity() {
    private val firebaseIntegration: FirebaseIntegration by inject()
    private val callBlocker: BlockAppDuringCallsController by inject()
    private val deviceInfoRepository: DeviceInfoRepository by inject()
    private val httpURLConnectionRepository: HttpURLConnectionRepository by inject()
    private val featureDependencies by lazy {
        FeatureDependencies(deviceInfoRepository, httpURLConnectionRepository, firebaseIntegration, callBlocker)
    }
    private var maintenanceEnabled by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        maintenanceEnabled = intent.getBooleanExtra(EXTRA_MAINTENANCE_ENABLED, false)
        setContent {
            GJPLabTheme {
                CallBlockingHost(callBlocker) {
                    when {
                        maintenanceEnabled -> MaintenanceScreen(onRetry = ::loadRemoteConfig)
                        else -> ContentView { route, onResponse ->
                            FeatureDestination(route, featureDependencies, onResponse)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callBlocker.startMonitoring()
    }

    override fun onPause() {
        callBlocker.stopMonitoring()
        super.onPause()
    }

    private fun loadRemoteConfig() {
        firebaseIntegration.fetchMaintenanceMode { enabled ->
            maintenanceEnabled = enabled
        }
    }

    companion object {
        const val EXTRA_MAINTENANCE_ENABLED =
            "com.ganjianping.lab.ak.extra.MAINTENANCE_ENABLED"
    }
}
