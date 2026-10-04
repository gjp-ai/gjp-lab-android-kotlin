package com.ganjianping.lab.ak.shell.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabListCard

/**
 * The second pane: the category description, then one card per topic. Available topics end with a
 * chevron and can be selected; planned topics end with a clock and cannot.
 */
@Composable
fun FeatureCatalogScreen(
    category: NavigationCategory,
    selectedRoute: FeatureRoute?,
    onFeatureSelected: (FeatureRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // A plain first row, so the description scrolls with the cards instead of staying pinned.
        item {
            Text(
                category.description,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        items(category.topics, key = { it.title }) { topic ->
            val route = topic.route
            LabListCard(
                isSelected = route != null && route == selectedRoute,
                onClick = route?.let { { onFeatureSelected(it) } },
                onClickLabel = "Open"
            ) {
                CatalogRow(topic, modifier = Modifier.weight(1f))
                Icon(
                    if (route != null) Icons.AutoMirrored.Outlined.KeyboardArrowRight else Icons.Outlined.Schedule,
                    contentDescription = if (route != null) "Open" else "Planned",
                    modifier = Modifier.size(20.dp),
                    tint = if (route != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CatalogRow(topic: NavigationTopic, modifier: Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            topic.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            topic.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CatalogPreview(categoryId: String) {
    val category = NavigationMenu.category(categoryId)!!
    GJPLabTheme {
        NavigationPane(title = category.title, onBack = {}) {
            FeatureCatalogScreen(category, selectedRoute = null, onFeatureSelected = {})
        }
    }
}

@Preview(name = "HTTP client catalogue - light", showBackground = true, widthDp = 360)
@Composable
private fun HttpClientCatalogPreview() = CatalogPreview("httpClient")

@Preview(name = "HTTP client catalogue - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HttpClientCatalogDarkPreview() = CatalogPreview("httpClient")

@Preview(name = "Compose catalogue - light", showBackground = true, widthDp = 360)
@Composable
private fun ComposeCatalogPreview() = CatalogPreview("compose")

@Preview(name = "Compose catalogue - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ComposeCatalogDarkPreview() = CatalogPreview("compose")
