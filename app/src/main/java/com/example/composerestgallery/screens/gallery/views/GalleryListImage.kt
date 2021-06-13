package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.size.OriginalSize
import com.example.composerestgallery.R
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryImage
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun GalleryListImage(image: GalleryImage) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberCoilPainter(
                image.url,
                fadeIn = true,
                requestBuilder = {
                    // This needs to be more intelligent, size of placeholder should be
                    // calculated based on image size that would be parsed from API.
                    // Without it, content jumps around as images are loaded.
                    // For this demo this is left as is.
                    placeholder(R.drawable.ic_baseline_image_24)
                    size(OriginalSize)
                }
            ),
            contentDescription = image.description,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
        )
        Text(text = image.description)
        Text(text = LocalContext.current.getString(R.string.by_author, image.userName))
    }
}