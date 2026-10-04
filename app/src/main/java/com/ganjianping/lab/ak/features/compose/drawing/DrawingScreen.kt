package com.ganjianping.lab.ak.features.compose.drawing

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun DrawingScreen(reduceMotion: Boolean) {
    var points by rememberSaveable { mutableFloatStateOf(5f) }
    var innerRatio by rememberSaveable { mutableFloatStateOf(0.5f) }
    var iconSize by rememberSaveable { mutableFloatStateOf(32f) }
    // Each stroke is the list of points the finger passed through.
    val strokes = remember { mutableStateListOf<List<Offset>>() }
    var currentStroke by remember { mutableStateOf<List<Offset>>(emptyList()) }

    LabDemoPage(
        intro = "Shapes and brushes style any composable. Canvas draws with a DrawScope, and pointerInput turns " +
            "touches into drawing."
    ) {
        LabDemoSection(
            title = "Shapes and brushes",
            caption = "Built-in shapes clip and fill; Brush draws gradients; a PathEffect dashes a stroke."
        ) {
            val primary = MaterialTheme.colorScheme.primary
            val container = MaterialTheme.colorScheme.primaryContainer
            val tertiary = MaterialTheme.colorScheme.tertiary
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f).aspectRatio(1f).background(Brush.linearGradient(listOf(primary, container)), CircleShape))
                Box(Modifier.weight(1f).aspectRatio(1f).background(Brush.radialGradient(listOf(container, primary)), RoundedCornerShape(16.dp)))
                Box(Modifier.weight(1f).aspectRatio(1f).background(Brush.sweepGradient(listOf(primary, tertiary, primary)), CutCornerShape(16.dp)))
                Canvas(Modifier.weight(1f).aspectRatio(1f)) {
                    drawCircle(
                        color = primary,
                        radius = size.minDimension / 2 - 4.dp.toPx(),
                        style = Stroke(width = 3.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)))
                    )
                }
            }
        }

        LabDemoSection(
            title = "Custom Shape",
            caption = "StarShape builds its outline from a list of vertices. The inner radius animates, so the star morphs instead of jumping."
        ) {
            val animatedRatio by animateFloatAsState(
                innerRatio,
                animationSpec = if (reduceMotion) snap() else tween(300),
                label = "innerRatio"
            )
            Box(Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .size(150.dp)
                        .background(MaterialTheme.colorScheme.primary, StarShape(points.roundToInt(), animatedRatio))
                        .semantics { contentDescription = "Star with ${points.roundToInt()} points" }
                )
            }
            Text("Points: ${points.roundToInt()}", style = MaterialTheme.typography.labelLarge)
            Slider(value = points, onValueChange = { points = it }, valueRange = 3f..12f, steps = 8)
            Text("Inner radius: ${(innerRatio * 100).roundToInt()}%", style = MaterialTheme.typography.labelLarge)
            Slider(value = innerRatio, onValueChange = { innerRatio = it }, valueRange = 0.2f..0.9f)
        }

        LabDemoSection(
            title = "Canvas animation",
            caption = "An infinite transition moves the phase; Canvas redraws the sine wave every frame. It stops when Remove animations is on."
        ) {
            val phase = if (reduceMotion) {
                0f
            } else {
                val transition = rememberInfiniteTransition(label = "wave")
                val animated by transition.animateFloat(
                    initialValue = 0f,
                    targetValue = (2 * PI).toFloat(),
                    animationSpec = infiniteRepeatable(tween(2_000, easing = LinearEasing), RepeatMode.Restart),
                    label = "phase"
                )
                animated
            }
            val waveColor = MaterialTheme.colorScheme.primary
            Canvas(Modifier.fillMaxWidth().height(80.dp)) {
                val path = Path()
                val amplitude = size.height / 3
                val steps = 60
                for (step in 0..steps) {
                    val x = size.width * step / steps
                    val y = size.height / 2 + amplitude * sin(phase + step * (4 * PI / steps)).toFloat()
                    if (step == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(path, waveColor, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))
            }
        }

        LabDemoSection(
            title = "Touch drawing",
            caption = "pointerInput with detectDragGestures records each drag as a stroke; Canvas draws them all."
        ) {
            val inkColor = MaterialTheme.colorScheme.onSurface
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .semantics { contentDescription = "Drawing area" }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { start -> currentStroke = listOf(start) },
                            onDrag = { change, _ -> currentStroke = currentStroke + change.position },
                            onDragEnd = {
                                strokes += currentStroke
                                currentStroke = emptyList()
                            },
                            onDragCancel = { currentStroke = emptyList() }
                        )
                    }
            ) {
                (strokes + listOf(currentStroke)).forEach { stroke ->
                    if (stroke.size < 2) return@forEach
                    val path = Path().apply {
                        moveTo(stroke.first().x, stroke.first().y)
                        stroke.drop(1).forEach { lineTo(it.x, it.y) }
                    }
                    drawPath(path, inkColor, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                }
            }
            OutlinedButton(onClick = { strokes.clear() }, enabled = strokes.isNotEmpty()) { Text("Clear") }
        }

        LabDemoSection(
            title = "Vector icons",
            caption = "Material icons are vectors: they stay sharp at any size and take their colour from tint."
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.WbSunny, contentDescription = null, modifier = Modifier.size(iconSize.dp), tint = MaterialTheme.colorScheme.primary)
                Icon(Icons.Outlined.Cloud, contentDescription = null, modifier = Modifier.size(iconSize.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(iconSize.dp), tint = MaterialTheme.colorScheme.error)
            }
            Text("Size: ${iconSize.roundToInt()} dp", style = MaterialTheme.typography.labelLarge)
            Slider(value = iconSize, onValueChange = { iconSize = it }, valueRange = 16f..64f)
        }
    }
}

@Composable
private fun DrawingPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { DrawingScreen(reduceMotion = true) } }
}

@Preview(name = "Drawing & graphics - light", showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
private fun DrawingLightPreview() = DrawingPreview()

@Preview(name = "Drawing & graphics - dark", showBackground = true, widthDp = 360, heightDp = 1500, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DrawingDarkPreview() = DrawingPreview()
