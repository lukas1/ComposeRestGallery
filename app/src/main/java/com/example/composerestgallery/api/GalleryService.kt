package com.example.composerestgallery.api

import com.example.composerestgallery.screens.gallery.viewmodel.GalleryImage
import retrofit2.http.GET
import retrofit2.http.Headers


interface GalleryService {
    @GET("photos")
    // ideally such secret keys would come from backend for max security
    @Headers("Authorization: Client-ID PROVIDE-YOUR-OWN")
    suspend fun getPhotos(): List<GalleryImage>
}