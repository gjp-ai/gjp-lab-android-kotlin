
package com.ganjianping.lab.ak.shell.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun FeatureCatalogScreen(
    category: NavigationCategory,
    onBack: () -> Unit,
    onFeatureSelected: (FeatureRoute) -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 720.dp)
                    .align(Alignment.TopCenter)
                    .padding(bottom = 24.dp)
            ) {
                TextButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
                    Text("‹  Dashboard")
                }
                Text(category.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(
                    category.description,
                    modifier = Modifier.padding(top = 6.dp, bottom = 22.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
                CatalogTable(category.topics, onFeatureSelected)
            }
        }
    }
}

@Composable
private fun CatalogTable(items: List<NavigationTopic>, onFeatureSelected: (FeatureRoute) -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Component", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Text("Status", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            items.forEachIndexed { index, item ->
                CatalogTableRow(item, onFeatureSelected)
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 20.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CatalogTableRow(item: NavigationTopic, onFeatureSelected: (FeatureRoute) -> Unit) {
    val rowModifier = if (item.route == null) {
        Modifier
    } else {
        Modifier.clickable { onFeatureSelected(item.route) }
    }
    Row(
        modifier = rowModifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                item.description,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (item.route == null) {
            Icon(
                Icons.Outlined.Schedule,
                contentDescription = "Planned",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = "Open ${item.title}",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(name = "HTTP client catalogue", showBackground = true, widthDp = 360)
@Composable
private fun HttpClientCatalogPreview() {
    GJPLabTheme {
        FeatureCatalogScreen(NavigationMenu.category("httpClient")!!, onBack = {}, onFeatureSelected = {})
    }
}

@Preview(name = "Compose catalogue - tablet", showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun ComposeCatalogTabletPreview() {
    GJPLabTheme {
        FeatureCatalogScreen(NavigationMenu.category("compose")!!, onBack = {}, onFeatureSelected = {})
    }
}

@Preview(
    name = "HTTP client catalogue - dark",
    showBackground = true,
    widthDp = 360,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HttpClientCatalogDarkPreview() {
    GJPLabTheme {
        FeatureCatalogScreen(NavigationMenu.category("httpClient")!!, onBack = {}, onFeatureSelected = {})
    }
}
