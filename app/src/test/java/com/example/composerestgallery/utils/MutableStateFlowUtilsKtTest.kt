package com.example.composerestgallery.utils

import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Test

class MutableStateFlowUtilsKtTest {
    @Test
    fun nextState() {
        // This method should enable only partial updates to state -> updating only one property of
        // state. Therefore in this test state is updated only partially in each step and confirmed
        // that only partial update on state happens.
        val firstFirst = 1
        val firstSecond = "a"

        val secondFirst = 2
        val secondSecond = "b"
        data class TestClass(
            val first: Int,
            val second: String
        )
        val stateFlow = MutableStateFlow(
            TestClass(firstFirst, firstSecond)
        )

        assertEquals(
            TestClass(firstFirst, firstSecond),
            stateFlow.value
        )

        stateFlow.nextState { copy(first = secondFirst) }
        assertEquals(
            TestClass(secondFirst, firstSecond),
            stateFlow.value
        )

        stateFlow.nextState { copy(second = secondSecond) }
        assertEquals(
            TestClass(secondFirst, secondSecond),
            stateFlow.value
        )
    }
}