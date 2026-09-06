package com.ganjianping.lab.ak.features.security.blockappduringcalls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhoneDisabled
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CallBlockingHost(
    coordinator: CallBlockingCoordinator,
    content: @Composable () -> Unit
) {
    val state by coordinator.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if (state.isBlocking) {
            CallBlockingOverlay(
                isTestCallActive = state.isTestCallActive,
                onEndTestCall = coordinator::toggleTestCall
            )
        }
    }
}

@Composable
private fun CallBlockingOverlay(isTestCallActive: Boolean, onEndTestCall: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
            .semantics { liveRegion = LiveRegionMode.Assertive }
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.PhoneDisabled,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "App unavailable during a call",
            modifier = Modifier.padding(top = 18.dp),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = if (isTestCallActive) {
                "A simulated call is active. End it to continue testing."
            } else {
                "End your call to continue using GJP Lab."
            },
            modifier = Modifier.padding(top = 10.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        if (isTestCallActive) {
            Button(onClick = onEndTestCall, modifier = Modifier.padding(top = 24.dp)) {
                Text("End simulated call")
            }
        }
    }
}
