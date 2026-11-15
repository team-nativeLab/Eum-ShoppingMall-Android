package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.runtime.mutableStateOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteTogglesTest {
    @Test
    fun loggedOutToggleAsksForLoginAndKeepsState() {
        var loginRequestCount = 0
        val favoriteToggles = FavoriteToggles(mutableStateOf(emptySet()), isLoggedIn = false) { loginRequestCount += 1 }

        favoriteToggles.toggle("p1")

        assertEquals(1, loginRequestCount)
        assertFalse(favoriteToggles.isFavorite("p1"))
    }

    @Test
    fun loggedInToggleAddsThenRemovesWithoutLoginRequest() {
        var loginRequestCount = 0
        val favoriteToggles = FavoriteToggles(mutableStateOf(emptySet()), isLoggedIn = true) { loginRequestCount += 1 }

        favoriteToggles.toggle("p1")
        assertTrue(favoriteToggles.isFavorite("p1"))
        favoriteToggles.toggle("p1")

        assertFalse(favoriteToggles.isFavorite("p1"))
        assertEquals(0, loginRequestCount)
    }
}
