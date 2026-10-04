package com.ganjianping.lab.ak.shell.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabListCard

/** The first pane: every category as a card with its icon, title, and summary. */
@Composable
fun CategorySidebar(
    categories: List<NavigationCategory>,
    selectedCategoryId: String?,
    onCategorySelected: (NavigationCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories, key = { it.id }) { category ->
            LabListCard(
                isSelected = category.id == selectedCategoryId,
                onClick = { onCategorySelected(category) },
                onClickLabel = "Open the ${category.title} catalogue"
            ) {
                CategoryRow(category)
            }
        }
    }
}

@Composable
private fun CategoryRow(category: NavigationCategory) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            category.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            category.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            category.summary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Sidebar - light", showBackground = true, widthDp = 360)
@Composable
private fun CategorySidebarPreview() {
    GJPLabTheme {
        NavigationPane(title = "GJP Lab") {
            CategorySidebar(NavigationMenu.categories, selectedCategoryId = "httpClient", onCategorySelected = {})
        }
    }
}

@Preview(name = "Sidebar - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategorySidebarDarkPreview() {
    GJPLabTheme {
        NavigationPane(title = "GJP Lab") {
            CategorySidebar(NavigationMenu.categories, selectedCategoryId = "httpClient", onCategorySelected = {})
        }
    }
}
