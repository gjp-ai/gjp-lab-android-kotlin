package com.ganjianping.lab.ak.features.compose

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ganjianping.lab.ak.features.compose.accessibility.AccessibilityTestTags
import com.ganjianping.lab.ak.shell.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Opens the Jetpack Compose category from the sidebar and checks its topics. Written for a phone-width
 * window, where the panes are one stack and each pane has a back arrow.
 */
@RunWith(AndroidJUnit4::class)
class ComposeTopicsTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val topics = listOf(
        "Material 3",
        "Layouts",
        "Text & input",
        "Buttons & actions",
        "Selection",
        "Lists & grids",
        "Navigation",
        "Animation",
        "Drawing & graphics",
        "Accessibility & testing"
    )

    @Test
    fun everyComposeTopicOpens() {
        rule.onNodeWithText("Jetpack Compose").performClick()
        topics.forEach { topic ->
            openTopic(topic)
            // The pane title is the topic title; the catalogue is no longer shown on a phone.
            rule.onAllNodesWithText(topic).onFirst().assertExists("$topic did not open")
            rule.onNodeWithContentDescription("Back").performClick()
            rule.onNodeWithText("Jetpack Compose").assertExists()
        }
    }

    @Test
    fun accessibilityTapCountChanges() {
        rule.onNodeWithText("Jetpack Compose").performClick()
        openTopic("Accessibility & testing")
        rule.onNodeWithTag(AccessibilityTestTags.TapCount).performScrollTo().assertTextEquals("Tapped 0 times")
        rule.onNodeWithTag(AccessibilityTestTags.TapButton).performScrollTo().performClick()
        rule.onNodeWithTag(AccessibilityTestTags.TapCount).assertTextEquals("Tapped 1 time")
    }

    @Test
    fun navigationTopicPushesAndPopsLevels() {
        rule.onNodeWithText("Jetpack Compose").performClick()
        openTopic("Navigation")
        rule.onNodeWithText("Push level 1").performClick()
        rule.onNodeWithText("Push level 2").performClick()
        rule.onAllNodesWithText("Level 2").onFirst().assertExists()
        rule.onNodeWithText("Pop to root").performClick()
        rule.onNodeWithText("Push level 1").assertExists()
    }

    /** The catalogue is lazy: scroll the topic into view before tapping it. */
    private fun openTopic(title: String) {
        rule.onNode(hasScrollAction()).performScrollToNode(hasText(title))
        rule.onNodeWithText(title).performClick()
    }
}
