package com.ganjianping.lab.ak.navigation.catalog.model

import com.ganjianping.lab.ak.navigation.FeatureRoute

data class CatalogItem(
    val title: String,
    val description: String,
    val route: FeatureRoute? = null
)
