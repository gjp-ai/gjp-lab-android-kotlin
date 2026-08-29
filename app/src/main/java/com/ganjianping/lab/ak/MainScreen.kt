package com.ganjianping.lab.ak

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Http
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

enum class FeatureAction {
    DeviceInfo,
    HttpURLConnection,
    Firebase
}

enum class DashboardCategory {
    JetpackCompose,
    HttpClient,
    Security,
    Integration,
    Others
}

private enum class DashboardLayout {
    Compact,
    Medium,
    TabletLandscape
}

private data class Category(
    val title: String,
    val description: String,
    val number: String,
    val icon: ImageVector,
    val destination: DashboardCategory
)

private val categories = listOf(
    Category("Android UI", "Explore the modern Android UI toolkit.", "01", Icons.Outlined.Code, DashboardCategory.JetpackCompose),
    Category("HTTP Client", "Compare native and library-based networking.", "02", Icons.Outlined.Http, DashboardCategory.HttpClient),
    Category("Security", "Learn screen-capture protection patterns.", "03", Icons.Outlined.Security, DashboardCategory.Security),
    Category("Integration", "Connect the app with external services.", "04", Icons.Outlined.Extension, DashboardCategory.Integration),
    Category("Others", "Inspect Android platform and device details.", "05", Icons.Outlined.Tune, DashboardCategory.Others)
)

@Composable
fun MainScreen(onCategorySelected: (DashboardCategory) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val layout = when {
            maxWidth < MediumWindowWidth -> DashboardLayout.Compact
            maxWidth < TabletLandscapeWindowWidth -> DashboardLayout.Medium
            else -> DashboardLayout.TabletLandscape
        }

        Scaffold { padding ->
            when (layout) {
                DashboardLayout.Compact -> CompactDashboard(padding, onCategorySelected)
                DashboardLayout.Medium -> MediumDashboard(padding, onCategorySelected)
                DashboardLayout.TabletLandscape -> TabletLandscapeDashboard(padding, onCategorySelected)
            }
        }
    }
}

@Composable
private fun CompactDashboard(padding: PaddingValues, onCategorySelected: (DashboardCategory) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        DashboardHeader()
        Spacer(Modifier.height(18.dp))
        CategoryGrid(
            columns = 2,
            modifier = Modifier.weight(1f),
            cardAspectRatio = 1.1f,
            onCategorySelected = onCategorySelected
        )
    }
}

@Composable
private fun MediumDashboard(padding: PaddingValues, onCategorySelected: (DashboardCategory) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        DashboardHeader()
        Spacer(Modifier.height(24.dp))
        CategoryGrid(
            columns = 3,
            modifier = Modifier.weight(1f),
            cardAspectRatio = 1.45f,
            denseCards = true,
            onCategorySelected = onCategorySelected
        )
    }
}

@Composable
private fun TabletLandscapeDashboard(
    padding: PaddingValues,
    onCategorySelected: (DashboardCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 40.dp, vertical = 32.dp)
    ) {
        DashboardHeader()
        Spacer(Modifier.height(24.dp))
        CategoryGrid(
            columns = 5,
            modifier = Modifier.weight(1f),
            cardAspectRatio = 1.45f,
            denseCards = true,
            descriptionMaxLines = 2,
            onCategorySelected = onCategorySelected
        )
    }
}

@Composable
private fun DashboardHeader() {
    Text("Android lab", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    Text(
        "Choose a category to explore focused Android samples.",
        modifier = Modifier.padding(top = 6.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun CategoryGrid(
    columns: Int,
    modifier: Modifier,
    cardAspectRatio: Float,
    denseCards: Boolean = false,
    descriptionMaxLines: Int = 3,
    onCategorySelected: (DashboardCategory) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(categories) { category ->
            CategoryCard(
                category = category,
                modifier = Modifier.aspectRatio(cardAspectRatio),
                dense = denseCards,
                descriptionMaxLines = descriptionMaxLines
            ) {
                onCategorySelected(category.destination)
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: Category,
    modifier: Modifier = Modifier,
    dense: Boolean = false,
    descriptionMaxLines: Int = 3,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            StandardCategoryContent(category, dense, descriptionMaxLines)
        }
    }
}

@Composable
private fun StandardCategoryContent(
    category: Category,
    dense: Boolean,
    descriptionMaxLines: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = if (dense) Arrangement.Top else Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(category.icon, null, Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
            Text(category.number, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
        }
        if (dense) {
            Spacer(Modifier.height(14.dp))
        }
        Column {
            Text(category.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(
                category.description,
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = descriptionMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private val MediumWindowWidth = 600.dp
private val TabletLandscapeWindowWidth = 1100.dp

@Preview(name = "Phone", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun MainScreenPhonePreview() {
    GJPLabTheme { MainScreen(onCategorySelected = {}) }
}

@Preview(name = "Foldable", showBackground = true, widthDp = 673, heightDp = 841)
@Composable
private fun MainScreenFoldablePreview() {
    GJPLabTheme { MainScreen(onCategorySelected = {}) }
}

@Preview(name = "Tablet", showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun MainScreenTabletPreview() {
    GJPLabTheme { MainScreen(onCategorySelected = {}) }
}

@Preview(name = "Phone - dark", showBackground = true, widthDp = 360, heightDp = 640, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPhoneDarkPreview() {
    GJPLabTheme { MainScreen(onCategorySelected = {}) }
}
