package com.ganjianping.lab.ak.features.kotlin.nullsafety

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun NullSafetyScreen() {
    CodeSamplePage(
        intro = "Kotlin puts null in the type: String can never be null, String? can, and the compiler makes you handle the null case.",
        samples = NullSafetySamples.all
    )
}

@Composable
private fun NullSafetyScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { NullSafetyScreen() } }
}

@Preview(name = "Null safety - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun NullSafetyScreenLightPreview() = NullSafetyScreenPreview()

@Preview(name = "Null safety - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NullSafetyScreenDarkPreview() = NullSafetyScreenPreview()
