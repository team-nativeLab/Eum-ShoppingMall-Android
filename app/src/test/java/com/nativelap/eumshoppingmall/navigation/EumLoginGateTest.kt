package com.nativelap.eumshoppingmall.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EumLoginGateTest {
    private val checkoutDestination = EumDestination.CheckoutAddress(orderedCartLineIds = listOf("line-1"))

    @Test
    fun loggedInUserGoesToTargetDirectly() {
        assertEquals(checkoutDestination, gateDestination(checkoutDestination, isLoggedIn = true))
    }

    @Test
    fun loggedOutUserIsSentToLoginWithTargetPending() {
        assertEquals(
            EumDestination.Login(pendingDestination = checkoutDestination),
            gateDestination(checkoutDestination, isLoggedIn = false),
        )
    }

    @Test
    fun loginTargetIsNeverWrappedAgain() {
        val loginDestination = EumDestination.Login(pendingDestination = EumDestination.QuoteHistory)

        assertEquals(loginDestination, gateDestination(loginDestination, isLoggedIn = false))
        assertEquals(loginDestination, gateDestination(loginDestination, isLoggedIn = true))
    }

    @Test
    fun drawerIsNeverWrappedAsPendingDestination() {
        assertEquals(EumDestination.Drawer, gateDestination(EumDestination.Drawer, isLoggedIn = false))
    }

    @Test
    fun onlyLoginIsAuthDestination() {
        assertTrue(isAuthDestination(EumDestination.Login()))
        assertFalse(isAuthDestination(EumDestination.Cart))
    }
}
