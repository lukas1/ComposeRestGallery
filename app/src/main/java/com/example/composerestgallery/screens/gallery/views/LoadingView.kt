package com.example.composerestgallery.screens.gallery.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.composerestgallery.R
import com.example.composerestgallery.shared.model.LoadingState

@Composable
fun <Value> LoadingView(
    loadingState: LoadingState<Value>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    content: @Composable (Value) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (loadingState) {
            is LoadingState.Loading -> Text(text = LocalContext.current.getString(R.string.loading))
            is LoadingState.Error -> Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = LocalContext.current.getString(R.string.error))
                Button(onClick = { onRetry() }) {
                    Text(text = LocalContext.current.getString(R.string.retry))
                }
            }
            is LoadingState.Loaded<Value> -> content(loadingState.value)
        }
    }
}