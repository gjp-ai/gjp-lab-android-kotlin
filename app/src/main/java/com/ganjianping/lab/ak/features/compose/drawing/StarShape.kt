package com.ganjianping.lab.ak.features.compose.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * A star with [points] tips. [innerRatio] is the inner radius as a fraction of the outer one, so 0.5
 * draws a classic star and values near 1 approach a polygon. Usable anywhere a [Shape] is, for example
 * `Modifier.background(color, StarShape(5, 0.5f))`.
 */
class StarShape(private val points: Int, private val innerRatio: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path()
        vertices(points, innerRatio, size).forEachIndexed { index, vertex ->
            if (index == 0) path.moveTo(vertex.x, vertex.y) else path.lineTo(vertex.x, vertex.y)
        }
        path.close()
        return Outline.Generic(path)
    }

    companion object {
        /**
         * The star's corners in drawing order, alternating outer tip and inner corner, starting at the
         * top centre and going clockwise. Fewer than two points draws nothing.
         */
        fun vertices(points: Int, innerRatio: Float, size: Size): List<Offset> {
            if (points < 2) return emptyList()
            val center = Offset(size.width / 2, size.height / 2)
            val outer = min(size.width, size.height) / 2
            val inner = outer * innerRatio
            val step = PI / points
            return List(points * 2) { index ->
                val radius = if (index % 2 == 0) outer else inner
                // Start at -90° (straight up) so the first tip is at the top.
                val angle = -PI / 2 + index * step
                Offset(center.x + (radius * cos(angle)).toFloat(), center.y + (radius * sin(angle)).toFloat())
            }
        }
    }
}
