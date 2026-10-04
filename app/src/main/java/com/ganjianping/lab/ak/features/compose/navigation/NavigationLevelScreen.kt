package com.ganjianping.lab.ak.features.compose.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.shell.navigation.DetailRoute

/** One pushed level of the Navigation topic. `ContentView` draws it in its own pane over the topic. */
@Composable
fun NavigationLevelScreen(
    level: Int,
    onPush: (DetailRoute) -> Unit,
    onBack: () -> Unit,
    onPopToRoot: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Level $level", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.SemiBold)
        Text(
            "This screen is DetailRoute.NavigationLevel($level) on top of the feature pane's stack.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = { onPush(DetailRoute.NavigationLevel(level + 1)) }, modifier = Modifier.fillMaxWidth()) {
            Text("Push level ${level + 1}")
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back one level") }
        TextButton(onClick = onPopToRoot, modifier = Modifier.fillMaxWidth()) { Text("Pop to root") }
    }
}

@Composable
private fun LevelPreview() {
    GJPLabTheme {
        Column(Modifier.background(MaterialTheme.colorScheme.background)) {
            NavigationLevelScreen(level = 2, onPush = {}, onBack = {}, onPopToRoot = {})
        }
    }
}

@Preview(name = "Navigation level - light", showBackground = true, widthDp = 360)
@Composable
private fun LevelLightPreview() = LevelPreview()

@Preview(name = "Navigation level - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LevelDarkPreview() = LevelPreview()
