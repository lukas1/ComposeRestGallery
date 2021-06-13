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
    private val _state = MutableStateFlow(GalleryState(images = LoadingState.Loading))
    val state: StateFlow<GalleryState> = _state.asStateFlow()

    init {
        stateHandle.get<GalleryViewMode>(viewModeStateHandleKey)?.let { savedViewMode ->
            _state.nextState { copy(galleryViewMode = savedViewMode) }
        }
        refresh()
    }

    private suspend fun loadPage() {
        _state.value.nextGalleryPageKey?.let { loadPage(it) }
    }

    private suspend fun loadPage(page: Int) {
        try {
            val newImages = galleryService.getPhotos(page)
            _state.nextState {
                copy(
                    images = LoadingState.Loaded((images.loadedValue ?: emptyList()) + newImages),
                    nextGalleryPageKey = if (newImages.isEmpty()) null else page + 1
                )
            }
        } catch (e: Exception) {
            _state.nextState { copy(images = LoadingState.Error) }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.nextState { copy(images = LoadingState.Loading, nextGalleryPageKey = 1) }

            loadPage()
        }
    }

    fun loadNextPage() {
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