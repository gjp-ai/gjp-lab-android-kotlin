@file:OptIn(ExperimentalMaterial3Api::class)

package com.ganjianping.lab.ak.features.compose.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection

/** The curves offered by the explicit-animation sample. */
enum class Curve(val title: String) { Linear("Linear"), Ease("Ease"), Spring("Spring"), Bouncy("Bouncy") }

/** The enter and exit transitions offered by the visibility sample. */
enum class CardTransition(val title: String) { Fade("Fade"), Slide("Slide"), Scale("Scale"), Expand("Expand") }

private val Tabs = listOf("Home", "Search", "Profile")

@Composable
fun AnimationScreen(reduceMotion: Boolean) {
    var isGrown by rememberSaveable { mutableStateOf(false) }
    var curve by rememberSaveable { mutableStateOf(Curve.Spring) }
    var isMoved by rememberSaveable { mutableStateOf(false) }
    var transition by rememberSaveable { mutableStateOf(CardTransition.Fade) }
    var isCardVisible by rememberSaveable { mutableStateOf(true) }
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    LabDemoPage(
        intro = "Compose animates by animating state. Change a value, and an animate*AsState, Animatable, or " +
            "AnimatedVisibility moves the UI there over time."
    ) {
        if (reduceMotion) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Remove animations is on, so changes below apply instantly and the heart stays still.",
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        LabDemoSection(
            title = "animate*AsState",
            caption = "The size and colour are targets. Change them and Compose animates from the current value, even mid-animation."
        ) {
            val size by animateDpAsState(
                if (isGrown) 120.dp else 56.dp,
                animationSpec = if (reduceMotion) snap() else spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "size"
            )
            val color by animateColorAsState(
                if (isGrown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                animationSpec = if (reduceMotion) snap() else tween(400),
                label = "color"
            )
            Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                Box(Modifier.size(size).background(color, CircleShape))
            }
            Button(onClick = { isGrown = !isGrown }) { Text(if (isGrown) "Shrink" else "Grow") }
        }

        LabDemoSection(
            title = "Animatable and curves",
            caption = "Animatable runs one animation at a time from a coroutine. The curve decides how the dot speeds up and slows down."
        ) {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                Curve.entries.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = curve == option,
                        onClick = { curve = option },
                        shape = SegmentedButtonDefaults.itemShape(index, Curve.entries.size)
                    ) { Text(option.title, maxLines = 1) }
                }
            }
            val progress = remember { Animatable(if (isMoved) 1f else 0f) }
            LaunchedEffect(isMoved) {
                progress.animateTo(if (isMoved) 1f else 0f, curve.spec(reduceMotion))
            }
            BoxWithConstraints(Modifier.fillMaxWidth().height(40.dp)) {
                val travel = maxWidth - 32.dp
                Box(
                    Modifier
                        .offset(x = travel * progress.value)
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .align(Alignment.CenterStart)
                )
            }
            OutlinedButton(onClick = { isMoved = !isMoved }) { Text("Move") }
        }

        LabDemoSection(
            title = "AnimatedVisibility",
            caption = "Enter and exit transitions play when content is added or removed. Choose one, then remove and add the card."
        ) {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                CardTransition.entries.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = transition == option,
                        onClick = { transition = option },
                        shape = SegmentedButtonDefaults.itemShape(index, CardTransition.entries.size)
                    ) { Text(option.title, maxLines = 1) }
                }
            }
            Box(Modifier.fillMaxWidth().height(72.dp)) {
                // The plain overload: inside a Column, the scoped one would be picked and is not allowed here.
                androidx.compose.animation.AnimatedVisibility(
                    visible = isCardVisible,
                    enter = if (reduceMotion) EnterTransition.None else transition.enter(),
                    exit = if (reduceMotion) ExitTransition.None else transition.exit()
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Hello from a card", modifier = Modifier.padding(24.dp))
                    }
                }
            }
            OutlinedButton(onClick = { isCardVisible = !isCardVisible }) { Text(if (isCardVisible) "Remove" else "Add") }
        }

        LabDemoSection(
            title = "Shared indicator",
            caption = "One indicator slides to the selected tab by animating its offset, instead of each tab drawing its own."
        ) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val tabWidth = maxWidth / Tabs.size
                val indicatorOffset by animateDpAsState(
                    tabWidth * selectedTab,
                    animationSpec = if (reduceMotion) snap() else spring(stiffness = Spring.StiffnessMediumLow),
                    label = "indicator"
                )
                Column {
                    Row {
                        Tabs.forEachIndexed { index, tab ->
                            Text(
                                tab,
                                modifier = Modifier
                                    .width(tabWidth)
                                    .clickable(role = Role.Tab) { selectedTab = index }
                                    .padding(vertical = 12.dp),
                                textAlign = TextAlign.Center,
                                color = if (selectedTab == index) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Box(
                        Modifier
                            .offset(x = indicatorOffset)
                            .width(tabWidth)
                            .height(3.dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                    )
                }
            }
        }

        LabDemoSection(
            title = "Infinite transition",
            caption = "rememberInfiniteTransition repeats forever while on screen. Decorative loops stop when Remove animations is on."
        ) {
            Box(Modifier.fillMaxWidth().height(64.dp), contentAlignment = Alignment.Center) {
                if (reduceMotion) {
                    Icon(Icons.Filled.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(40.dp))
                } else {
                    val pulse = rememberInfiniteTransition(label = "pulse")
                    val scale by pulse.animateFloat(
                        initialValue = 0.85f,
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
                        label = "scale"
                    )
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(40.dp).scale(scale)
                    )
                }
            }
        }
    }
}

private fun Curve.spec(reduceMotion: Boolean): AnimationSpec<Float> = when {
    reduceMotion -> snap()
    this == Curve.Linear -> tween(800, easing = LinearEasing)
    this == Curve.Ease -> tween(800, easing = FastOutSlowInEasing)
    this == Curve.Spring -> spring(stiffness = Spring.StiffnessLow)
    else -> spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow)
}

private fun CardTransition.enter(): EnterTransition = when (this) {
    CardTransition.Fade -> fadeIn()
    CardTransition.Slide -> slideInVertically { it } + fadeIn()
    CardTransition.Scale -> scaleIn() + fadeIn()
    CardTransition.Expand -> expandVertically() + fadeIn()
}

private fun CardTransition.exit(): ExitTransition = when (this) {
    CardTransition.Fade -> fadeOut()
    CardTransition.Slide -> slideOutVertically { it } + fadeOut()
    CardTransition.Scale -> scaleOut() + fadeOut()
    CardTransition.Expand -> shrinkVertically() + fadeOut()
}

@Composable
private fun AnimationPreview(reduceMotion: Boolean) {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { AnimationScreen(reduceMotion) } }
}

@Preview(name = "Animation - light", showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
private fun AnimationLightPreview() = AnimationPreview(reduceMotion = false)

@Preview(name = "Animation - dark", showBackground = true, widthDp = 360, heightDp = 1500, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AnimationDarkPreview() = AnimationPreview(reduceMotion = false)

@Preview(name = "Remove animations - light", showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
private fun AnimationReducedPreview() = AnimationPreview(reduceMotion = true)

@Preview(name = "Remove animations - dark", showBackground = true, widthDp = 360, heightDp = 1500, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AnimationReducedDarkPreview() = AnimationPreview(reduceMotion = true)
