package com.example.composerestgallery.screens.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.composerestgallery.TestCoroutineRule
import com.example.composerestgallery.api.GalleryService
import com.example.composerestgallery.shared.model.LoadingState
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GalleryViewModelTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

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

    private val savedStateHandle = SavedStateHandle()

    private fun viewModel(
        galleryService: GalleryService
    ): GalleryViewModel = GalleryViewModel(
        stateHandle = savedStateHandle,
        galleryService = galleryService
    )

    @Test
    fun loadingState() {
        var continuation: Continuation<List<GalleryImage>>? = null
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = suspendCancellableCoroutine {
                continuation = it
            }
        }

        val viewModel = viewModel(mockGalleryService)
        // View Model is created, so started to load data first, images is in Loading state
        assertEquals(
            GalleryState(images = LoadingState.Loading),
            viewModel.state.value
        )

        // Simulates images loaded. images should be passed to state,
        // index of next page to load should be increased
        requireNotNull(continuation).resume(images)
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(images),
                nextGalleryPageKey = 2
            ),
            viewModel.state.value
        )
    }

    @Test
    fun errorWithRetry() {
        var continuation: Continuation<List<GalleryImage>>? = null
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = suspendCancellableCoroutine {
                continuation = it
            }
        }

        val viewModel = viewModel(mockGalleryService)
        assertEquals(
            GalleryState(images = LoadingState.Loading),
            viewModel.state.value
        )

        // Simulates error loading images. Index of next page to load should not be changed
        requireNotNull(continuation).resumeWithException(IllegalStateException())
        assertEquals(
            GalleryState(
                images = LoadingState.Error,
                nextGalleryPageKey = 1
            ),
            viewModel.state.value
        )

        // When user wants to retry and it succeeds, proceed as if it succeeded the first time
        // Images are passed to state, index of next page to load is increased
        viewModel.refresh()
        requireNotNull(continuation).resume(images)
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(images),
                nextGalleryPageKey = 2
            ),
            viewModel.state.value
        )
    }

    @Test
    fun toggleViewMode() {
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = images
        }

        // Toggling view mode between GRID and LIST mode does work, but it doesn't affect
        // other state properties
        val viewModel = viewModel(mockGalleryService)
        assertEquals(
            LoadingState.Loaded(images),
            viewModel.state.value.images
        )
        assertEquals(
            GalleryViewMode.LIST,
            viewModel.state.value.galleryViewMode
        )

        viewModel.toggleViewMode()
        assertEquals(
            LoadingState.Loaded(images),
            viewModel.state.value.images
        )
        assertEquals(
            GalleryViewMode.GRID,
            viewModel.state.value.galleryViewMode
        )

        viewModel.toggleViewMode()
        assertEquals(
            LoadingState.Loaded(images),
            viewModel.state.value.images
        )
        assertEquals(
            GalleryViewMode.LIST,
            viewModel.state.value.galleryViewMode
        )
    }

    @Test
    fun viewModeFromStateHandleIsApplied() {
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = images
        }

        // When creating new instance of view model with restored state instance,
        // view mode is restored
        savedStateHandle.set(GalleryViewModel.viewModeStateHandleKey, GalleryViewMode.GRID)
        val viewModel = viewModel(mockGalleryService)
        assertEquals(
            LoadingState.Loaded(images),
            viewModel.state.value.images
        )
        assertEquals(
            GalleryViewMode.GRID,
            viewModel.state.value.galleryViewMode
        )
    }

    @Test
    fun loadingNextPage() {
        val firstPage = listOf(images.first())
        val secondPage = listOf(images.last())
        var loadedLastPageOnce = false
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = when (page) {
                1 -> firstPage
                2 -> secondPage
                else -> listOf<GalleryImage>().also {
                    if (!loadedLastPageOnce) {
                        loadedLastPageOnce = true
                    } else {
                        // To verify, that multiple calls to viewModel::loadNextPage on last page
                        // do not cause any additional loads from data service
                        throw IllegalStateException()
                    }
                }
            }
        }

        // First page is loaded automatically
        val viewModel = viewModel(mockGalleryService)
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage),
                nextGalleryPageKey = 2
            ),
            viewModel.state.value
        )

        // When next page is loaded, it is appended to current list of images,
        // index of next page to load is increased
        viewModel.loadNextPage()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage + secondPage),
                nextGalleryPageKey = 3
            ),
            viewModel.state.value
        )

        // When reaching last page (return list of images is empty), list of images is not changed.
        // Lack of next pages to load is represented by null
        assertEquals(false, loadedLastPageOnce)
        viewModel.loadNextPage()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage + secondPage),
                nextGalleryPageKey = null
            ),
            viewModel.state.value
        )
        assertEquals(true, loadedLastPageOnce)

        // No further call is made to load images, otherwise the mock would throw exception
        viewModel.loadNextPage()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage + secondPage),
                nextGalleryPageKey = null
            ),
            viewModel.state.value
        )
    }

    @Test
    fun refreshResetsPaging() {
        val firstPage = listOf(images.first())
        val secondPage = listOf(images.last())
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = when (page) {
                1 -> firstPage
                2 -> secondPage
                else -> listOf()
            }
        }

        val viewModel = viewModel(mockGalleryService)
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage),
                nextGalleryPageKey = 2
            ),
            viewModel.state.value
        )

        viewModel.loadNextPage()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage + secondPage),
                nextGalleryPageKey = 3
            ),
            viewModel.state.value
        )

        viewModel.loadNextPage()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage + secondPage),
                nextGalleryPageKey = null
            ),
            viewModel.state.value
        )

        viewModel.refresh()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage),
                nextGalleryPageKey = 2
            ),
            viewModel.state.value
        )
    }
}