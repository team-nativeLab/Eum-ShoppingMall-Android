package com.nativelap.eumshoppingmall.ui.catalog

import org.junit.Assert.assertEquals
import org.junit.Test

class QuickCartDecisionTest {
    @Test
    fun loggedOutTapRoutesToLoginInsteadOfAdding() {
        assertEquals(QuickCartDecision.RequireLogin, decideQuickCart(isLoggedIn = false))
    }

    @Test
    fun loggedInTapUsesExistingAddPath() {
        assertEquals(QuickCartDecision.Add, decideQuickCart(isLoggedIn = true))
    }
}
