package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
                fadeIn = true
            ),
            contentDescription = image.description,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
        )
        Text(text = image.description)
        Text(text = LocalContext.current.getString(R.string.by_author, image.userName))
    }
}