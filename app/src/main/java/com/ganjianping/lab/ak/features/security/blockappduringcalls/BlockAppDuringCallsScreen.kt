package com.ganjianping.lab.ak.features.security.blockappduringcalls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockAppDuringCallsScreen(
    state: CallBlockingState,
    onBack: () -> Unit,
    onEnabledChange: (Boolean) -> Unit,
    onToggleTestCall: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Block App During Calls") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
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
}

@Composable
private fun SettingCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
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

@Preview(name = "Monitoring active", showBackground = true)
@Composable
private fun BlockAppDuringCallsScreenPreview() {
    GJPLabTheme {
        BlockAppDuringCallsScreen(
            state = CallBlockingState(
                isEnabled = true,
                availability = CallMonitoringAvailability.MonitoringActive
            ),
            onBack = {},
            onEnabledChange = {},
            onToggleTestCall = {}
        )
    }
}

@Preview(
    name = "Permission required - dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BlockAppDuringCallsPermissionPreview() {
    GJPLabTheme {
        BlockAppDuringCallsScreen(
            state = CallBlockingState(
                isEnabled = true,
                availability = CallMonitoringAvailability.PermissionRequired
            ),
            onBack = {},
            onEnabledChange = {},
            onToggleTestCall = {}
        )
    }
}
