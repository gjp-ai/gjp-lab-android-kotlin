package com.ganjianping.lab.ak.features.compose.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection
import kotlin.math.roundToInt

@Composable
fun Material3Screen() {
    var padding by rememberSaveable { mutableFloatStateOf(12f) }

    LabDemoPage(
        intro = "Material 3 styles every component from the theme: colour roles, a type scale, and shapes. " +
            "Modifiers then wrap a composable, and their order decides the result."
    ) {
        LabDemoSection(
            title = "Colour roles",
            caption = "Each role comes with a matching on-colour for content. Use the pair, never a raw colour."
        ) {
            val colors = MaterialTheme.colorScheme
            RoleSwatch("primary", colors.primary, colors.onPrimary)
            RoleSwatch("primaryContainer", colors.primaryContainer, colors.onPrimaryContainer)
            RoleSwatch("surfaceContainer", colors.surfaceContainer, colors.onSurface)
            RoleSwatch("errorContainer", colors.errorContainer, colors.onErrorContainer)
        }

        LabDemoSection(
            title = "Type scale",
            caption = "Text styles come from MaterialTheme.typography, so sizes stay consistent and scale with the user's font size."
        ) {
            val type = MaterialTheme.typography
            TypeSample("headlineSmall", type.headlineSmall)
            TypeSample("titleMedium", type.titleMedium)
            TypeSample("bodyLarge", type.bodyLarge)
            TypeSample("labelMedium", type.labelMedium)
        }

        LabDemoSection(
            title = "Cards and shapes",
            caption = "Card, ElevatedCard, and OutlinedCard read their colours and corners from the theme."
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(Modifier.weight(1f)) { CardLabel("Card") }
                ElevatedCard(Modifier.weight(1f)) { CardLabel("Elevated") }
                OutlinedCard(Modifier.weight(1f)) { CardLabel("Outlined") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ShapeSample("small", MaterialTheme.shapes.small, Modifier.weight(1f))
                ShapeSample("medium", MaterialTheme.shapes.medium, Modifier.weight(1f))
                ShapeSample("large", MaterialTheme.shapes.large, Modifier.weight(1f))
            }
        }

        LabDemoSection(
            title = "Modifier order",
            caption = "Modifiers apply from the outside in. Background before padding fills the padding too; padding before background leaves the padding outside the fill (the reverse of SwiftUI)."
        ) {
            Text("Padding: ${padding.roundToInt()} dp", style = MaterialTheme.typography.labelLarge)
            Slider(value = padding, onValueChange = { padding = it }, valueRange = 0f..32f, steps = 31)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OrderSample("Background first", Modifier.weight(1f)) {
                    Text(
                        "Hello",
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(padding.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                OrderSample("Padding first", Modifier.weight(1f)) {
                    Text(
                        "Hello",
                        modifier = Modifier
                            .padding(padding.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleSwatch(name: String, container: Color, content: Color) {
    Surface(color = container, contentColor = content, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text(name, modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp), style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun TypeSample(name: String, style: TextStyle) {
    Text(name, style = style)
}

@Composable
private fun CardLabel(text: String) {
    Text(text, modifier = Modifier.padding(14.dp), style = MaterialTheme.typography.labelLarge)
}

@Composable
private fun ShapeSample(name: String, shape: Shape, modifier: Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, shape)
        )
        Text(name, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** A labelled frame with a thin border, so the sample's own size is visible. */
@Composable
private fun OrderSample(label: String, modifier: Modifier, sample: @Composable () -> Unit) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)) { sample() }
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Material3Preview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { Material3Screen() } }
}

@Preview(name = "Material 3 - light", showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
private fun Material3LightPreview() = Material3Preview()

@Preview(name = "Material 3 - dark", showBackground = true, widthDp = 360, heightDp = 1500, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Material3DarkPreview() = Material3Preview()
