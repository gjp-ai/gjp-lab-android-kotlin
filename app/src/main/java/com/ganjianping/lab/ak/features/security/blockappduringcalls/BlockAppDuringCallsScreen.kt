package com.ganjianping.lab.ak.features.security.blockappduringcalls

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun BlockAppDuringCallsScreen(controller: BlockAppDuringCallsController) {
    val state by controller.state.collectAsState()
    val context = LocalContext.current
    val phoneStatePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        controller.onPermissionResult()
    }
    BlockAppDuringCallsContent(
        state = state,
        onEnabledChange = { enabled ->
            controller.setEnabled(enabled)
            // Ask for READ_PHONE_STATE only after the user turns the feature on, never at launch.
            if (enabled && context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                phoneStatePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
            }
        },
        onToggleTestCall = controller::toggleTestCall
    )
}

@Composable
private fun BlockAppDuringCallsContent(
    state: CallBlockingState,
    onEnabledChange: (Boolean) -> Unit,
    onToggleTestCall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        SettingCard("Block app during calls") {
            Switch(
                checked = state.isEnabled,
                onCheckedChange = onEnabledChange
            )
            Text(
                text = "When enabled, GJP Lab blocks interaction while Android reports an ongoing call.",
                modifier = Modifier.padding(top = 12.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        SettingCard("Current feature status", modifier = Modifier.padding(top = 16.dp)) {
            Text(statusText(state), fontWeight = FontWeight.Bold)
            Text(
                callStateText(state),
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        SettingCard("Test", modifier = Modifier.padding(top = 16.dp)) {
            Button(
                onClick = onToggleTestCall,
                enabled = state.isEnabled &&
                    state.availability == CallMonitoringAvailability.MonitoringActive
            ) {
                Text(if (state.isTestCallActive) "End simulated call" else "Simulate active call")
            }
            Text(
                text = "Verify the blocking overlay without placing a real call.",
                modifier = Modifier.padding(top = 12.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        SettingCard("Call detection limitation", modifier = Modifier.padding(top = 16.dp)) {
            Text(
                text = "Android reports only calls exposed through its Telecom APIs. " +
                    "Third-party VoIP or video calls may not be detectable.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SettingCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Column(modifier = Modifier.padding(top = 14.dp), content = { content() })
        }
    }
}

private fun statusText(state: CallBlockingState): String = when (state.availability) {
    CallMonitoringAvailability.Off -> "Off"
    CallMonitoringAvailability.PermissionRequired -> "Permission required"
    CallMonitoringAvailability.Unavailable -> "Unavailable on this device"
    CallMonitoringAvailability.MonitoringActive -> "Monitoring active"
}

private fun callStateText(state: CallBlockingState): String = when {
    state.isTestCallActive -> "Simulated active call"
    state.hasActiveCall -> "Active call detected"
    else -> "No active call detected"
}

@Composable
private fun CallBlockingPreview(availability: CallMonitoringAvailability) {
    GJPLabTheme {
        Column(Modifier.background(MaterialTheme.colorScheme.background)) {
            BlockAppDuringCallsContent(
                state = CallBlockingState(isEnabled = true, availability = availability),
                onEnabledChange = {},
                onToggleTestCall = {}
            )
        }
    }
}

@Preview(name = "Monitoring active - light", showBackground = true, widthDp = 360)
@Composable
private fun MonitoringActivePreview() = CallBlockingPreview(CallMonitoringAvailability.MonitoringActive)

@Preview(name = "Monitoring active - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MonitoringActiveDarkPreview() = CallBlockingPreview(CallMonitoringAvailability.MonitoringActive)

@Preview(name = "Permission required - light", showBackground = true, widthDp = 360)
@Composable
private fun PermissionRequiredPreview() = CallBlockingPreview(CallMonitoringAvailability.PermissionRequired)

@Preview(name = "Permission required - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PermissionRequiredDarkPreview() = CallBlockingPreview(CallMonitoringAvailability.PermissionRequired)
