package com.ganjianping.lab.ak.features.kotlin.basics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun KotlinBasicsScreen() {
    CodeSamplePage(
        intro = "Kotlin is statically typed: every value has a type known at compile time, usually inferred from the value itself.",
        samples = BasicsSamples.all
    )
}

@Composable
private fun KotlinBasicsScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { KotlinBasicsScreen() } }
}

@Preview(name = "Values & types - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun KotlinBasicsScreenLightPreview() = KotlinBasicsScreenPreview()

@Preview(name = "Values & types - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun KotlinBasicsScreenDarkPreview() = KotlinBasicsScreenPreview()
