package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryState
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel

@Composable
fun GalleryScreen(viewModel: GalleryViewModel) {
    val state: GalleryState by viewModel.state.collectAsState()

    LoadingView(
        loadingState = state.images,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        onRetry = { viewModel.refresh() }
    ) { images ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(images) { image ->
                GalleryListImage(image = image)
            }
        }
    }
}