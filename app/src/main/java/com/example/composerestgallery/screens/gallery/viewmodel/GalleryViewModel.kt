package com.example.composerestgallery.screens.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composerestgallery.api.GalleryService
import com.example.composerestgallery.shared.model.LoadingState
import com.example.composerestgallery.utils.nextState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val galleryService: GalleryService
) : ViewModel() {
    companion object {
        const val viewModeStateHandleKey = "viewMode"
    }

    private val _state = MutableStateFlow(GalleryState(images = LoadingState.Loaded(emptyList())))
    val state: StateFlow<GalleryState> = _state.asStateFlow()

    init {
        stateHandle.get<GalleryViewMode>(viewModeStateHandleKey)?.let { savedViewMode ->
            _state.nextState { copy(galleryViewMode = savedViewMode) }
        }
        refresh()
    }

    private suspend fun loadPage() = with(state.value) {
        // To avoid duplicate requests, allow only one request at a time
        if (images !is LoadingState.Loading && nextPageLoadingState !is LoadingState.Loading) {
            nextGalleryPageKey?.let { loadPage(it) }
        }
    }

    private suspend fun loadPage(page: Int) {
        val isFirstPage = page == 1
        try {
            _state.nextState {
                if (isFirstPage) {
                    copy(images = LoadingState.Loading)
                } else {
                    copy(nextPageLoadingState = LoadingState.Loading)
                }
            }

            val newImages = galleryService.getPhotos(page)
            _state.nextState {
                val currentImages = images.loadedValue ?: emptyList()
                return@nextState copy(
                    images = LoadingState.Loaded(currentImages + newImages),
                    nextGalleryPageKey = if (newImages.isEmpty()) null else page + 1,
                    nextPageLoadingState = LoadingState.Loaded(Unit)
                )
            }
        } catch (e: Exception) {
            _state.nextState {
                if (isFirstPage) {
                    copy(images = LoadingState.Error)
                } else {
                    copy(nextPageLoadingState = LoadingState.Error)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.nextState {
                copy(
                    nextGalleryPageKey = 1,
                    nextPageLoadingState = LoadingState.Loaded(Unit)
                )
            }

            loadPage()
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            // This method is called when scrolled to bottom, but if last item is error item
            // No request should be made until Retry button is pressed
            if (_state.value.nextPageLoadingState is LoadingState.Loaded) {
                loadPage()
            }
        }
    }

    fun retryLoadingPage() {
        viewModelScope.launch {
            loadPage()
        }
    }

    fun toggleViewMode() {
        val newMode = when (state.value.galleryViewMode) {
            GalleryViewMode.LIST -> GalleryViewMode.GRID
            GalleryViewMode.GRID -> GalleryViewMode.LIST
        }
        stateHandle.set(viewModeStateHandleKey, newMode)
        _state.nextState { copy(galleryViewMode = newMode) }
    }
}