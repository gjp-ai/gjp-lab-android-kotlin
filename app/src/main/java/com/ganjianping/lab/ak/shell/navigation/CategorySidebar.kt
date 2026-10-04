package com.ganjianping.lab.ak.shell.navigation

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

private enum class DashboardLayout {
    Compact,
    Medium,
    TabletLandscape
}

private data class Category(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val destination: NavigationCategory
)

private val categories = listOf(
    Category("Android UI", "Modern Android UI.", Icons.Outlined.Code, NavigationCategory.JetpackCompose),
    Category("HTTP Client", "Native and library networking.", Icons.Outlined.Http, NavigationCategory.HttpClient),
    Category("Security", "Screen-capture protection.", Icons.Outlined.Security, NavigationCategory.Security),
    Category("Integration", "External SDKs and services.", Icons.Outlined.Extension, NavigationCategory.Integration),
    Category("Others", "Platform and device details.", Icons.Outlined.Tune, NavigationCategory.Others)
)

@Composable
fun CategorySidebar(onCategorySelected: (NavigationCategory) -> Unit) {
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
private fun CompactDashboard(padding: PaddingValues, onCategorySelected: (NavigationCategory) -> Unit) {
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
            compactCards = true,
            onCategorySelected = onCategorySelected
        )
    }
}

@Composable
private fun MediumDashboard(padding: PaddingValues, onCategorySelected: (NavigationCategory) -> Unit) {
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
            onCategorySelected = onCategorySelected
        )
    }
}

@Composable
private fun TabletLandscapeDashboard(
    padding: PaddingValues,
    onCategorySelected: (NavigationCategory) -> Unit
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
    compactCards: Boolean = false,
    onCategorySelected: (NavigationCategory) -> Unit
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
                compact = compactCards
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
    compact: Boolean = false,
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
            StandardCategoryContent(category, Modifier.weight(1f), compact)
        }
    }
}

@Composable
private fun StandardCategoryContent(
    category: Category,
    modifier: Modifier,
    compact: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(18.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                category.icon,
                null,
                Modifier.size(if (compact) 20.dp else 24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                category.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (compact) 8.dp else 10.dp),
                style = if (compact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            category.description,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Clip
        )
    }
}

private val MediumWindowWidth = 600.dp
private val TabletLandscapeWindowWidth = 1100.dp

@Preview(name = "Phone", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun CategorySidebarPhonePreview() {
    GJPLabTheme { CategorySidebar(onCategorySelected = {}) }
}

@Preview(name = "Foldable", showBackground = true, widthDp = 673, heightDp = 841)
@Composable
private fun CategorySidebarFoldablePreview() {
    GJPLabTheme { CategorySidebar(onCategorySelected = {}) }
}

@Preview(name = "Tablet", showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun CategorySidebarTabletPreview() {
    GJPLabTheme { CategorySidebar(onCategorySelected = {}) }
}

@Preview(name = "Phone - dark", showBackground = true, widthDp = 360, heightDp = 640, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategorySidebarPhoneDarkPreview() {
    GJPLabTheme { CategorySidebar(onCategorySelected = {}) }
}
