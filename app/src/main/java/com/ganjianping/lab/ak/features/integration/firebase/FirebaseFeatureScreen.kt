package com.ganjianping.lab.ak.features.integration.firebase

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FirebaseFeatureScreen(integration: FirebaseIntegration) {
    val context = LocalContext.current
    var analyticsStatus by rememberSaveable { mutableStateOf("No event sent yet") }
    var crashlyticsStatus by rememberSaveable { mutableStateOf("No non-fatal exception recorded yet") }
    var remoteConfigStatus by rememberSaveable { mutableStateOf("Not fetched yet") }
    var performanceStatus by rememberSaveable { mutableStateOf("No custom trace completed yet") }
    var messagingStatus by rememberSaveable { mutableStateOf("Token not loaded yet") }
    var messagingToken by remember { mutableStateOf<String?>(null) }
    var tokenCopied by remember { mutableStateOf(false) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) messagingStatus = "Notifications are disabled; FCM token is still available"
    }

    LaunchedEffect(Unit) {
        integration.logFirebaseFeatureOpened()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    FirebaseFeatureContent(
        analyticsStatus = analyticsStatus,
        crashlyticsStatus = crashlyticsStatus,
        remoteConfigStatus = remoteConfigStatus,
        performanceStatus = performanceStatus,
        messagingStatus = messagingStatus,
        messagingToken = messagingToken,
        tokenCopied = tokenCopied,
        onLogAnalytics = {
            integration.logFirebaseFeatureOpened()
            analyticsStatus = "${FirebaseConstants.EventFirebaseFeatureOpened} sent"
        },
        onRecordCrashlytics = {
            integration.recordCrashlyticsDemo()
            crashlyticsStatus = "Non-fatal demo exception recorded"
        },
        onFetchRemoteConfig = {
            remoteConfigStatus = "Fetching maintenance flag..."
            integration.fetchMaintenanceMode { enabled ->
                remoteConfigStatus = "${FirebaseConstants.RemoteConfigMaintenanceEnabled} = $enabled"
            }
        },
        onRunPerformance = {
            performanceStatus = "Running custom trace..."
            integration.runPerformanceDemo { durationMillis ->
                performanceStatus = "${FirebaseConstants.PerformanceDemoTrace} completed in $durationMillis ms"
            }
        },
        onFetchMessagingToken = {
            messagingStatus = "Loading FCM token..."
            tokenCopied = false
            integration.fetchMessagingToken { token, error ->
                messagingToken = token
                messagingStatus = token?.let { "Full token loaded" }
                    ?: "Token error: ${error?.message ?: "unknown error"}"
            }
        },
        onCopyToken = {
            messagingToken?.let { token ->
                val clipboard = context.getSystemService(ClipboardManager::class.java)
                clipboard.setPrimaryClip(ClipData.newPlainText("FCM registration token", token))
                tokenCopied = true
            }
        },
        onSubscribeToTopic = {
            messagingStatus = "Subscribing to ${FirebaseConstants.MessagingDemoTopic}..."
            integration.subscribeToMessagingDemoTopic { success ->
                messagingStatus = if (success) {
                    "Subscribed to ${FirebaseConstants.MessagingDemoTopic}"
                } else {
                    "Topic subscription failed"
                }
            }
        }
    )
}

