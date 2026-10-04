package com.ganjianping.lab.ak.features.compose.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * A custom layout that places children left to right and wraps onto a new row when the next child
 * would not fit. The arithmetic lives in [arrangeFlow], a pure function the unit tests check.
 */
@Composable
fun FlowLayout(modifier: Modifier = Modifier, spacing: Dp = 8.dp, content: @Composable () -> Unit) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        // Children may be as narrow as they like but no wider than the row.
        val placeables = measurables.map { it.measure(Constraints(maxWidth = constraints.maxWidth)) }
        val result = arrangeFlow(
            sizes = placeables.map { IntSize(it.width, it.height) },
            maxWidth = constraints.maxWidth,
            spacing = spacing.roundToPx()
        )
        layout(
            width = result.size.width.coerceIn(constraints.minWidth, constraints.maxWidth),
            height = result.size.height.coerceIn(constraints.minHeight, constraints.maxHeight)
        ) {
            // placeRelative mirrors x in right-to-left languages, so tags flow from the right there.
            placeables.forEachIndexed { index, placeable -> placeable.placeRelative(result.origins[index]) }
        }
    }
}

/** Where each child goes, and the total size of the arrangement. */
data class FlowResult(val origins: List<IntOffset>, val size: IntSize)

/**
 * Places [sizes] in rows no wider than [maxWidth]. A row ends when the next item would pass the
 * width and the row is not empty, so an item wider than [maxWidth] sits alone on its own row.
 */
fun arrangeFlow(sizes: List<IntSize>, maxWidth: Int, spacing: Int): FlowResult {
    val origins = ArrayList<IntOffset>(sizes.size)
    var x = 0
    var y = 0
    var rowHeight = 0
    var widest = 0
    sizes.forEach { size ->
        if (x > 0 && x + size.width > maxWidth) {
            x = 0
            y += rowHeight + spacing
            rowHeight = 0
        }
        origins += IntOffset(x, y)
        widest = maxOf(widest, x + size.width)
        x += size.width + spacing
        rowHeight = maxOf(rowHeight, size.height)
    }
    return FlowResult(origins, IntSize(widest, if (sizes.isEmpty()) 0 else y + rowHeight))
}
