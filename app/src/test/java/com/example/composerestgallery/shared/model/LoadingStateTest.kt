package com.example.composerestgallery.shared.model

import org.junit.Assert.*

import org.junit.Test

class LoadingStateTest {

    @Test
    fun getLoadedValue() {
        assertEquals(null, LoadingState.Loading.loadedValue)
        assertEquals(null, LoadingState.Error.loadedValue)
        val testValue = "test"
        assertEquals(testValue, LoadingState.Loaded(testValue).loadedValue)
    }
}