package com.ganjianping.lab.ak.shell.navigation

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationPaneTest {
    @Test
    fun narrowWindowsUseOneStack() {
        assertEquals(PaneLayout.Single, paneLayout(360.dp))
        assertEquals(PaneLayout.Single, paneLayout(839.dp))
    }

    @Test
    fun expandedWindowsShowTwoPanes() {
        assertEquals(PaneLayout.Two, paneLayout(840.dp))
        assertEquals(PaneLayout.Two, paneLayout(1199.dp))
    }

    @Test
    fun largeWindowsShowThreePanes() {
        assertEquals(PaneLayout.Three, paneLayout(1200.dp))
        assertEquals(PaneLayout.Three, paneLayout(1600.dp))
    }

    @Test
    fun threePanesLeaveRoomForTheFeature() {
        // At the smallest three-pane width the feature pane is still at least as wide as the catalogue.
        assertEquals(true, 1200.dp - SidebarPaneWidth - CatalogPaneWidth >= CatalogPaneWidth)
    }
}
