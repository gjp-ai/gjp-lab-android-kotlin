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
            summary = "Modern Android UI.",
            description = "A practical index of the Compose building blocks used in Android UI.",
            icon = Icons.Outlined.Code,
            topics = listOf(
                NavigationTopic("Material 3", "Theme roles, surfaces, and component styling."),
                NavigationTopic("Layouts", "Rows, columns, boxes, and responsive arrangements."),
                NavigationTopic("Text & input", "Text, text fields, and user-input patterns."),
                NavigationTopic("Buttons & actions", "Buttons, FABs, and touch targets."),
                NavigationTopic("Selection", "Chips, switches, checkboxes, and radio buttons."),
                NavigationTopic("Lists & grids", "Lazy lists and grids for collections."),
                NavigationTopic("Navigation", "Navigation surfaces and destination patterns."),
                NavigationTopic("Animation", "State-driven transitions and motion."),
                NavigationTopic("Drawing & graphics", "Canvas, images, and custom visuals."),
                NavigationTopic("Accessibility & testing", "Semantics, scaling, and UI tests.")
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
