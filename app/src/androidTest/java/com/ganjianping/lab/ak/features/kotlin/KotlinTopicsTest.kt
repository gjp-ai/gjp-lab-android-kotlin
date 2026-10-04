package com.ganjianping.lab.ak.features.kotlin

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.ganjianping.lab.ak.common.codesample.CodeSampleTestTags
import com.ganjianping.lab.ak.shell.MainActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Opens the Kotlin category from the sidebar, checks its topics, and runs one sample. Written for a
 * phone-width window, where the panes are one stack and each pane has a back arrow.
 */
@RunWith(AndroidJUnit4::class)
class KotlinTopicsTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val topics = listOf(
        "Values & types",
        "Null safety",
        "Collections",
        "Functions & lambdas",
        "Classes, data & sealed",
        "Interfaces & generics",
        "Error handling",
        "Coroutines",
        "Extensions & scope functions",
        "Strings & regex"
    )

    @Test
    fun kotlinIsTheFirstCategory() {
        val kotlin = rule.onNodeWithText("Kotlin").fetchSemanticsNode().boundsInRoot
        val compose = rule.onNodeWithText("Jetpack Compose").fetchSemanticsNode().boundsInRoot
        assert(kotlin.top < compose.top) { "Kotlin is not above Jetpack Compose" }
    }

    @Test
    fun everyKotlinTopicOpens() {
        rule.onNodeWithText("Kotlin").performClick()
        topics.forEach { topic ->
            openTopic(topic)
            rule.onAllNodesWithText(topic).onFirst().assertExists("$topic did not open")
            rule.onNodeWithContentDescription("Back").performClick()
            rule.onNodeWithText("Kotlin").assertExists()
        }
    }

    @Test
    fun runShowsTheSampleOutput() {
        rule.onNodeWithText("Kotlin").performClick()
        openTopic("Values & types")
        rule.onAllNodesWithTag(CodeSampleTestTags.Output).assertCountEquals(0)
        rule.onAllNodesWithTag(CodeSampleTestTags.Run).onFirst().performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag(CodeSampleTestTags.Output).fetchSemanticsNodes().isNotEmpty()
        }
        rule.onAllNodesWithTag(CodeSampleTestTags.Output).onFirst().assertTextEquals("Kotlin 2.0\nKotlin 2.4")
    }

    /** The catalogue is lazy: scroll the topic into view before tapping it. */
    private fun openTopic(title: String) {
        rule.onNode(hasScrollAction()).performScrollToNode(hasText(title))
        rule.onNodeWithText(title).performClick()
    }
}
