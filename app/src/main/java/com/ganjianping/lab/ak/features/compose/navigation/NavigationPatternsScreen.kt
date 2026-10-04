@file:OptIn(ExperimentalMaterial3Api::class)

package com.ganjianping.lab.ak.features.compose.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection
import com.ganjianping.lab.ak.shell.navigation.DetailRoute
import kotlinx.coroutines.launch

/**
 * Navigation samples. Pushed levels go through [onPush] to `ContentView`, which owns the feature pane's
 * stack, exactly as the HTTP response does; the screen never navigates on its own.
 */
@Composable
fun NavigationPatternsScreen(onPush: (DetailRoute) -> Unit) {
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    var isFullScreenOpen by rememberSaveable { mutableStateOf(false) }

    LabDemoPage(
        intro = "Navigation in this app is state: ContentView keeps the selected category, topic, and a stack of " +
            "pushed screens. Sheets and dialogs are state too: a Boolean decides whether they are shown."
    ) {
        LabDemoSection(
            title = "Push and pop",
            caption = "Each level is a DetailRoute pushed on the feature pane. Back pops one; Pop to root clears the stack."
        ) {
            Button(onClick = { onPush(DetailRoute.NavigationLevel(1)) }, modifier = Modifier.fillMaxWidth()) {
                Text("Push level 1")
            }
        }

        LabDemoSection(
            title = "Bottom sheet",
            caption = "ModalBottomSheet opens half-way on tall screens and can be dragged up to full height or down to close."
        ) {
            OutlinedButton(onClick = { isSheetOpen = true }) { Text("Show bottom sheet") }
        }

        LabDemoSection(
            title = "Full-screen dialog",
            caption = "A Dialog that ignores the platform width covers the whole window. It always offers a close button."
        ) {
            OutlinedButton(onClick = { isFullScreenOpen = true }) { Text("Show full-screen dialog") }
        }

        LabDemoSection(
            title = "Adaptive panes",
            caption = "The same selection state is shown as one, two, or three panes depending on the window width."
        ) {
            PaneRow("Under 840 dp", "One stack: categories → catalogue → feature, with Back between levels.")
            PaneRow("840–1199 dp", "Two panes: categories or catalogue, then the feature.")
            PaneRow("1200 dp and wider", "Three panes side by side; the selected rows stay outlined.")
        }
    }

    if (isSheetOpen) {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        ModalBottomSheet(onDismissRequest = { isSheetOpen = false }, sheetState = sheetState) {
            Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Bottom sheet", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Sheets show supporting content without leaving the screen. Drag the handle, tap outside, or press Back to close.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(onClick = {
                    // Animate the sheet away first, then remove it.
                    scope.launch { sheetState.hide() }.invokeOnCompletion { isSheetOpen = false }
                }) { Text("Close") }
            }
        }
    }

    if (isFullScreenOpen) {
        Dialog(
            onDismissRequest = { isFullScreenOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { isFullScreenOpen = false }) {
                            Icon(Icons.Outlined.Close, contentDescription = "Close")
                        }
                        Text("Full-screen dialog", style = MaterialTheme.typography.titleLarge)
                    }
                    Text(
                        "Use a full-screen dialog for a focused task, such as composing a message, that the user finishes or cancels.",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun PaneRow(width: String, layout: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(width, fontWeight = FontWeight.SemiBold)
        Text(layout, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun NavigationPatternsPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { NavigationPatternsScreen(onPush = {}) } }
}

@Preview(name = "Navigation - light", showBackground = true, widthDp = 360, heightDp = 1000)
@Composable
private fun NavigationPatternsLightPreview() = NavigationPatternsPreview()

@Preview(name = "Navigation - dark", showBackground = true, widthDp = 360, heightDp = 1000, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationPatternsDarkPreview() = NavigationPatternsPreview()
