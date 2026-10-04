package com.ganjianping.lab.ak.shell.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpURLConnectionActivity
import com.ganjianping.lab.ak.features.integration.firebase.FirebaseFeatureActivity
import com.ganjianping.lab.ak.features.others.deviceinfo.DeviceInfoActivity
import com.ganjianping.lab.ak.features.security.blockappduringcalls.BlockAppDuringCallsActivity
import com.ganjianping.lab.ak.features.security.blockappduringcalls.BlockAppDuringCallsController
import com.ganjianping.lab.ak.features.security.blockappduringcalls.CallBlockingHost
import org.koin.android.ext.android.inject

class FeatureCatalogActivity : ComponentActivity() {
    private val callBlocker: BlockAppDuringCallsController by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getStringExtra(EXTRA_CATEGORY)
            ?.let { name -> NavigationCategory.entries.firstOrNull { it.name == name } }
            ?: run {
                finish()
                return
            }

        enableEdgeToEdge()
        setContent {
            GJPLabTheme {
                CallBlockingHost(callBlocker) {
                    FeatureCatalogScreen(
                        category = category,
                        onBack = ::finish,
                        onFeatureSelected = ::openFeature
                    )
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

    private fun openFeature(route: FeatureRoute) {
        val activity = when (route) {
            FeatureRoute.DeviceInfo -> DeviceInfoActivity::class.java
            FeatureRoute.HttpURLConnection -> HttpURLConnectionActivity::class.java
            FeatureRoute.Firebase -> FirebaseFeatureActivity::class.java
            FeatureRoute.BlockAppDuringCalls -> BlockAppDuringCallsActivity::class.java
        }
        startActivity(Intent(this, activity))
    }

    companion object {
        private const val EXTRA_CATEGORY = "com.ganjianping.lab.ak.extra.CATEGORY"

        fun createIntent(context: Context, category: NavigationCategory): Intent =
            Intent(context, FeatureCatalogActivity::class.java)
                .putExtra(EXTRA_CATEGORY, category.name)
    }
}
