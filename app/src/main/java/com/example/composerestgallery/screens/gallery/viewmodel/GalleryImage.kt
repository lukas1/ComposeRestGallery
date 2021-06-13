package com.example.composerestgallery.screens.gallery.viewmodel

import com.example.composerestgallery.api.GalleryImageSerializer
import kotlinx.serialization.Serializable

@Serializable(with = GalleryImageSerializer::class)
data class GalleryImage(
    val url: String,
    val description: String?,
    val userName: String
)