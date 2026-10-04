package com.ganjianping.lab.ak.shell.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class NavigationMenuTest {
    @Test
    fun categoriesHaveUniqueIdsAndTopics() {
        val categories = NavigationMenu.categories
        assertFalse(categories.isEmpty())
        assertEquals(categories.size, categories.map { it.id }.toSet().size)
        categories.forEach { category ->
            assertFalse("${category.id} has no topics", category.topics.isEmpty())
            assertEquals(
                "${category.id} repeats a topic title",
                category.topics.size,
                category.topics.map { it.title }.toSet().size
            )
        }
    }

    @Test
    fun everyFeatureRouteAppearsExactlyOnceInTheMenu() {
        val routes = NavigationMenu.categories.flatMap { it.topics }.mapNotNull { it.route }
        assertEquals(FeatureRoute.entries.size, routes.size)
        assertEquals(FeatureRoute.entries.toSet(), routes.toSet())
    }

    @Test
    fun categoryLookupUsesTheId() {
        val category = NavigationMenu.category("httpClient")
        assertNotNull(category)
        assertEquals("HTTP Client", category!!.title)
        assertEquals(null, NavigationMenu.category("missing"))
    }
}
