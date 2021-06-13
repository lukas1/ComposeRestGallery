package com.example.composerestgallery.screens.gallery.views

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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

class GalleryScreenTest {
    private val images = listOf(
        GalleryImage(
            url = "url1",
            description = "desc1",
            userName = "user1"
        ),
        GalleryImage(
            url = "url1",
            description = "desc1",
            userName = "user1"
        ),
    )

    @get:Rule
    val screenTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun imagesDisplayed() {
        var continuation: Continuation<List<GalleryImage>>? = null
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(): List<GalleryImage> = suspendCancellableCoroutine {
                continuation = it
            }
        }

        screenTestRule.setContent {
            GalleryScreen(viewModel = GalleryViewModel(mockGalleryService))
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
            .onChild()

        lazyColumnNode.onChildAt(0).assert(hasContentDescription(images[0].description))
        lazyColumnNode.onChildAt(1).assert(hasText(images[0].description))
        lazyColumnNode.onChildAt(2).assert(hasContentDescription(images[1].description))
        lazyColumnNode.onChildAt(3).assert(hasText(images[1].description))
        lazyColumnNode.onChildAt(4).assertDoesNotExist()
    }

    @Test
    fun errorWithRetry() {
        var continuation: Continuation<List<GalleryImage>>? = null
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(): List<GalleryImage> = suspendCancellableCoroutine {
                continuation = it
            }
        }

        screenTestRule.setContent {
            GalleryScreen(viewModel = GalleryViewModel(mockGalleryService))
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
            .onChild()

        lazyColumnNode.onChildAt(0).assert(hasContentDescription(images[0].description))
        lazyColumnNode.onChildAt(1).assert(hasText(images[0].description))
        lazyColumnNode.onChildAt(2).assert(hasContentDescription(images[1].description))
        lazyColumnNode.onChildAt(3).assert(hasText(images[1].description))
        lazyColumnNode.onChildAt(4).assertDoesNotExist()
    }
}