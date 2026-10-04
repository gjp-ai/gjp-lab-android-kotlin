@file:OptIn(ExperimentalFoundationApi::class)

package com.ganjianping.lab.ak.features.compose.buttons

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** The orders offered by the sort menu. */
enum class SortOrder(val title: String) { Newest("Newest"), Oldest("Oldest"), Name("Name") }

/** How long the save sample pretends to work, in milliseconds. */
const val SaveDurationMillis = 1_500L

@Composable
fun ButtonsScreen() {
    var lastAction by rememberSaveable { mutableStateOf("None yet") }
    var sortOrder by rememberSaveable { mutableStateOf(SortOrder.Newest) }
    var isSortMenuOpen by rememberSaveable { mutableStateOf(false) }
    var isConfirmingDelete by rememberSaveable { mutableStateOf(false) }
    var isCardMenuOpen by rememberSaveable { mutableStateOf(false) }
    // Not saved: the save coroutine does not survive recreation, so neither should its flag.
    var isSaving by remember { mutableStateOf(false) }
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var likes by rememberSaveable { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    fun record(action: String) {
        lastAction = action
    }

    LabDemoPage(
        intro = "A button pairs a label with an action. Its style changes how it looks; dialogs, menus, and " +
            "gestures change how people reach the action."
    ) {
        Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Last action")
                Text(lastAction, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        LabDemoSection(
            title = "Styles and emphasis",
            caption = "Material 3 has five button styles, from most to least emphasis. Use one filled button per area for the main action."
        ) {
            Button(onClick = { record("Filled") }, modifier = Modifier.fillMaxWidth()) { Text("Filled") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(onClick = { record("Tonal") }, modifier = Modifier.weight(1f)) { Text("Tonal") }
                ElevatedButton(onClick = { record("Elevated") }, modifier = Modifier.weight(1f)) { Text("Elevated") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { record("Outlined") }, modifier = Modifier.weight(1f)) { Text("Outlined") }
                TextButton(onClick = { record("Text") }, modifier = Modifier.weight(1f)) { Text("Text") }
            }
        }

        LabDemoSection(
            title = "Destructive action and confirmation",
            caption = "Ask before deleting. The confirm button uses the error colours so the risk is visible."
        ) {
            OutlinedButton(onClick = { isConfirmingDelete = true }) {
                Icon(Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Text("Delete draft", modifier = Modifier.padding(start = ButtonDefaults.IconSpacing))
            }
            if (isConfirmingDelete) {
                AlertDialog(
                    onDismissRequest = { isConfirmingDelete = false; record("Cancelled delete") },
                    title = { Text("Delete this draft?") },
                    text = { Text("This sample does not delete anything.") },
                    confirmButton = {
                        TextButton(
                            onClick = { isConfirmingDelete = false; record("Deleted draft") },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) { Text("Delete") }
                    },
                    dismissButton = {
                        TextButton(onClick = { isConfirmingDelete = false; record("Cancelled delete") }) { Text("Cancel") }
                    }
                )
            }
        }

        LabDemoSection(
            title = "Menus",
            caption = "A DropdownMenu groups related choices behind one button. Long-press the card below for its own menu."
        ) {
            Box {
                OutlinedButton(onClick = { isSortMenuOpen = true }) {
                    Icon(Icons.AutoMirrored.Outlined.Sort, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Text("Sort: ${sortOrder.title}", modifier = Modifier.padding(start = ButtonDefaults.IconSpacing))
                }
                DropdownMenu(expanded = isSortMenuOpen, onDismissRequest = { isSortMenuOpen = false }) {
                    SortOrder.entries.forEach { order ->
                        DropdownMenuItem(
                            text = { Text(order.title) },
                            onClick = {
                                sortOrder = order
                                isSortMenuOpen = false
                                record("Sorted by ${order.title.lowercase()}")
                            }
                        )
                    }
                }
            }
            Box {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { record("Opened card") },
                            onLongClick = { isCardMenuOpen = true },
                            onLongClickLabel = "Show card actions"
                        )
                ) {
                    Text("Weekly report · long-press for actions", modifier = Modifier.padding(16.dp))
                }
                DropdownMenu(expanded = isCardMenuOpen, onDismissRequest = { isCardMenuOpen = false }) {
                    listOf("Share", "Duplicate", "Archive").forEach { action ->
                        DropdownMenuItem(text = { Text(action) }, onClick = { isCardMenuOpen = false; record(action) })
                    }
                }
            }
        }

        LabDemoSection(
            title = "Gestures with accessible alternatives",
            caption = "Double-tap the photo to like it. A gesture alone is invisible to TalkBack, so the same action is also a named custom action."
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
                    .combinedClickable(
                        onClick = { record("Tapped photo") },
                        onDoubleClick = { likes += 1; record("Liked photo") }
                    )
                    .semantics {
                        customActions = listOf(
                            CustomAccessibilityAction("Like") { likes += 1; record("Liked photo"); true }
                        )
                    }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Photo · $likes ${if (likes == 1) "like" else "likes"}", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        LabDemoSection(
            title = "Async action and touch target",
            caption = "Disable a button while its work runs so it cannot start twice. Icon-only buttons need a label and a 48 dp target."
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        isSaving = true
                        scope.launch {
                            delay(SaveDurationMillis)
                            isSaving = false
                            record("Saved")
                        }
                    },
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Text("Saving…", modifier = Modifier.padding(start = 8.dp))
                    } else {
                        Text("Save")
                    }
                }
                // IconToggleButton is 48 dp and announces its checked state.
                IconToggleButton(checked = isFavorite, onCheckedChange = { isFavorite = it; record(if (it) "Added favourite" else "Removed favourite") }) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove favourite" else "Add favourite",
                        tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonsPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { ButtonsScreen() } }
}

@Preview(name = "Buttons & actions - light", showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
private fun ButtonsLightPreview() = ButtonsPreview()

@Preview(name = "Buttons & actions - dark", showBackground = true, widthDp = 360, heightDp = 1500, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ButtonsDarkPreview() = ButtonsPreview()
