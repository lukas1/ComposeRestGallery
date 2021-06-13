package com.example.composerestgallery.shared.model

sealed class LoadingState<out Value> {
    object Loading : LoadingState<Nothing>()
    data class Loaded<Value>(val value: Value) : LoadingState<Value>()
    object Error : LoadingState<Nothing>()

    val loadedValue: Value?
        get() = when (this) {
            is Loaded -> value
            else -> null
        }
}
