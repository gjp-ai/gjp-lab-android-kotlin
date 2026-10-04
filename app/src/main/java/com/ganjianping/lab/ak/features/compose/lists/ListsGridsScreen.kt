@file:OptIn(ExperimentalMaterial3Api::class)

package com.ganjianping.lab.ak.features.compose.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** How the items are shown. */
enum class ProduceLayout(val title: String) { List("List"), Grid("Grid") }

/** How long pull to refresh pretends to load, in milliseconds. */
const val RefreshDurationMillis = 1_000L

@Composable
fun ListsGridsScreen() {
    val items = remember { mutableStateListOf<Produce>().apply { addAll(Produce.samples) } }
    var query by rememberSaveable { mutableStateOf("") }
    var layout by rememberSaveable { mutableStateOf(ProduceLayout.List) }
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val visible = items.matching(query)

    fun toggleFavorite(item: Produce) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index >= 0) items[index] = item.copy(isFavorite = !item.isFavorite)
    }

    fun delete(item: Produce) {
        items.removeAll { it.id == item.id }
    }

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "LazyColumn and LazyVerticalGrid compose only the rows on screen. Search filters both; swipe a row to delete it, and pull down to restore everything.",
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search produce") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                singleLine = true
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                SingleChoiceSegmentedButtonRow(Modifier.weight(1f)) {
                    ProduceLayout.entries.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = layout == option,
                            onClick = { layout = option },
                            shape = SegmentedButtonDefaults.itemShape(index, ProduceLayout.entries.size)
                        ) { Text(option.title) }
                    }
                }
                if (layout == ProduceLayout.List) {
                    TextButton(onClick = { isEditing = !isEditing }) { Text(if (isEditing) "Done" else "Edit") }
                }
            }
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                scope.launch {
                    delay(RefreshDurationMillis)
                    items.clear()
                    items.addAll(Produce.samples)
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize().padding(top = 8.dp)
        ) {
            when {
                visible.isEmpty() -> EmptySearch(query)
                layout == ProduceLayout.List -> ProduceList(visible, isEditing, ::toggleFavorite, ::delete)
                else -> ProduceGrid(visible, ::toggleFavorite)
            }
        }
    }
}

@Composable
private fun ProduceList(
    items: List<Produce>,
    isEditing: Boolean,
    onToggleFavorite: (Produce) -> Unit,
    onDelete: (Produce) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProduceKind.entries.forEach { kind ->
            val section = items.filter { it.kind == kind }
            if (section.isEmpty()) return@forEach
            item(key = "header-$kind") {
                Text(
                    "${kind.title} (${section.size})",
                    modifier = Modifier
                        .padding(start = 4.dp, top = 8.dp)
                        .semantics { heading() },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(section, key = { it.id }) { item ->
                SwipeToDeleteRow(item, isEditing, onToggleFavorite, onDelete)
            }
        }
    }
}

/** A row that is deleted by swiping it from end to start, or with its delete button in edit mode. */
@Composable
private fun SwipeToDeleteRow(
    item: Produce,
    isEditing: Boolean,
    onToggleFavorite: (Produce) -> Unit,
    onDelete: (Produce) -> Unit
) {
    val state = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(
        state = state,
        enableDismissFromStartToEnd = false,
        onDismiss = { onDelete(item) },
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(14.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                    Icon(Icons.Outlined.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(start = 16.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(item.emoji, fontSize = 24.sp)
                Text(item.name, modifier = Modifier.weight(1f).padding(start = 14.dp))
                FavoriteButton(item, onToggleFavorite)
                if (isEditing) {
                    IconButton(onClick = { onDelete(item) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete ${item.name}", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProduceGrid(items: List<Produce>, onToggleFavorite: (Produce) -> Unit) {
    LazyVerticalGrid(
        // As many 104 dp columns as fit: three on a phone, more on a tablet.
        columns = GridCells.Adaptive(minSize = 104.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items, key = { it.id }) { item ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (item.isFavorite) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                contentColor = if (item.isFavorite) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable(onClickLabel = if (item.isFavorite) "Remove favourite" else "Add favourite") { onToggleFavorite(item) }
            ) {
                Column(
                    Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(item.emoji, fontSize = 32.sp)
                    Text(item.name, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
                    if (item.isFavorite) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favourite", modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteButton(item: Produce, onToggleFavorite: (Produce) -> Unit) {
    IconButton(onClick = { onToggleFavorite(item) }) {
        Icon(
            if (item.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (item.isFavorite) "Remove ${item.name} from favourites" else "Add ${item.name} to favourites",
            tint = if (item.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Shown when the search matches nothing. A scrolling column keeps pull to refresh working. */
@Composable
private fun EmptySearch(query: String) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(32.dp)) {
        item {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("No results", fontWeight = FontWeight.SemiBold)
                Text(
                    "Nothing matches “${query.trim()}”. Check the spelling or try another search.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ListsPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { ListsGridsScreen() } }
}

@Preview(name = "Lists & grids - light", showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun ListsLightPreview() = ListsPreview()

@Preview(name = "Lists & grids - dark", showBackground = true, widthDp = 360, heightDp = 760, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListsDarkPreview() = ListsPreview()
