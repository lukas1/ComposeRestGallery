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
import org.junit.Assert.assertEquals
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

    @ExperimentalTestApi
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

        // Screen is created, new page is loading, so loading view is visible
        screenTestRule
            .onRoot()
            .onChildAt(1)
            .assert(hasText(screenTestRule.activity.getString(R.string.loading)))

        // Simulates that images are loaded
        requireNotNull(continuation).resume(images)

        // No error should be displayed, loading is on bottom of list
        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertDoesNotExist()

        // images are expected to be displayed
        val lazyColumnNode = screenTestRule
            .onRoot()
            .onChildAt(1)

        lazyColumnNode.onChildAt(0).assert(hasContentDescription(images[0].description!!))
        lazyColumnNode.onChildAt(1).assert(hasText(images[0].description!!))
        lazyColumnNode.onChildAt(2).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, images[0].userName)
            )
        )
        lazyColumnNode.onChildAt(3).assert(hasContentDescription(images[1].description!!))
        lazyColumnNode.onChildAt(4).assert(hasText(images[1].description!!))
        lazyColumnNode.onChildAt(5).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, images[1].userName)
            )
        )

        // Must scroll to bottom when running on small device/emulator
        lazyColumnNode.performScrollToIndex(1)

        // There's no other image displayed
        lazyColumnNode.assert(
            hasAnyDescendant(
                hasText(screenTestRule.activity.getString(R.string.loading))
            )
        )
        lazyColumnNode.onChildAt(7).assertDoesNotExist()
        assertEquals(7, lazyColumnNode.fetchSemanticsNode().children.count())

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
            hasContentDescription(nextPageImages[0].description!!)
        )
        lazyColumnNode.onChildAt(7).assert(hasText(nextPageImages[0].description!!))
        lazyColumnNode.onChildAt(8).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, nextPageImages[0].userName)
            )
        )

        // Must scroll to bottom when running on small device/emulator
        lazyColumnNode.performScrollToIndex(4)
        lazyColumnNode.assert(
            hasAnyDescendant(
                hasContentDescription(nextPageImages[1].description!!)
            )
        )
        lazyColumnNode.assert(
            hasAnyDescendant(
                hasText(nextPageImages[1].description!!)
            )
        )
        lazyColumnNode.assert(
            hasAnyDescendant(
                hasText(
                    screenTestRule.activity.getString(R.string.by_author, nextPageImages[1].userName)
                )
            )
        )
    }

    @ExperimentalTestApi
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

        // Screen is created, new page is loading, so loading view is visible
        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertExists()

        // Simulates error when loading images
        requireNotNull(continuation).resumeWithException(IllegalStateException())

        // Loading should no longer be displayed
        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertDoesNotExist()

        // Error view should be visible
        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertExists()

        // Simulates click on Retry button that invokes new loading with new loading view
        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.retry)))
            .performClick()

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertExists()

        // Simulates successful response from server
        requireNotNull(continuation).resume(images)

        // Now error message should be displayed, only bottom loading should be displayed
        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertDoesNotExist()

        // Images are expected to be displayed
        val lazyColumnNode = screenTestRule
            .onRoot()
            .onChildAt(1)

        lazyColumnNode.onChildAt(0).assert(hasContentDescription(images[0].description!!))
        lazyColumnNode.onChildAt(1).assert(hasText(images[0].description!!))
        lazyColumnNode.onChildAt(2).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, images[0].userName)
            )
        )
        lazyColumnNode.onChildAt(3).assert(hasContentDescription(images[1].description!!))
        lazyColumnNode.onChildAt(4).assert(hasText(images[1].description!!))
        lazyColumnNode.onChildAt(5).assert(
            hasText(
                screenTestRule.activity.getString(R.string.by_author, images[1].userName)
            )
        )

        // Must scroll to bottom when running on small device/emulator
        lazyColumnNode.performScrollToIndex(1)

        // Bottom loading is displayed
        lazyColumnNode.onChildAt(6)
            .assert(hasText(screenTestRule.activity.getString(R.string.loading)))

        // There's no other image displayed
        lazyColumnNode.onChildAt(7).assertDoesNotExist()
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

        // Screen starts in list mode with button to switch to grid mode
        val toGridNode = screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_grid_description)
                )
            )

        // Button to switch to GRID mode should be displayed
        toGridNode.assertExists()

        // Button to switch to LIST mode is NOT displayed
        screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_list_description)
                )
            )
            .assertDoesNotExist()

        // Switch to GRID mode
        toGridNode.performClick()

        val toListNode = screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_list_description)
                )
            )

        // In GRID mode show button to switch to LIST mode
        toListNode.assertExists()

        // In GRID mode don't show button to LIST mode
        screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_grid_description)
                )
            )
            .assertDoesNotExist()

        // Switch to LIST mode again
        toGridNode.performClick()

        // In LIST mode sho button to GRID mode, hide button to LIST mode
        toGridNode.assertExists()
        screenTestRule
            .onNode(
                hasContentDescription(
                    screenTestRule.activity.getString(R.string.toggle_to_list_description)
                )
            )
            .assertDoesNotExist()
    }
}