package com.ganjianping.lab.ak.common.codesample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection

/** Test tags used by the Kotlin topic UI test. */
object CodeSampleTestTags {
    const val Run = "codeSample.run"
    const val Output = "codeSample.output"
}

/** A scrolling page of runnable samples for one Kotlin topic. */
@Composable
fun CodeSamplePage(intro: String, samples: List<CodeSample>) {
    LabDemoPage(intro = "$intro Samples call log(…) where a script would call println(…).") {
        samples.forEach { sample -> CodeSampleCard(sample) }
    }
}

/** One sample: explanation, code, a Run button, and the output of the last run. */
@Composable
fun CodeSampleCard(sample: CodeSample) {
    var output by remember { mutableStateOf<List<String>?>(null) }
    var runCount by rememberSaveable { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    // Tied to the card's lifetime: leaving the topic cancels a running sample, and a new run
    // (a new runCount) replaces the previous output.
    LaunchedEffect(runCount) {
        if (runCount == 0) return@LaunchedEffect
        isRunning = true
        output = sample.output()
        isRunning = false
    }

    LabDemoSection(title = sample.title, caption = sample.explanation) {
        // Code keeps its line breaks and scrolls sideways instead of wrapping.
        SelectionContainer(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
                .horizontalScroll(rememberScrollState())
        ) {
            Text(
                sample.code,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                softWrap = false
            )
        }

        Button(
            onClick = { runCount += 1 },
            enabled = !isRunning,
            modifier = Modifier.testTag(CodeSampleTestTags.Run)
        ) {
            if (isRunning) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Text("Running…")
                }
            } else {
                Text("Run")
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "Output",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            val lines = output
            if (lines != null) {
                SelectionContainer {
                    Text(
                        lines.joinToString("\n"),
                        // TalkBack reads the new output when a run finishes.
                        modifier = Modifier
                            .testTag(CodeSampleTestTags.Output)
                            .semantics { liveRegion = LiveRegionMode.Polite },
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            } else {
                Text(
                    "Tap Run to see the output",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private val previewSample = CodeSample(
    title = "Hello",
    explanation = "A sample logs lines instead of printing them.",
    code = """
        val name = "Kotlin"
        log("Hello, ${'$'}name!")
    """.trimIndent(),
    run = { log ->
        val name = "Kotlin"
        log("Hello, $name!")
    }
)

@Composable
private fun CodeSamplePreview() {
    GJPLabTheme {
        Column(Modifier.background(MaterialTheme.colorScheme.background)) {
            CodeSamplePage(intro = "A runnable sample.", samples = listOf(previewSample))
        }
    }
}

@Preview(name = "Code sample - light", showBackground = true, widthDp = 360)
@Composable
private fun CodeSampleLightPreview() = CodeSamplePreview()

@Preview(name = "Code sample - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CodeSampleDarkPreview() = CodeSamplePreview()
