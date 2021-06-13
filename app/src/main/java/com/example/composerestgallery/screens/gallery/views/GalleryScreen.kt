package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryState
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel
import com.example.composerestgallery.shared.model.LoadingState

@Composable
fun GalleryScreen(viewModel: GalleryViewModel) {
    val state: GalleryState by viewModel.state.collectAsState()

    LoadingView(
        loadingState = state.images,
        onRetry = { viewModel.retry() }
    ) { images ->
        LazyColumn {
            items(images) { image ->
                Text(text = image.description)
            }
        }
    }
}