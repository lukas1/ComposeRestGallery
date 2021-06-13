package com.example.composerestgallery.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

private const val API_URL = "https://api.unsplash.com/"

fun createRetrofit(
    baseUrl: String = API_URL
): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory(MediaType.parse("application/json")!!))
        .build()