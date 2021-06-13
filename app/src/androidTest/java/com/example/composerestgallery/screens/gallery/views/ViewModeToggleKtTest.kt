package com.example.composerestgallery.screens.gallery.views

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.composerestgallery.MainActivity
import com.example.composerestgallery.R
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewMode
import org.junit.Assert.assertEquals
import org.junit.Rule

import org.junit.Test

class ViewModeToggleKtTest {
    @get:Rule
    val screenTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun clickingPerformsAction() {
        var hasClicked = false
        screenTestRule.setContent {
            ViewModeToggle(currentViewMode = GalleryViewMode.LIST, onClick = { hasClicked = true })
        }

        assertEquals(false, hasClicked)

        screenTestRule
            .onRoot()
            .onChild()
            .performClick()

        assertEquals(true, hasClicked)
    }

    @Test
    fun listMode() {
        screenTestRule.setContent {
            ViewModeToggle(currentViewMode = GalleryViewMode.LIST, onClick = {})
        }

        screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_grid_description)
                )
            )
            .assertExists()
    }

    @Test
    fun gridMode() {
        screenTestRule.setContent {
            ViewModeToggle(currentViewMode = GalleryViewMode.GRID, onClick = {})
        }

        screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_list_description)
                )
            )
            .assertExists()
    }
}