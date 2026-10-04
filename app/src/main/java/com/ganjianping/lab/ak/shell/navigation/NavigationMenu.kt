package com.ganjianping.lab.ak.shell.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Http
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Dashboard categories and their catalogue topics, in display order. This is the only place menu
 * text lives; the unit tests check that every [FeatureRoute] appears here exactly once.
 */
object NavigationMenu {
    val categories: List<NavigationCategory> = listOf(
        NavigationCategory(
            id = "compose",
            title = "Jetpack Compose",
            summary = "Modern Android UI, built declaratively.",
            description = "A practical index of the Compose building blocks used in Android UI. Each topic is a page of live samples.",
            icon = Icons.Outlined.Code,
            topics = listOf(
                NavigationTopic("Material 3", "Color roles, type scale, cards, and modifier order.", FeatureRoute.Material3),
                NavigationTopic("Layouts", "Rows, columns, boxes, adaptive layouts, and a custom Layout.", FeatureRoute.Layouts),
                NavigationTopic("Text & input", "Styled text, text fields, focus, and validation.", FeatureRoute.TextInput),
                NavigationTopic("Buttons & actions", "Button styles, dialogs, menus, gestures, and touch targets.", FeatureRoute.ButtonsActions),
                NavigationTopic("Selection", "Segmented buttons, switches, sliders, chips, and dates.", FeatureRoute.Selection),
                NavigationTopic("Lists & grids", "Lazy lists and grids, search, swipe, and pull to refresh.", FeatureRoute.ListsGrids),
                NavigationTopic("Navigation", "Pushed screens, bottom sheets, dialogs, and panes.", FeatureRoute.NavigationPatterns),
                NavigationTopic("Animation", "State-driven animation, transitions, and motion settings.", FeatureRoute.Animation),
                NavigationTopic("Drawing & graphics", "Shapes, brushes, Canvas, and touch drawing.", FeatureRoute.Drawing),
                NavigationTopic("Accessibility & testing", "Font scale, semantics, TalkBack, and UI tests.", FeatureRoute.Accessibility)
            )
        ),
        NavigationCategory(
            id = "httpClient",
            title = "HTTP Client",
            summary = "Native and library networking.",
            description = "Compare a native connection API with a popular HTTP client library.",
            icon = Icons.Outlined.Http,
            topics = listOf(
                NavigationTopic("HttpURLConnection", "Native HttpURLConnection request sample.", FeatureRoute.HttpURLConnection),
                NavigationTopic("Retrofit", "Type-safe HTTP client integration.")
            )
        ),
        NavigationCategory(
            id = "security",
            title = "Security",
            summary = "Screen-capture protection.",
            description = "Screen-capture detection topics for protecting sensitive content.",
            icon = Icons.Outlined.Security,
            topics = listOf(
                NavigationTopic(
                    "Block App During Calls",
                    "Block access while Android reports a supported active call.",
                    FeatureRoute.BlockAppDuringCalls
                ),
                NavigationTopic("Screenshot Detection", "Detect screenshot events where supported."),
                NavigationTopic("Screen Sharing Detection", "Detect active screen sharing where supported."),
                NavigationTopic("Screen Recording Detection", "Detect active screen recording where supported.")
            )
        ),
        NavigationCategory(
            id = "integration",
            title = "Integration",
            summary = "External SDKs and services.",
            description = "External SDKs and services connected to the Android app.",
            icon = Icons.Outlined.Extension,
            topics = listOf(
                NavigationTopic("Firebase", "Analytics, Config, Crashlytics, Performance, and Messaging.", FeatureRoute.Firebase)
            )
        ),
        NavigationCategory(
            id = "others",
            title = "Others",
            summary = "Platform and device details.",
            description = "Android platform information available on the current device.",
            icon = Icons.Outlined.Tune,
            topics = listOf(
                NavigationTopic("OS & Hardware", "Inspect the Android OS and current device hardware.", FeatureRoute.DeviceInfo)
            )
        )
    )

    fun category(id: String): NavigationCategory? = categories.firstOrNull { it.id == id }

    fun topic(route: FeatureRoute): NavigationTopic =
        categories.flatMap { it.topics }.first { it.route == route }
}

/** A dashboard card and the catalogue it opens. */
data class NavigationCategory(
    val id: String,
    val title: String,
    /** Shown under the title on the dashboard card. */
    val summary: String,
    /** Shown above the topic list in the catalogue. */
    val description: String,
    val icon: ImageVector,
    val topics: List<NavigationTopic>
)

/** A catalogue row; a topic without a route is planned and cannot be opened. */
data class NavigationTopic(
    val title: String,
    val description: String,
    val route: FeatureRoute? = null
)
