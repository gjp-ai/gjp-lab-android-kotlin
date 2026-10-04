package com.ganjianping.lab.ak.shell

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.ViewSidebar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.features.compose.navigation.NavigationLevelScreen
import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpResponseScreen
import com.ganjianping.lab.ak.shell.navigation.CatalogPaneWidth
import com.ganjianping.lab.ak.shell.navigation.CategorySidebar
import com.ganjianping.lab.ak.shell.navigation.DetailRoute
import com.ganjianping.lab.ak.shell.navigation.FeatureCatalogScreen
import com.ganjianping.lab.ak.shell.navigation.FeatureRoute
import com.ganjianping.lab.ak.shell.navigation.NavigationCategory
import com.ganjianping.lab.ak.shell.navigation.NavigationMenu
import com.ganjianping.lab.ak.shell.navigation.NavigationPane
import com.ganjianping.lab.ak.shell.navigation.NavigationPlaceholder
import com.ganjianping.lab.ak.shell.navigation.PaneLayout
import com.ganjianping.lab.ak.shell.navigation.SidebarPaneWidth
import com.ganjianping.lab.ak.shell.navigation.paneLayout

/**
 * The app's navigation: categories, catalogue, and feature, driven by selection state. On wide windows
 * the panes sit side by side; on narrow ones they collapse into a stack with Back between levels.
 * [featureContent] draws the screen for a route; a feature pushes a [DetailRoute] (for example an
 * HTTP response) on top of itself through the second argument.
 */
@Composable
fun ContentView(
    featureContent: @Composable (route: FeatureRoute, onPush: (DetailRoute) -> Unit) -> Unit
) {
    var selectedCategoryId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedTopic by rememberSaveable { mutableStateOf<FeatureRoute?>(null) }
    val detailPath = remember { mutableStateListOf<DetailRoute>() }
    val selectedCategory = selectedCategoryId?.let(NavigationMenu::category)

    // Changing the topic returns the feature pane to its first screen.
    fun selectTopic(route: FeatureRoute?) {
        if (route == selectedTopic) return
        selectedTopic = route
        detailPath.clear()
    }

    // Changing the category clears the selected topic.
    fun selectCategory(category: NavigationCategory?) {
        if (category?.id == selectedCategoryId) return
        selectedCategoryId = category?.id
        selectTopic(null)
    }

    fun goBack() {
        when {
            detailPath.isNotEmpty() -> detailPath.removeAt(detailPath.lastIndex)
            selectedTopic != null -> selectTopic(null)
            else -> selectCategory(null)
        }
    }

    fun popToRoot() {
        detailPath.clear()
    }

    // With nothing selected, Back leaves the app as usual.
    BackHandler(enabled = selectedCategoryId != null) { goBack() }

    @Composable
    fun Sidebar(modifier: Modifier = Modifier) {
        NavigationPane(title = "GJP Lab", modifier = modifier) {
            CategorySidebar(NavigationMenu.categories, selectedCategoryId, onCategorySelected = ::selectCategory)
        }
    }

    @Composable
    fun Catalog(category: NavigationCategory, showBack: Boolean, modifier: Modifier = Modifier) {
        NavigationPane(title = category.title, modifier = modifier, onBack = if (showBack) ::goBack else null) {
            FeatureCatalogScreen(category, selectedTopic, onFeatureSelected = ::selectTopic)
        }
    }

    @Composable
    fun Feature(route: FeatureRoute, showBack: Boolean, modifier: Modifier = Modifier) {
        // The pushed screen is drawn on top, so the feature keeps its state (for example the request form).
        val onPush: (DetailRoute) -> Unit = { detail ->
            // Ignore a late push (for example a slow HTTP response) if the user has moved to another topic.
            if (selectedTopic == route) detailPath.add(detail)
        }
        Box(modifier) {
            NavigationPane(title = NavigationMenu.topic(route).title, onBack = if (showBack) ::goBack else null) {
                featureContent(route, onPush)
            }
            when (val detail = detailPath.lastOrNull()) {
                is DetailRoute.Response -> NavigationPane(title = "Response", onBack = ::goBack) {
                    HttpResponseScreen(detail.response)
                }
                is DetailRoute.NavigationLevel -> NavigationPane(title = "Level ${detail.level}", onBack = ::goBack) {
                    NavigationLevelScreen(
                        level = detail.level,
                        onPush = onPush,
                        onBack = ::goBack,
                        onPopToRoot = ::popToRoot
                    )
                }
                null -> Unit
            }
        }
    }

    @Composable
    fun FeatureOrPlaceholder(modifier: Modifier) {
        val topic = selectedTopic
        when {
            topic != null -> Feature(topic, showBack = false, modifier = modifier)
            selectedCategory != null -> NavigationPlaceholder("Choose a topic", Icons.AutoMirrored.Outlined.List, modifier)
            else -> NavigationPlaceholder("Choose a category", Icons.AutoMirrored.Outlined.ViewSidebar, modifier)
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val divider = @Composable { VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant) }
        when (paneLayout(maxWidth)) {
            PaneLayout.Three -> Row(Modifier.fillMaxSize()) {
                Sidebar(Modifier.width(SidebarPaneWidth))
                divider()
                if (selectedCategory != null) {
                    Catalog(selectedCategory, showBack = false, modifier = Modifier.width(CatalogPaneWidth))
                } else {
                    NavigationPlaceholder("Choose a category", Icons.AutoMirrored.Outlined.ViewSidebar, Modifier.width(CatalogPaneWidth))
                }
                divider()
                FeatureOrPlaceholder(Modifier.weight(1f))
            }

            PaneLayout.Two -> Row(Modifier.fillMaxSize()) {
                if (selectedCategory != null) {
                    Catalog(selectedCategory, showBack = true, modifier = Modifier.width(CatalogPaneWidth))
                } else {
                    Sidebar(Modifier.width(CatalogPaneWidth))
                }
                divider()
                FeatureOrPlaceholder(Modifier.weight(1f))
            }

            PaneLayout.Single -> {
                val topic = selectedTopic
                when {
                    topic != null -> Feature(topic, showBack = true, modifier = Modifier.fillMaxSize())
                    selectedCategory != null -> Catalog(selectedCategory, showBack = true)
                    else -> Sidebar()
                }
            }
        }
    }
}

@Composable
private fun PreviewFeature(route: FeatureRoute) {
    Text("${NavigationMenu.topic(route).title} screen", modifier = Modifier.padding(20.dp))
}

@Preview(name = "Phone - light", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun ContentViewPhonePreview() {
    GJPLabTheme { ContentView { route, _ -> PreviewFeature(route) } }
}

@Preview(name = "Phone - dark", showBackground = true, widthDp = 360, heightDp = 720, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ContentViewPhoneDarkPreview() {
    GJPLabTheme { ContentView { route, _ -> PreviewFeature(route) } }
}

@Preview(name = "Tablet - light", showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun ContentViewTabletPreview() {
    GJPLabTheme { ContentView { route, _ -> PreviewFeature(route) } }
}

@Preview(name = "Tablet - dark", showBackground = true, widthDp = 1280, heightDp = 800, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ContentViewTabletDarkPreview() {
    GJPLabTheme { ContentView { route, _ -> PreviewFeature(route) } }
}
