package com.ganjianping.lab.ak.features.catalog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ganjianping.lab.ak.DashboardCategory
import com.ganjianping.lab.ak.FeatureAction
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.features.deviceinfo.DeviceInfoActivity
import com.ganjianping.lab.ak.features.firebase.FirebaseFeatureActivity
import com.ganjianping.lab.ak.features.httpurlconnection.HttpURLConnectionActivity

class FeatureCatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getStringExtra(EXTRA_CATEGORY)
            ?.let { name -> DashboardCategory.entries.firstOrNull { it.name == name } }
            ?: run {
                finish()
                return
            }

        enableEdgeToEdge()
        setContent {
            GJPLabTheme {
                FeatureCatalogScreen(
                    category = category,
                    onBack = ::finish,
                    onFeatureSelected = ::openFeature
                )
            }
        }
    }

    private fun openFeature(action: FeatureAction) {
        val activity = when (action) {
            FeatureAction.DeviceInfo -> DeviceInfoActivity::class.java
            FeatureAction.HttpURLConnection -> HttpURLConnectionActivity::class.java
            FeatureAction.Firebase -> FirebaseFeatureActivity::class.java
        }
        startActivity(Intent(this, activity))
    }

    companion object {
        private const val EXTRA_CATEGORY = "com.ganjianping.lab.ak.extra.CATEGORY"

        fun createIntent(context: Context, category: DashboardCategory): Intent =
            Intent(context, FeatureCatalogActivity::class.java)
                .putExtra(EXTRA_CATEGORY, category.name)
    }
}
