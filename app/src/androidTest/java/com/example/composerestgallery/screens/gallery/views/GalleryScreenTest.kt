package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import com.example.composerestgallery.MainActivity
import com.example.composerestgallery.R
import com.example.composerestgallery.api.GalleryService
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryImage
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@ExperimentalFoundationApi
class GalleryScreenTest {
    private val images = listOf(
        GalleryImage(
            url = "url1",
            description = "desc1",
            userName = "user1"
        ),
        GalleryImage(
            url = "url2",
            description = "desc2",
            userName = "user2"
        ),
    )

    @get:Rule
    val screenTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun imagesDisplayed() {
        var continuation: Continuation<List<GalleryImage>>? = null
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?,
                perPage: Int
            ): List<GalleryImage> = suspendCancellableCoroutine {
                continuation = it
            }
        }

        screenTestRule.setContent {
            GalleryScreen(viewModel = GalleryViewModel(SavedStateHandle(), mockGalleryService))
        }

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertExists()

        requireNotNull(continuation).resume(images)

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertDoesNotExist()

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertDoesNotExist()

        val lazyColumnNode = screenTestRule
            .onRoot()
            .onChildAt(1)

        lazyColumnNode.onChildAt(0).assert(hasContentDescription(images[0].description))
        lazyColumnNode.onChildAt(1).assert(hasText(images[0].description))
        lazyColumnNode.onChildAt(2).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, images[0].userName)
            )
        )
        lazyColumnNode.onChildAt(3).assert(hasContentDescription(images[1].description))
        lazyColumnNode.onChildAt(4).assert(hasText(images[1].description))
        lazyColumnNode.onChildAt(5).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, images[1].userName)
            )
        )
        lazyColumnNode.onChildAt(6).assertDoesNotExist()

        // Simulates next page load by scrolling to bottom
        val nextPageImages = listOf(
            GalleryImage(
                url = "url3",
                description = "desc3",
                userName = "user3"
            ),
            GalleryImage(
                url = "url4",
                description = "desc4",
                userName = "user4"
            ),
        )
        requireNotNull(continuation).resume(nextPageImages)

        // New two gallery images should be added to layout
        lazyColumnNode.onChildAt(6).assert(
            hasContentDescription(nextPageImages[0].description)
        )
        lazyColumnNode.onChildAt(7).assert(hasText(nextPageImages[0].description))
        lazyColumnNode.onChildAt(8).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, nextPageImages[0].userName)
            )
        )

        lazyColumnNode.onChildAt(9).assert(
            hasContentDescription(nextPageImages[1].description)
        )
        lazyColumnNode.onChildAt(10).assert(hasText(nextPageImages[1].description))
        lazyColumnNode.onChildAt(11).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, nextPageImages[1].userName)
            )
        )
    }

    @Test
    fun errorWithRetry() {
        var continuation: Continuation<List<GalleryImage>>? = null
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?,
                perPage: Int
            ): List<GalleryImage> = suspendCancellableCoroutine {
                continuation = it
            }
        }

        screenTestRule.setContent {
            GalleryScreen(viewModel = GalleryViewModel(SavedStateHandle(), mockGalleryService))
        }

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertExists()

        requireNotNull(continuation).resumeWithException(IllegalStateException())

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertDoesNotExist()

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertExists()

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.retry)))
            .performClick()

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertExists()

        requireNotNull(continuation).resume(images)

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertDoesNotExist()

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertDoesNotExist()

        val lazyColumnNode = screenTestRule
            .onRoot()
            .onChildAt(1)

        lazyColumnNode.onChildAt(0).assert(hasContentDescription(images[0].description))
        lazyColumnNode.onChildAt(1).assert(hasText(images[0].description))
        lazyColumnNode.onChildAt(2).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, images[0].userName)
            )
        )
        lazyColumnNode.onChildAt(3).assert(hasContentDescription(images[1].description))
        lazyColumnNode.onChildAt(4).assert(hasText(images[1].description))
        lazyColumnNode.onChildAt(5).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, images[1].userName)
            )
        )
        lazyColumnNode.onChildAt(6).assertDoesNotExist()
    }

    @Test
    fun listModeToggle() {
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?,
                perPage: Int
            ): List<GalleryImage> = suspendCancellableCoroutine {
                images
            }
        }

        screenTestRule.setContent {
            GalleryScreen(viewModel = GalleryViewModel(SavedStateHandle(), mockGalleryService))
        }

        val toGridNode = screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_grid_description)
                )
            )

        toGridNode.assertExists()
        screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_list_description)
                )
            )
            .assertDoesNotExist()

        toGridNode.performClick()

        val toListNode = screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_list_description)
                )
            )

        toListNode.assertExists()
        screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_grid_description)
                )
            )
            .assertDoesNotExist()

        toGridNode.performClick()

        toGridNode.assertExists()
        screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_list_description)
                )
            )
            .assertDoesNotExist()

        toGridNode.performClick()
    }
}