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

    fun refresh() {
        viewModelScope.launch {
            _state.nextState { copy(images = LoadingState.Loading) }

            _state.nextState {
                copy(
                    images = try {
                        LoadingState.Loaded(galleryService.getPhotos())
                    } catch (e: Exception) {
                        LoadingState.Error
                    }
                )
            }
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