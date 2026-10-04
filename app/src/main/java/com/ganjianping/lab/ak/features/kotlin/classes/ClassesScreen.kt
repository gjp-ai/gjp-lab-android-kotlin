package com.ganjianping.lab.ak.features.kotlin.classes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ganjianping.lab.ak.common.codesample.CodeSamplePage
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun ClassesScreen() {
    CodeSamplePage(
        intro = "Classes hold state and behaviour. Data classes, enums, and sealed types model values and fixed sets of cases.",
        samples = ClassesSamples.all
    )
}

@Composable
private fun ClassesScreenPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { ClassesScreen() } }
}

@Preview(name = "Classes, data & sealed - light", showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun ClassesScreenLightPreview() = ClassesScreenPreview()

@Preview(name = "Classes, data & sealed - dark", showBackground = true, widthDp = 360, heightDp = 1600, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ClassesScreenDarkPreview() = ClassesScreenPreview()
