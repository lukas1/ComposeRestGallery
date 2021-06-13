package com.example.composerestgallery.shared.model

import org.junit.Assert.*

import org.junit.Test

class LoadingStateTest {

    @Test
    fun getLoadedValue() {
        assertNull(LoadingState.Loading.loadedValue)
        assertNull(LoadingState.Error.loadedValue)
        val testValue = "test"
        assertEquals(testValue, LoadingState.Loaded(testValue).loadedValue)
    }
}