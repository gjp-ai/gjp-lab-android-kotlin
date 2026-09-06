package com.ganjianping.lab.ak.features.httpclient.httpurlconnection

import android.util.Log
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.features.security.blockappduringcalls.CallBlockingCoordinator
import com.ganjianping.lab.ak.features.security.blockappduringcalls.CallBlockingHost
import org.koin.android.ext.android.inject

class HttpURLConnectionActivity : ComponentActivity() {
    private val httpURLConnectionRepository: HttpURLConnectionRepository by inject()
    private val callBlockingCoordinator: CallBlockingCoordinator by inject()
    private var activityErrorMessage by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GJPLabTheme {
                CallBlockingHost(callBlockingCoordinator) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        HttpURLConnectionScreen(
                            repository = httpURLConnectionRepository,
                            onBack = ::finish,
                            onError = { message ->
                                activityErrorMessage = message
                                Log.e(TAG, "Request failed: $message")
                            },
                            onResponse = { response ->
                                activityErrorMessage = null
                                Log.i(TAG, "Response activity opened with HTTP ${response.statusCode}")
                                HttpResponseActivity.start(this@HttpURLConnectionActivity, response)
                            }
                        )
                        activityErrorMessage?.let { message ->
                            HttpErrorBanner(
                                message = message,
                                onDismiss = { activityErrorMessage = null }
                            )
                        }
                    }
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

    companion object {
        private const val TAG = "HttpURLConnection"
    }
}
