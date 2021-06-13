package com.example.composerestgallery.screens.gallery.views

import androidx.compose.material.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.composerestgallery.MainActivity
import com.example.composerestgallery.R
import com.example.composerestgallery.shared.model.LoadingState
import org.junit.Assert.assertEquals
import org.junit.Rule

import org.junit.Test

class LoadingViewKtTest {
    @get:Rule
    val screenTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loadingState() {
        val something = "something"
        screenTestRule.setContent {
            LoadingView(loadingState = LoadingState.Loading, onRetry = {}) {
                Text(text = something)
            }
        }

        // Mode is set to LOADING, so only loading view should be displayed, other views are hidden
        screenTestRule
            .onRoot()
            .onChild()
            .assert(hasText(screenTestRule.activity.getString(R.string.loading)))

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertDoesNotExist()

        screenTestRule
            .onNode(hasText(something))
            .assertDoesNotExist()
    }

    @Test
    fun errorState() {
        val something = "something"
        var hasRetried = false
        screenTestRule.setContent {
            LoadingView(loadingState = LoadingState.Error, onRetry = { hasRetried = true }) {
                Text(text = something)
            }
        }

        // Mode is set to ERROR, so only Error view should be displayed, other views are hidden
        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertExists()

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertDoesNotExist()

        screenTestRule
            .onNode(hasText(something))
            .assertDoesNotExist()

        assertEquals(false, hasRetried)

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.retry)))
            .performClick()

        assertEquals(true, hasRetried)
    }

    @Test
    fun loadedState() {
        val expectedText = "expected"
        screenTestRule.setContent {
            LoadingView(loadingState = LoadingState.Loaded(expectedText), onRetry = {}) { value ->
                Text(text = value)
            }
        }

        // Mode is set to LOADED, so only view with loaded data should be displayed,
        // other views are hidden
        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.loading)))
            .assertDoesNotExist()

        screenTestRule
            .onNode(hasText(screenTestRule.activity.getString(R.string.error)))
            .assertDoesNotExist()

        screenTestRule
            .onRoot()
            .onChild()
            .assert(hasText(expectedText))
    }
}