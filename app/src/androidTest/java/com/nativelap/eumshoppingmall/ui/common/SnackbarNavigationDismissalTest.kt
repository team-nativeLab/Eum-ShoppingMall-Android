package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.launch
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SnackbarNavigationDismissalTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun changingDestinationDismissesVisibleSnackbar() {
        lateinit var hostState: SnackbarHostState
        var completed = false
        composeRule.setContent {
            val state = remember { SnackbarHostState() }
            hostState = state
            var destination by remember { mutableStateOf("cart") }
            DismissSnackbarOnNavigationChange(state, destination)
            LaunchedEffect(state) {
                launch {
                    state.showSnackbar("안내", duration = SnackbarDuration.Indefinite)
                    completed = true
                }
            }
            androidx.compose.material3.TextButton(onClick = { destination = "checkout" }) { androidx.compose.material3.Text("전환") }
        }
        composeRule.waitUntil(5_000) { hostState.currentSnackbarData != null }
        composeRule.onNodeWithText("전환").performClick()
        composeRule.waitUntil(5_000) { completed && hostState.currentSnackbarData == null }
        assertTrue(completed)
    }
}
