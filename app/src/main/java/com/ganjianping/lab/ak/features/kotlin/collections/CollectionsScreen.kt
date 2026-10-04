package com.ganjianping.lab.ak.features.kotlin.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun CollectionsScreen() {
    CodeSamplePage(
        intro = "Lists, sets, and maps come in read-only and mutable forms, with higher-order functions to transform them without loops.",
        samples = CollectionsSamples.all
    )
}

@Composable
private fun CollectionsScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { CollectionsScreen() } }
}

@Preview(name = "Collections - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun CollectionsScreenLightPreview() = CollectionsScreenPreview()

@Preview(name = "Collections - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CollectionsScreenDarkPreview() = CollectionsScreenPreview()
