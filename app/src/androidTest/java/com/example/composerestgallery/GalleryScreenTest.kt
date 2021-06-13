package com.example.composerestgallery

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel
import com.example.composerestgallery.screens.gallery.views.GalleryScreen
import org.junit.Rule
import org.junit.Test

class GalleryScreenTest {
    @get:Rule
    val screenTestRule = createComposeRule()

    @Test
    fun imagesDisplayed() {
        screenTestRule.setContent {
            GalleryScreen(viewModel = GalleryViewModel())
        }
        val lazyColumnNode = screenTestRule
            .onRoot()
            .onChild()

        lazyColumnNode.onChildAt(0).assert(hasText("description"))
        lazyColumnNode.onChildAt(1).assert(hasText("description2"))
        lazyColumnNode.onChildAt(2).assertDoesNotExist()
    }
}