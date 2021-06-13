package com.example.composerestgallery.networking

import com.example.composerestgallery.screens.gallery.api.GalleryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides fun providesRetrofit(): Retrofit = createRetrofit()

    @Provides fun providesGalleryService(retrofit: Retrofit): GalleryService =
        retrofit.create(GalleryService::class.java)
}