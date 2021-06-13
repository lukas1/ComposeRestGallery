package com.example.composerestgallery.api

import com.example.composerestgallery.UIConstants
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryImage
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface GalleryService {
    @GET("photos")
    // ideally such secret keys would come from backend for max security
    @Headers("Authorization: Client-ID PROVIDE-YOUR-OWN")
    suspend fun getPhotos(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int = UIConstants.pageSize
    ): List<GalleryImage>
}