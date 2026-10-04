package com.ganjianping.lab.ak.features.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.ganjianping.lab.ak.features.compose.accessibility.clampRating
import com.ganjianping.lab.ak.features.compose.accessibility.tapCountLabel
import com.ganjianping.lab.ak.features.compose.drawing.StarShape
import com.ganjianping.lab.ak.features.compose.layouts.arrangeFlow
import com.ganjianping.lab.ak.features.compose.lists.Produce
import com.ganjianping.lab.ak.features.compose.lists.matching
import com.ganjianping.lab.ak.features.compose.selection.CoffeeOrder
import com.ganjianping.lab.ak.features.compose.selection.CoffeeSize
import com.ganjianping.lab.ak.features.compose.selection.Extra
import com.ganjianping.lab.ak.features.compose.selection.Temperature
import com.ganjianping.lab.ak.features.compose.textinput.SignUpForm
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Checks the pure helpers behind the Jetpack Compose topics; the screens themselves are covered by ComposeTopicsTest. */
class ComposeFeatureTest {

    // Text & input

    @Test
    fun emptySignUpFormReportsEveryProblem() {
        val form = SignUpForm()
        assertEquals(3, form.problems.size)
        assertFalse(form.isValid)
    }

    @Test
    fun completeSignUpFormIsValid() {
        val form = SignUpForm(name = "Ada", email = "ada@example.com", password = "analytical")
        assertTrue(form.problems.isEmpty())
        assertTrue(form.isValid)
    }

    @Test
    fun invalidEmailsAreRejected() {
        listOf("", "ada", "ada@", "ada@example", "ada @example.com", "@example.com").forEach { email ->
            assertFalse("\"$email\" was accepted", SignUpForm("Ada", email, "analytical").isValid)
        }
    }

    @Test
    fun whitespaceNameAndShortPasswordAreRejected() {
        assertEquals(2, SignUpForm(name = "   ", email = "ada@example.com", password = "short").problems.size)
    }

    // Layouts

    @Test
    fun flowLayoutWrapsWhenARowIsFull() {
        val result = arrangeFlow(List(3) { IntSize(40, 20) }, maxWidth = 100, spacing = 10)
        // Two items fit in 100 (40 + 10 + 40); the third starts a new row.
        assertEquals(listOf(IntOffset(0, 0), IntOffset(50, 0), IntOffset(0, 30)), result.origins)
        assertEquals(IntSize(90, 50), result.size)
    }

    @Test
    fun flowLayoutPlacesAnOversizedItemOnItsOwnRow() {
        val result = arrangeFlow(listOf(IntSize(150, 20), IntSize(30, 20)), maxWidth = 100, spacing = 8)
        assertEquals(listOf(IntOffset(0, 0), IntOffset(0, 28)), result.origins)
        assertEquals(IntSize(150, 48), result.size)
    }

    @Test
    fun flowLayoutWithNoItemsHasZeroSize() {
        val result = arrangeFlow(emptyList(), maxWidth = 100, spacing = 8)
        assertTrue(result.origins.isEmpty())
        assertEquals(IntSize(0, 0), result.size)
    }

    // Selection

    @Test
    fun defaultOrderSummary() {
        assertEquals("Medium hot coffee, 2 shots, 50% sweet, with Cinnamon.", CoffeeOrder().summary)
    }

    @Test
    fun summaryCombinesEverySelection() {
        val order = CoffeeOrder(
            size = CoffeeSize.Large,
            temperature = Temperature.Iced,
            isDecaf = true,
            shots = 1,
            sweetness = 20,
            extras = emptySet()
        )
        assertEquals("Large iced decaf coffee, 1 shot, 20% sweet, no extras.", order.summary)
    }

    @Test
    fun extrasAreListedInDeclaredOrder() {
        val order = CoffeeOrder(extras = setOf(Extra.OatMilk, Extra.Vanilla, Extra.Caramel))
        assertTrue(order.summary.endsWith("with Vanilla, Caramel and Oat milk."))
    }

    // Lists & grids

    @Test
    fun sampleProduceHasTwentyUniqueIds() {
        val ids = Produce.samples.map { it.id }
        assertEquals(20, ids.size)
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun searchIgnoresCaseAndAccents() {
        assertEquals(listOf("Jalapeño"), Produce.samples.matching("JALAPENO").map { it.name })
        assertEquals(listOf("Banana", "Mango", "Orange", "Eggplant"), Produce.samples.matching("an").map { it.name })
    }

    @Test
    fun blankSearchMatchesEverythingAndUnknownMatchesNothing() {
        assertEquals(Produce.samples, Produce.samples.matching("  "))
        assertTrue(Produce.samples.matching("zzz").isEmpty())
    }

    // Drawing

    @Test
    fun starHasTwoVerticesPerPointStartingAtTheTop() {
        val vertices = StarShape.vertices(points = 5, innerRatio = 0.5f, size = Size(100f, 100f))
        assertEquals(10, vertices.size)
        assertEquals(50f, vertices[0].x, 0.001f)
        assertEquals(0f, vertices[0].y, 0.001f)
    }

    @Test
    fun starWithFewerThanTwoPointsIsEmpty() {
        assertTrue(StarShape.vertices(points = 1, innerRatio = 0.5f, size = Size(10f, 10f)).isEmpty())
    }

    // Accessibility & testing

    @Test
    fun tapCountLabelUsesSingularForOne() {
        assertEquals("Tapped 0 times", tapCountLabel(0))
        assertEquals("Tapped 1 time", tapCountLabel(1))
        assertEquals("Tapped 2 times", tapCountLabel(2))
    }

    @Test
    fun ratingStaysBetweenOneAndFive() {
        assertEquals(1, clampRating(0))
        assertEquals(3, clampRating(3))
        assertEquals(5, clampRating(6))
    }
}
