package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.example.composerestgallery.R
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryState
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewMode
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel

@ExperimentalFoundationApi
@Composable
fun GalleryScreen(viewModel: GalleryViewModel) {
    val state: GalleryState by viewModel.state.collectAsState()

    Column {
        TopAppBar(
            title = {
                Text(text = LocalContext.current.getString(R.string.app_name))
            },
            actions = {
                ViewModeToggle(
                    currentViewMode = state.galleryViewMode,
                    onClick = { viewModel.toggleViewMode() }
                )
            }
        )

        LoadingView(
            loadingState = state.images,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            onRetry = { viewModel.refresh() }
        ) { images ->
            when (state.galleryViewMode) {
                GalleryViewMode.LIST -> LazyColumn {
                    itemsIndexed(images) { index, image ->
                        if (index == images.lastIndex) {
                            viewModel.loadNextPage()
                        }
                        // Height is just random value so that each element
                        // has the same size
                        GalleryListImage(image = image, height = 250f)
                    }
                }
                GalleryViewMode.GRID -> LazyVerticalGrid(
                    cells = GridCells.Adaptive(Dp(150f)),
                ) {
                    itemsIndexed(images) { index, image ->
                        if (index == images.lastIndex) {
                            viewModel.loadNextPage()
                        }
                        // Height is just random value so that each element
                        // has the same size
                        GalleryListImage(image = image, height = 150f)
                    }
                }
            }
        }
    }
}