package com.ganjianping.lab.ak.features.kotlin.strings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun StringsRegexScreen() {
    CodeSamplePage(
        intro = "Strings are immutable UTF-16 text. Templates, raw strings, and Regex cover most text work.",
        samples = StringsRegexSamples.all
    )
}

@Composable
private fun StringsRegexScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { StringsRegexScreen() } }
}

@Preview(name = "Strings & regex - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun StringsRegexScreenLightPreview() = StringsRegexScreenPreview()

@Preview(name = "Strings & regex - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StringsRegexScreenDarkPreview() = StringsRegexScreenPreview()
