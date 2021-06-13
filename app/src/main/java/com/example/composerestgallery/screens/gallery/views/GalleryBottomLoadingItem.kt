package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryState
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel

@Composable
fun GalleryBottomLoadingItem(
    viewModel: GalleryViewModel
) {
    val state: GalleryState by viewModel.state.collectAsState()

    LoadingView(
        loadingState = state.nextPageLoadingState,
        modifier = Modifier.fillMaxWidth(),
        onRetry = { viewModel.retryLoadingPage() }
    ) {
        // Intentionally empty success layout
    }
}