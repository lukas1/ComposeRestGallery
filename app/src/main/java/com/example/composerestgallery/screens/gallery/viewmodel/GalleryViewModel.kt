package com.example.composerestgallery.screens.gallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composerestgallery.api.GalleryService
import com.example.composerestgallery.api.createRetrofit
import com.example.composerestgallery.shared.model.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val galleryService: GalleryService = createRetrofit().create(GalleryService::class.java)
) : ViewModel() {
    private val _state = MutableStateFlow(GalleryState(images = LoadingState.Loading))
    val state: StateFlow<GalleryState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.emit(GalleryState(images = LoadingState.Loading))

            _state.emit(
                GalleryState(
                    images = try {
                        LoadingState.Loaded(galleryService.getPhotos())
                    } catch (e: Exception) {
                        println("MY EXC:")
                        println(e)
                        LoadingState.Error
                    }
                )
            )

        }
    }
}