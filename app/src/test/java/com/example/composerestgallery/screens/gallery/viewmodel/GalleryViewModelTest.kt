package com.example.composerestgallery.screens.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.composerestgallery.TestCoroutineRule
import com.example.composerestgallery.screens.gallery.api.GalleryService
import com.example.composerestgallery.shared.model.LoadingState
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

        var nextPageResult: List<GalleryImage> = emptyList()
        var continuation: Continuation<List<GalleryImage>>? = null

        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = suspendCoroutine {
                nextPageResult = when (page) {
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
                continuation = it
            }
        }

        // Start with loading state
        val viewModel = viewModel(mockGalleryService)

        assertEquals(
            GalleryState(
                images = LoadingState.Loading,
                nextGalleryPageKey = 1,
                nextPageLoadingState = LoadingState.Loaded(Unit)
            ),
            viewModel.state.value
        )

        // Finish loading first page
        requireNotNull(continuation).resume(nextPageResult)
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage),
                nextGalleryPageKey = 2,
                nextPageLoadingState = LoadingState.Loaded(Unit)
            ),
            viewModel.state.value
        )

        // When next page loading is started, nextPageLoadingState is set to Loading
        viewModel.loadNextPage()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage),
                nextGalleryPageKey = 2,
                nextPageLoadingState = LoadingState.Loading
            ),
            viewModel.state.value
        )

        // Simulates loading page after delay
        requireNotNull(continuation).resume(nextPageResult)

        // When next page is loaded, it is appended to current list of images,
        // index of next page to load is increased
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
        requireNotNull(continuation).resume(nextPageResult)
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage + secondPage),
                nextGalleryPageKey = null,
                nextPageLoadingState = LoadingState.Loaded(Unit)
            ),
            viewModel.state.value
        )
        assertEquals(true, loadedLastPageOnce)

        // No further call is made to load images, otherwise the mock would throw exception
        viewModel.loadNextPage()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(firstPage + secondPage),
                nextGalleryPageKey = null,
                nextPageLoadingState = LoadingState.Loaded(Unit)
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

    @Test
    fun loadingNextPageCannotBeCalledIfPageIsBeingLoaded() {
        var continuation: Continuation<List<GalleryImage>>? = null
        var callCount = 0

        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = suspendCoroutine {
                callCount += 1
                continuation = it
            }
        }

        // Start with loading state
        assertEquals(0, callCount)
        val viewModel = viewModel(mockGalleryService)

        assertEquals(
            GalleryState(
                images = LoadingState.Loading,
                nextGalleryPageKey = 1,
                nextPageLoadingState = LoadingState.Loaded(Unit)
            ),
            viewModel.state.value
        )
        assertEquals(1, callCount)

        // If first page is still not loaded, next page can't be loaded yet
        // No call to GalleryService is therefore expected
        viewModel.loadNextPage()
        assertEquals(1, callCount)

        // Finish loading first page
        requireNotNull(continuation).resume(images)
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(images),
                nextGalleryPageKey = 2,
                nextPageLoadingState = LoadingState.Loaded(Unit)
            ),
            viewModel.state.value
        )
        assertEquals(1, callCount)

        // When next page loading is started, nextPageLoadingState is set to Loading
        viewModel.loadNextPage()
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(images),
                nextGalleryPageKey = 2,
                nextPageLoadingState = LoadingState.Loading
            ),
            viewModel.state.value
        )
        assertEquals(2, callCount)

        // If previous page is still not loaded, next page can't be loaded yet
        // No call to GalleryService is therefore expected
        viewModel.loadNextPage()
        assertEquals(2, callCount)

        // Simulates loading page after delay
        requireNotNull(continuation).resume(images)

        // When next page is loaded, it is appended to current list of images,
        // index of next page to load is increased
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(images + images),
                nextGalleryPageKey = 3
            ),
            viewModel.state.value
        )
    }

    @Test
    fun errorInPaging() {
        var callCount = 0
        var continuation: Continuation<List<GalleryImage>>? = null
        val mockGalleryService = object : GalleryService {
            override suspend fun getPhotos(
                page: Int?, perPage: Int
            ): List<GalleryImage> = suspendCancellableCoroutine {
                callCount += 1
                continuation = it
            }
        }

        assertEquals(0, callCount)
        val viewModel = viewModel(mockGalleryService)
        assertEquals(
            GalleryState(images = LoadingState.Loading),
            viewModel.state.value
        )

        // Loading first page is successful
        requireNotNull(continuation).resume(images)
        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(images),
                nextGalleryPageKey = 2,
                nextPageLoadingState = LoadingState.Loaded(Unit)
            ),
            viewModel.state.value
        )
        assertEquals(1, callCount)

        // Attempt loading next page
        viewModel.loadNextPage()
        assertEquals(2, callCount)

        // Simulates error loading images. Expected Error state for nextPageLoadingState
        requireNotNull(continuation).resumeWithException(IllegalStateException())

        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(images),
                nextGalleryPageKey = 2,
                nextPageLoadingState = LoadingState.Error
            ),
            viewModel.state.value
        )

        viewModel.loadNextPage()
        assertEquals(2, callCount)

        viewModel.retryLoadingPage()
        assertEquals(3, callCount)

        // Loading next page is successful
        requireNotNull(continuation).resume(images)

        assertEquals(
            GalleryState(
                images = LoadingState.Loaded(images + images),
                nextGalleryPageKey = 3,
                nextPageLoadingState = LoadingState.Loaded(Unit)
            ),
            viewModel.state.value
        )
    }
}