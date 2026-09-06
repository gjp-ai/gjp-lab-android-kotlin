package com.ganjianping.lab.ak.features.others.deviceinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.features.others.deviceinfo.data.DeviceInfoRepository
import com.ganjianping.lab.ak.features.security.blockappduringcalls.CallBlockingCoordinator
import com.ganjianping.lab.ak.features.security.blockappduringcalls.CallBlockingHost
import org.koin.android.ext.android.inject

class DeviceInfoActivity : ComponentActivity() {
    private val deviceInfoRepository: DeviceInfoRepository by inject()
    private val callBlockingCoordinator: CallBlockingCoordinator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GJPLabTheme {
                CallBlockingHost(callBlockingCoordinator) {
                    DeviceInfoScreen(
                        repository = deviceInfoRepository,
                        onBack = ::finish
                    )
                }
            }
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
}
