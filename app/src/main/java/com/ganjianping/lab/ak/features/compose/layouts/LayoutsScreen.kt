package com.ganjianping.lab.ak.features.compose.layouts

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection
import kotlin.math.roundToInt

/** The container the first demo arranges its three boxes in. */
enum class StackKind(val title: String) { Row("Row"), Column("Column"), Box("Box") }

private val tags = listOf("Compose", "Kotlin", "Material 3", "Layout", "Modifier", "State", "Coroutines", "Preview")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutsScreen() {
    var stack by rememberSaveable { mutableStateOf(StackKind.Row) }
    var spacing by rememberSaveable { mutableFloatStateOf(12f) }
    var fitWidth by rememberSaveable { mutableFloatStateOf(320f) }

    LabDemoPage(
        intro = "Compose measures each child once, then places it. Row, Column, and Box cover most screens; " +
            "BoxWithConstraints adapts to the space available, and Layout builds your own container."
    ) {
        LabDemoSection(
            title = "Row, Column, and Box",
            caption = "The same three children in three containers. Positions animate because each box keeps its own offset."
        ) {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                StackKind.entries.forEachIndexed { index, kind ->
                    SegmentedButton(
                        selected = stack == kind,
                        onClick = { stack = kind },
                        shape = SegmentedButtonDefaults.itemShape(index, StackKind.entries.size)
                    ) { Text(kind.title) }
                }
            }
            Text(
                if (stack == StackKind.Box) "Spacing: not used by Box" else "Spacing: ${spacing.roundToInt()} dp",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                value = spacing,
                onValueChange = { spacing = it },
                valueRange = 0f..40f,
                enabled = stack != StackKind.Box
            )
            StackDemo(stack, spacing.dp)
        }

        LabDemoSection(
            title = "Weight and alignment",
            caption = "weight shares the leftover width; Alignment places each child on the cross axis."
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LabelBox("1×", Modifier.weight(1f).align(Alignment.Top))
                LabelBox("2×", Modifier.weight(2f).align(Alignment.CenterVertically))
                LabelBox("1×", Modifier.weight(1f).align(Alignment.Bottom))
            }
        }

        LabDemoSection(
            title = "BoxWithConstraints",
            caption = "Reads the width it is given and chooses a row or a column. Narrow the box to see it switch."
        ) {
            Text("Box width: ${fitWidth.roundToInt()} dp", style = MaterialTheme.typography.labelLarge)
            Slider(value = fitWidth, onValueChange = { fitWidth = it }, valueRange = 140f..320f)
            BoxWithConstraints(
                Modifier
                    .width(fitWidth.dp)
                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                val actions: @Composable () -> Unit = {
                    OutlinedButton(onClick = {}) { Text("Copy") }
                    OutlinedButton(onClick = {}) { Text("Share") }
                    OutlinedButton(onClick = {}) { Text("Save") }
                }
                if (maxWidth >= 280.dp) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { actions() }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) { actions() }
                }
            }
        }

        LabDemoSection(
            title = "Custom Layout",
            caption = "FlowLayout measures each tag and wraps to a new row when the next one would not fit."
        ) {
            FlowLayout(spacing = 8.dp) {
                tags.forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Text(tag, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

/**
 * Three numbered boxes whose offsets are computed for the chosen container. Animating each offset,
 * rather than swapping Row for Column, keeps the boxes in place so they move instead of reappearing.
 */
@Composable
private fun StackDemo(stack: StackKind, spacing: Dp) {
    val sizes = listOf(64.dp, 48.dp, 32.dp)
    Box(Modifier.fillMaxWidth().height(64.dp * 3 + 40.dp * 2)) {
        sizes.forEachIndexed { index, size ->
            val before = sizes.take(index).fold(0.dp) { total, s -> total + s + spacing }
            val x by animateDpAsState(if (stack == StackKind.Row) before else 0.dp, label = "x$index")
            val y by animateDpAsState(if (stack == StackKind.Column) before else 0.dp, label = "y$index")
            LabelBox(
                "${index + 1}",
                Modifier
                    .offset(x, y)
                    .size(size)
            )
        }
    }
}

@Composable
private fun LabelBox(label: String, modifier: Modifier) {
    Box(
        modifier
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            .border(1.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun LayoutsPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { LayoutsScreen() } }
}

@Preview(name = "Layouts - light", showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
private fun LayoutsLightPreview() = LayoutsPreview()

@Preview(name = "Layouts - dark", showBackground = true, widthDp = 360, heightDp = 1500, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LayoutsDarkPreview() = LayoutsPreview()
