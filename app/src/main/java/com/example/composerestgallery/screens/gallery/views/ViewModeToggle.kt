package com.example.composerestgallery.screens.gallery.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.example.composerestgallery.screens.gallery.viewmodel.GalleryViewMode

@Composable
fun ViewModeToggle(
    currentViewMode: GalleryViewMode,
    onClick: () -> Unit
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = ImageVector.vectorResource(currentViewMode.iconRes),
            contentDescription = LocalContext.current.getString(currentViewMode.description),
            tint = MaterialTheme.colors.surface
        )
    }
}