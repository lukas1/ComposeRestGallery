package com.example.composerestgallery.utils

import kotlinx.coroutines.flow.MutableStateFlow

inline fun <T> MutableStateFlow<T>.nextState(computeNextState: T.() -> T) {
    tryEmit(value.computeNextState())
}