
package com.ganjianping.lab.ak.navigation.catalog

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
import com.ganjianping.lab.ak.navigation.FeatureRoute
import com.ganjianping.lab.ak.navigation.catalog.model.CatalogItem
import com.ganjianping.lab.ak.navigation.catalog.model.DashboardCategory

@Composable
fun FeatureCatalogScreen(
    category: DashboardCategory,
    onBack: () -> Unit,
    onFeatureSelected: (FeatureRoute) -> Unit
) {
    val items = catalogItems(category)
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
                Text(categoryTitle(category), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(
                    categoryDescription(category),
                    modifier = Modifier.padding(top = 6.dp, bottom = 22.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
                CatalogTable(items, onFeatureSelected)
            }
        }
    }
}

@Composable
private fun CatalogTable(items: List<CatalogItem>, onFeatureSelected: (FeatureRoute) -> Unit) {
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
private fun CatalogTableRow(item: CatalogItem, onFeatureSelected: (FeatureRoute) -> Unit) {
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

private fun categoryTitle(category: DashboardCategory): String = when (category) {
    DashboardCategory.JetpackCompose -> "Jetpack Compose"
    DashboardCategory.HttpClient -> "HTTP client"
    DashboardCategory.Security -> "Security"
    DashboardCategory.Integration -> "Integration"
    DashboardCategory.Others -> "Others"
}

private fun categoryDescription(category: DashboardCategory): String = when (category) {
    DashboardCategory.JetpackCompose -> "A practical index of the Compose building blocks used in Android UI."
    DashboardCategory.HttpClient -> "Compare a native connection API with a popular HTTP client library."
    DashboardCategory.Security -> "Screen-capture detection topics for protecting sensitive content."
    DashboardCategory.Integration -> "External SDKs and services connected to the Android app."
    DashboardCategory.Others -> "Android platform information available on the current device."
}

private fun catalogItems(category: DashboardCategory): List<CatalogItem> = when (category) {
    DashboardCategory.JetpackCompose -> listOf(
        CatalogItem("Material 3", "Theme roles, surfaces, and component styling."),
        CatalogItem("Layouts", "Rows, columns, boxes, and responsive arrangements."),
        CatalogItem("Text & input", "Text, text fields, and user-input patterns."),
        CatalogItem("Buttons & actions", "Buttons, FABs, and touch targets."),
        CatalogItem("Selection", "Chips, switches, checkboxes, and radio buttons."),
        CatalogItem("Lists & grids", "Lazy lists and grids for collections."),
        CatalogItem("Navigation", "Navigation surfaces and destination patterns."),
        CatalogItem("Animation", "State-driven transitions and motion."),
        CatalogItem("Drawing & graphics", "Canvas, images, and custom visuals."),
        CatalogItem("Accessibility & testing", "Semantics, scaling, and UI tests.")
    )

    DashboardCategory.HttpClient -> listOf(
        CatalogItem("HttpsURLConnection", "Native HttpURLConnection request sample.", FeatureRoute.HttpURLConnection),
        CatalogItem("Retrofit", "Type-safe HTTP client integration.")
    )

    DashboardCategory.Security -> listOf(
        CatalogItem(
            "Block App During Calls",
            "Block access while Android reports a supported active call.",
            FeatureRoute.BlockAppDuringCalls
        ),
        CatalogItem("Screenshot Detection", "Detect screenshot events where supported."),
        CatalogItem("Screen Sharing Detection", "Detect active screen sharing where supported."),
        CatalogItem("Screen Recording Detection", "Detect active screen recording where supported.")
    )

    DashboardCategory.Integration -> listOf(
        CatalogItem("Firebase", "Analytics, Config, Crashlytics, Performance, and Messaging.", FeatureRoute.Firebase)
    )

    DashboardCategory.Others -> listOf(
        CatalogItem("OS & Hardware", "Inspect the Android OS and current device hardware.", FeatureRoute.DeviceInfo)
    )
}

@Preview(name = "HTTP client catalogue", showBackground = true, widthDp = 360)
@Composable
private fun HttpClientCatalogPreview() {
    GJPLabTheme {
        FeatureCatalogScreen(DashboardCategory.HttpClient, onBack = {}, onFeatureSelected = {})
    }
}

@Preview(name = "Compose catalogue - tablet", showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun ComposeCatalogTabletPreview() {
    GJPLabTheme {
        FeatureCatalogScreen(DashboardCategory.JetpackCompose, onBack = {}, onFeatureSelected = {})
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
        FeatureCatalogScreen(DashboardCategory.HttpClient, onBack = {}, onFeatureSelected = {})
    }
}
