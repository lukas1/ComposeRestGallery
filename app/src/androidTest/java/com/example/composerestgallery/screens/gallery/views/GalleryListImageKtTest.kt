package com.example.composerestgallery.screens.gallery.views

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.composerestgallery.MainActivity
import com.example.composerestgallery.R
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryImage
import org.junit.Rule

import org.junit.Test

class GalleryListImageKtTest {
    @get:Rule
    val screenTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun imageWithDescription() {
        val image = GalleryImage(
            url = "",
            description = "description",
            userName = "user"
        )
        screenTestRule.setContent {
            GalleryListImage(image = image, height = 250f)
        }

        val root = screenTestRule.onRoot()

        root.onChildAt(0).assert(hasContentDescription(image.description!!))
        root.onChildAt(1).assert(hasText(image.description!!))
        root.onChildAt(2).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, image.userName)
            )
        )
    }

    @Test
    fun imageWithNullDescription() {
        val image = GalleryImage(
            url = "",
            description = null,
            userName = "user"
        )
        screenTestRule.setContent {
            GalleryListImage(image = image, height = 250f)
        }

        val noDescriptionText = screenTestRule.activity.getString(R.string.image_empty_description)
        val root = screenTestRule.onRoot()

        root.onChildAt(0).assert(hasContentDescription(noDescriptionText))
        root.onChildAt(1).assert(hasText(noDescriptionText))
        root.onChildAt(2).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, image.userName)
            )
        )
    }
}