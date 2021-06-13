package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryState
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel
import com.google.accompanist.coil.rememberCoilPainter

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
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberCoilPainter(
                            image.url,
                            fadeIn = true
                        ),
                        contentDescription = image.description,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.5f)
                    )
                    Text(text = image.description)
                }
            }
        }
    }
}