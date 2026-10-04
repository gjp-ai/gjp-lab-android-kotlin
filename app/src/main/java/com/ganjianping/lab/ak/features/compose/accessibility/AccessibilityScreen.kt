package com.ganjianping.lab.ak.features.compose.accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FlightTakeoff
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.accessibility.AccessibilityStatus
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection
import kotlin.math.roundToInt

/** Test tags used by `ComposeTopicsTest`. */
object AccessibilityTestTags {
    const val TapButton = "accessibility.tapButton"
    const val TapCount = "accessibility.tapCount"
}

/** At this font scale or larger, the font-scale row stacks vertically. */
const val LargeFontScale = 1.5f

/** "Tapped 0 times", "Tapped 1 time", "Tapped 2 times". */
fun tapCountLabel(count: Int): String = "Tapped $count ${if (count == 1) "time" else "times"}"

/** Clamps a rating to 1–5 stars. */
fun clampRating(value: Int): Int = value.coerceIn(1, 5)

@Composable
fun AccessibilityScreen(status: AccessibilityStatus) {
    var rating by rememberSaveable { mutableIntStateOf(3) }
    var taps by rememberSaveable { mutableIntStateOf(0) }
    val fontScale = LocalDensity.current.fontScale

    LabDemoPage(
        intro = "Semantics describe the UI to TalkBack, switch access, and tests. Most composables provide good " +
            "semantics already; these samples show when to add, merge, or replace them."
    ) {
        LabDemoSection(
            title = "Font scale",
            caption = "Text in sp follows the user's font size. At large scales, rows that would squeeze their text become columns."
        ) {
            val isLarge = fontScale >= LargeFontScale
            // An icon sized in sp-equivalent dp grows with the text next to it.
            val iconSize = (24 * fontScale).dp
            val icon = @Composable {
                Icon(Icons.Outlined.TextFields, contentDescription = null, modifier = Modifier.size(iconSize), tint = MaterialTheme.colorScheme.primary)
            }
            val label = @Composable { Text("Current font scale: ${"%.2f".format(fontScale)}×") }
            if (isLarge) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { icon(); label() }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) { icon(); label() }
            }
            Text(
                if (isLarge) "Large font: the row is stacked." else "Change Settings → Display → Font size to see the row stack.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LabDemoSection(
            title = "Merging and decorative icons",
            caption = "mergeDescendants reads the card as one item. The airplane is decorative, so its content description is null."
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth().semantics(mergeDescendants = true) {}
            ) {
                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.FlightTakeoff, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Column {
                        Text("SQ 321 to London", fontWeight = FontWeight.SemiBold)
                        Text("Departs 23:05 · Gate B4", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        LabDemoSection(
            title = "Adjustable rating",
            caption = "Five stars become one element with a value. TalkBack adjusts it with its own gestures or the Increase and Decrease actions."
        ) {
            Row(
                Modifier.clearAndSetSemantics {
                    contentDescription = "Rating"
                    stateDescription = "$rating of 5 stars"
                    progressBarRangeInfo = ProgressBarRangeInfo(rating.toFloat(), 1f..5f, steps = 3)
                    setProgress { target -> rating = clampRating(target.roundToInt()); true }
                    customActions = listOf(
                        CustomAccessibilityAction("Increase") { rating = clampRating(rating + 1); true },
                        CustomAccessibilityAction("Decrease") { rating = clampRating(rating - 1); true }
                    )
                },
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                (1..5).forEach { star ->
                    Icon(
                        if (star <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { rating = star }
                    )
                }
            }
        }

        LabDemoSection(
            title = "System settings",
            caption = "AccessibilitySettings reports these while the screen is open, so they update without reopening it."
        ) {
            SettingRow("TalkBack", if (status.isTalkBackOn) "On" else "Off")
            SettingRow("Remove animations", if (status.isRemoveAnimationsOn) "On" else "Off")
            SettingRow("Font scale", "${"%.2f".format(fontScale)}×")
        }

        LabDemoSection(
            title = "UI test hooks",
            caption = "testTag gives tests a stable handle that does not change with the visible text. ComposeTopicsTest taps this button."
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Button(onClick = { taps += 1 }, modifier = Modifier.testTag(AccessibilityTestTags.TapButton)) { Text("Tap me") }
                Text(tapCountLabel(taps), modifier = Modifier.testTag(AccessibilityTestTags.TapCount))
            }
        }
    }
}

@Composable
private fun SettingRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().semantics(mergeDescendants = true) {}) {
        Text(label, modifier = Modifier.weight(1f))
        Text(value, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun AccessibilityPreview() {
    GJPLabTheme {
        Column(Modifier.background(MaterialTheme.colorScheme.background)) {
            AccessibilityScreen(AccessibilityStatus(isTalkBackOn = false, isRemoveAnimationsOn = true))
        }
    }
}

@Preview(name = "Accessibility - light", showBackground = true, widthDp = 360, heightDp = 1400)
@Composable
private fun AccessibilityLightPreview() = AccessibilityPreview()

@Preview(name = "Accessibility - dark", showBackground = true, widthDp = 360, heightDp = 1400, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AccessibilityDarkPreview() = AccessibilityPreview()

@Preview(name = "Large font - light", showBackground = true, widthDp = 360, heightDp = 1600, fontScale = 2f)
@Composable
private fun AccessibilityLargeFontPreview() = AccessibilityPreview()

@Preview(name = "Large font - dark", showBackground = true, widthDp = 360, heightDp = 1600, fontScale = 2f, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AccessibilityLargeFontDarkPreview() = AccessibilityPreview()
