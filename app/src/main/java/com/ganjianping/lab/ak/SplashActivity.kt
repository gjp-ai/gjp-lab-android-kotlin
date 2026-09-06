package com.ganjianping.lab.ak

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.ganjianping.lab.ak.common.config.AppConfig
import com.ganjianping.lab.ak.common.network.NetworkConnectivity
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.features.security.blockappduringcalls.CallBlockingCoordinator
import com.ganjianping.lab.ak.features.security.blockappduringcalls.CallBlockingHost
import com.ganjianping.lab.ak.integration.firebase.FirebaseIntegration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashActivity : ComponentActivity() {
    private val firebaseIntegration: FirebaseIntegration by inject()
    private val callBlockingCoordinator: CallBlockingCoordinator by inject()
    private var remoteConfigLoaded = false
    private var minimumSplashTimeElapsed = false
    private var maintenanceEnabled = false
    private var hasOpenedMainActivity = false
    private var remoteConfigTimeoutJob: kotlinx.coroutines.Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GJPLabTheme {
                CallBlockingHost(callBlockingCoordinator) {
                    SplashScreen()
                }
            }
        }

        loadRemoteConfig()
        lifecycleScope.launch {
            delay(AppConfig.MINIMUM_SPLASH_DURATION_MS)
            minimumSplashTimeElapsed = true
            openMainActivityIfReady()
        }
    }

    override fun onResume() {
        super.onResume()
        callBlockingCoordinator.startMonitoring()
    }

    override fun onPause() {
        callBlockingCoordinator.stopMonitoring()
        super.onPause()
    }

    private fun loadRemoteConfig() {
        if (!NetworkConnectivity.isAvailable(this)) {
            completeRemoteConfig(enabled = false)
            return
        }

        remoteConfigTimeoutJob = lifecycleScope.launch {
            delay(AppConfig.REMOTE_CONFIG_TIMEOUT_MS)
            completeRemoteConfig(enabled = false)
        }

        firebaseIntegration.fetchMaintenanceMode { enabled ->
            completeRemoteConfig(enabled)
        }
    }

    private fun completeRemoteConfig(enabled: Boolean) {
        if (remoteConfigLoaded) return

        remoteConfigTimeoutJob?.cancel()
        maintenanceEnabled = enabled
        remoteConfigLoaded = true
        openMainActivityIfReady()
    }

    private fun openMainActivityIfReady() {
        if (!remoteConfigLoaded || !minimumSplashTimeElapsed || hasOpenedMainActivity) return

        hasOpenedMainActivity = true
        startActivity(
            Intent(this, MainActivity::class.java).putExtra(
                MainActivity.EXTRA_MAINTENANCE_ENABLED,
                maintenanceEnabled
            )
        )
        finish()
    }
}
