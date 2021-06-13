package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryState
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel

@Composable
fun GalleryScreen(viewModel: GalleryViewModel) {
    val state: GalleryState by viewModel.state.collectAsState()

    LazyColumn {
        items(state.images) { image ->
            Text(text = image.description)
        }
    }
}