@Composable
private fun FirebaseFeatureContent(
    analyticsStatus: String,
    crashlyticsStatus: String,
    remoteConfigStatus: String,
    performanceStatus: String,
    messagingStatus: String,
    messagingToken: String?,
    tokenCopied: Boolean,
    onLogAnalytics: () -> Unit,
    onRecordCrashlytics: () -> Unit,
    onFetchRemoteConfig: () -> Unit,
    onRunPerformance: () -> Unit,
    onFetchMessagingToken: () -> Unit,
    onCopyToken: () -> Unit,
    onSubscribeToTopic: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Run small, safe demonstrations of the Firebase services used by GJPLab.",
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))

        FirebaseActionCard(
            title = "Analytics",
            description = "Send a ${FirebaseConstants.EventFirebaseFeatureOpened} event.",
            status = analyticsStatus,
            actionLabel = "Log event",
            onAction = onLogAnalytics
        )
        FirebaseActionCard(
            title = "Crashlytics",
            description = "Record a non-fatal demo exception without crashing the app.",
            status = crashlyticsStatus,
            actionLabel = "Record exception",
            onAction = onRecordCrashlytics
        )
        FirebaseActionCard(
            title = "Remote Config",
            description = "Fetch the maintenance-mode flag from Firebase.",
            status = remoteConfigStatus,
            actionLabel = "Fetch flag",
            onAction = onFetchRemoteConfig
        )
        FirebaseActionCard(
            title = "Performance Monitoring",
            description = "Run and complete a short custom performance trace.",
            status = performanceStatus,
            actionLabel = "Run trace",
            onAction = onRunPerformance
        )
        FirebaseActionCard(
            title = "Cloud Messaging",
            description = "Retrieve the FCM token or subscribe to the demo topic.",
            status = messagingStatus,
            actionLabel = "Get token",
            onAction = onFetchMessagingToken,
            extraContent = messagingToken?.let { token ->
                {
                    SelectionContainer {
                        RowWithCopyButton(
                            token = token,
                            tokenCopied = tokenCopied,
                            onCopyToken = onCopyToken
                        )
                    }
                }
            }
        )
        Button(
            onClick = onSubscribeToTopic,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Subscribe to demo topic")
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun FirebaseActionCard(
    title: String,
    description: String,
    status: String,
    actionLabel: String,
    onAction: () -> Unit,
    extraContent: (@Composable () -> Unit)? = null
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                description,
                modifier = Modifier.padding(top = 6.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                status,
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            extraContent?.invoke()
            Button(
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
private fun RowWithCopyButton(
    token: String,
    tokenCopied: Boolean,
    onCopyToken: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            token,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
        IconButton(onClick = onCopyToken) {
            Icon(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = if (tokenCopied) "Token copied" else "Copy FCM token"
            )
        }
    }
    if (tokenCopied) {
        Text(
            "Copied to clipboard",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun FirebasePreview(token: String?) {
    GJPLabTheme {
        Column(Modifier.background(MaterialTheme.colorScheme.background)) {
            FirebaseFeatureContent(
                analyticsStatus = "${FirebaseConstants.EventFirebaseFeatureOpened} sent",
                crashlyticsStatus = "No non-fatal exception recorded yet",
                remoteConfigStatus = "${FirebaseConstants.RemoteConfigMaintenanceEnabled} = false",
                performanceStatus = "No custom trace completed yet",
                messagingStatus = if (token == null) "Token not loaded yet" else "Full token loaded",
                messagingToken = token,
                tokenCopied = false,
                onLogAnalytics = {},
                onRecordCrashlytics = {},
                onFetchRemoteConfig = {},
                onRunPerformance = {},
                onFetchMessagingToken = {},
                onCopyToken = {},
                onSubscribeToTopic = {}
            )
        }
    }
}

@Preview(name = "Firebase - light", showBackground = true, widthDp = 360, heightDp = 1400)
@Composable
private fun FirebaseFeaturePreview() = FirebasePreview(token = null)

@Preview(name = "Firebase - dark", showBackground = true, widthDp = 360, heightDp = 1400, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FirebaseFeatureDarkPreview() = FirebasePreview(token = null)

@Preview(name = "Token loaded - light", showBackground = true, widthDp = 360, heightDp = 1400)
@Composable
private fun FirebaseTokenPreview() = FirebasePreview(token = "fcm-demo-token-0123456789")

@Preview(name = "Token loaded - dark", showBackground = true, widthDp = 360, heightDp = 1400, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FirebaseTokenDarkPreview() = FirebasePreview(token = "fcm-demo-token-0123456789")
