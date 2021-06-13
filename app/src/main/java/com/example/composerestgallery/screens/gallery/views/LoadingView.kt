package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.composerestgallery.R
import com.example.composerestgallery.shared.model.LoadingState

@Composable
fun <Value> LoadingView(
    loadingState: LoadingState<Value>,
    onRetry: () -> Unit,
    content: @Composable (Value) -> Unit
) {
    when (loadingState) {
        is LoadingState.Loading -> Text(text = LocalContext.current.getString(R.string.loading))
        is LoadingState.Error -> Column {
            Text(text = LocalContext.current.getString(R.string.error))
            Button(onClick = { onRetry() }) {
                Text(text = LocalContext.current.getString(R.string.retry))
            }
        }
        is LoadingState.Loaded<Value> -> content(loadingState.value)
    }
}