package com.ganjianping.lab.ak.features.kotlin.generics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun GenericsScreen() {
    CodeSamplePage(
        intro = "Interfaces describe what a type can do; generics let one piece of code work with many types, checked by the compiler.",
        samples = GenericsSamples.all
    )
}

@Composable
private fun GenericsScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { GenericsScreen() } }
}

@Preview(name = "Interfaces & generics - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun GenericsScreenLightPreview() = GenericsScreenPreview()

@Preview(name = "Interfaces & generics - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GenericsScreenDarkPreview() = GenericsScreenPreview()
