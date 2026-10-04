package com.ganjianping.lab.ak.shell.navigation

/** A dashboard category and the catalogue it opens. */
enum class NavigationCategory {
    JetpackCompose,
    HttpClient,
    Security,
    Integration,
    Others
}

/** A catalogue row; a topic without a route is planned and cannot be opened. */
data class NavigationTopic(
    val title: String,
    val description: String,
    val route: FeatureRoute? = null
)
