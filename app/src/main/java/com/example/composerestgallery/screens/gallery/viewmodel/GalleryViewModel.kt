package com.example.composerestgallery.screens.gallery.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GalleryViewModel : ViewModel() {
    private val _state = MutableStateFlow(GalleryState(images = listOf()))
    val state: StateFlow<GalleryState> = _state.asStateFlow()

    init {
        _state.tryEmit(
            GalleryState(images = listOf(
                GalleryImage(
                    url = "test",
                    description = "description",
                    userName = "user"
                ),
                GalleryImage(
                    url = "test",
                    description = "description2",
                    userName = "user2"
                )
            ))
        )
    }
}