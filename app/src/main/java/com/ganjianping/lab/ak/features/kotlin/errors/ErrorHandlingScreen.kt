package com.ganjianping.lab.ak.features.kotlin.errors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun ErrorHandlingScreen() {
    CodeSamplePage(
        intro = "Kotlin reports failures with exceptions. try is an expression, and Result carries a success or a failure as a value.",
        samples = ErrorHandlingSamples.all
    )
}

@Composable
private fun ErrorHandlingScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { ErrorHandlingScreen() } }
}

@Preview(name = "Error handling - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun ErrorHandlingScreenLightPreview() = ErrorHandlingScreenPreview()

@Preview(name = "Error handling - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ErrorHandlingScreenDarkPreview() = ErrorHandlingScreenPreview()
