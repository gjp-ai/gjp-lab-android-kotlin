package com.ganjianping.lab.ak.features.kotlin.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun ExtensionsScreen() {
    CodeSamplePage(
        intro = "Extensions add functions to existing types, and scope functions run a block in the context of an object.",
        samples = ExtensionsSamples.all
    )
}

@Composable
private fun ExtensionsScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { ExtensionsScreen() } }
}

@Preview(name = "Extensions & scope functions - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun ExtensionsScreenLightPreview() = ExtensionsScreenPreview()

@Preview(name = "Extensions & scope functions - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExtensionsScreenDarkPreview() = ExtensionsScreenPreview()
