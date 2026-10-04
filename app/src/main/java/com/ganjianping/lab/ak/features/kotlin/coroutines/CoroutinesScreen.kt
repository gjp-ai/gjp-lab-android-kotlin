package com.ganjianping.lab.ak.features.kotlin.coroutines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun CoroutinesScreen() {
    CodeSamplePage(
        intro = "Coroutines run asynchronous code that reads top to bottom. suspend functions pause without blocking a thread.",
        samples = CoroutinesSamples.all
    )
}

@Composable
private fun CoroutinesScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { CoroutinesScreen() } }
}

@Preview(name = "Coroutines - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun CoroutinesScreenLightPreview() = CoroutinesScreenPreview()

@Preview(name = "Coroutines - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CoroutinesScreenDarkPreview() = CoroutinesScreenPreview()
