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
        assertEquals(
            GalleryState(images = LoadingState.Loading),
            viewModel.state.value
        )

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

        requireNotNull(continuation).resumeWithException(IllegalStateException())
        assertEquals(
            GalleryState(
                images = LoadingState.Error,
                nextGalleryPageKey = 1
            ),
            viewModel.state.value
        )

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