package com.example.composerestgallery.screens.gallery.viewmodel

import com.example.composerestgallery.shared.model.LoadingState

data class GalleryState(
    val images: LoadingState<List<GalleryImage>>,
    val galleryViewMode: GalleryViewMode = GalleryViewMode.LIST,
    val nextGalleryPageKey: Int? = 1,
    val nextPageLoadingState: LoadingState<Unit> = LoadingState.Loaded(Unit)
)