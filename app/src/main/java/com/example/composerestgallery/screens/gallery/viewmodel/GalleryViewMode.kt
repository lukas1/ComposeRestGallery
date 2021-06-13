package com.example.composerestgallery.screens.gallery.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.composerestgallery.R

enum class GalleryViewMode(
    @DrawableRes val iconRes: Int,
    @StringRes val description: Int
) {
    LIST(
        iconRes = R.drawable.ic_baseline_view_module_24,
        description = R.string.toggle_to_grid_description
    ),
    GRID(
        iconRes = R.drawable.ic_baseline_view_list_24,
        description = R.string.toggle_to_list_description
    )
}