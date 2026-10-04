package com.ganjianping.lab.ak.features.others.deviceinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun DeviceInfoScreen(repository: DeviceInfoRepository) {
    val info = remember(repository) { repository.read() }
    DeviceInfoContent(info)
}

@Composable
private fun DeviceInfoContent(info: List<InfoRow>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Text(
            "A snapshot of the device this app is running on.",
            modifier = Modifier.padding(top = 4.dp, bottom = 22.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        InfoSection("Android OS", info.take(4))
        Spacer(Modifier.height(18.dp))
        InfoSection("Hardware", info.drop(4))
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun InfoSection(title: String, entries: List<InfoRow>) {
    Card {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
            Text(title, modifier = Modifier.padding(vertical = 10.dp), fontWeight = FontWeight.Bold)
            entries.forEachIndexed { index, entry ->
                if (index > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(entry.label, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(entry.value, modifier = Modifier.padding(start = 16.dp), textAlign = TextAlign.End, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

private val previewRows = listOf(
    InfoRow("Android version", "17"),
    InfoRow("SDK level", "37"),
    InfoRow("Codename", "REL"),
    InfoRow("Screen", "1080 × 2400"),
    InfoRow("Manufacturer", "Google"),
    InfoRow("Model", "Pixel 10"),
    InfoRow("CPU cores", "8"),
    InfoRow("Memory", "12288 MB")
)

@Preview(name = "OS & hardware - light", showBackground = true, widthDp = 360)
@Composable
private fun DeviceInfoPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { DeviceInfoContent(previewRows) } }
}

@Preview(name = "OS & hardware - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DeviceInfoDarkPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { DeviceInfoContent(previewRows) } }
}
