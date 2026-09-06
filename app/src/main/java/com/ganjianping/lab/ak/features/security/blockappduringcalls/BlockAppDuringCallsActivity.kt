package com.ganjianping.lab.ak.features.security.blockappduringcalls

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import org.koin.android.ext.android.inject

class BlockAppDuringCallsActivity : ComponentActivity() {
    private val coordinator: CallBlockingCoordinator by inject()
    private val phoneStatePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        coordinator.onPermissionResult()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by coordinator.state.collectAsState()
            GJPLabTheme {
                CallBlockingHost(coordinator) {
                    BlockAppDuringCallsScreen(
                        state = state,
                        onBack = ::finish,
                        onEnabledChange = ::setEnabled,
                        onToggleTestCall = coordinator::toggleTestCall
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        coordinator.startMonitoring()
    }

    override fun onPause() {
        coordinator.stopMonitoring()
        super.onPause()
    }

    private fun setEnabled(enabled: Boolean) {
        coordinator.setEnabled(enabled)
        if (enabled && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            phoneStatePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
        }
    }
}
