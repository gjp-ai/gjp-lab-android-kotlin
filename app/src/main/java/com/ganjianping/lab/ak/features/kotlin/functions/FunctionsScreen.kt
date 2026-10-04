package com.ganjianping.lab.ak.features.kotlin.functions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun FunctionsScreen() {
    CodeSamplePage(
        intro = "Functions are values in Kotlin: pass them, return them, and write them inline as lambdas.",
        samples = FunctionsSamples.all
    )
}

@Composable
private fun FunctionsScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { FunctionsScreen() } }
}

@Preview(name = "Functions & lambdas - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun FunctionsScreenLightPreview() = FunctionsScreenPreview()

@Preview(name = "Functions & lambdas - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FunctionsScreenDarkPreview() = FunctionsScreenPreview()
