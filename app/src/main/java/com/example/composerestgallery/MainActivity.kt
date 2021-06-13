package com.example.composerestgallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewModel
import com.example.composerestgallery.screens.gallery.views.GalleryScreen

class MainActivity : AppCompatActivity() {
    private val galleryViewModel by viewModels<GalleryViewModel>()
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GalleryScreen(galleryViewModel)
                }
            }
        }
    }
}