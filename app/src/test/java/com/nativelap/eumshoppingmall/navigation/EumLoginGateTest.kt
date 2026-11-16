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
    fun loginSignupAndRecoveryAreAuthDestinations() {
        assertTrue(isAuthDestination(EumDestination.Login()))
        assertTrue(isAuthDestination(EumDestination.Signup))
        assertTrue(isAuthDestination(EumDestination.AccountRecovery()))
        assertTrue(isAuthDestination(EumDestination.AccountRecovery(findsPassword = true)))
        assertFalse(isAuthDestination(EumDestination.Cart))
    }

    @Test
    fun orderClaimInquiryAndReviewDestinationsRequireLogin() {
        listOf(
            EumDestination.OrderDetail("order-1"),
            EumDestination.DeliveryTracking("order-1"),
            EumDestination.ClaimHub,
            EumDestination.ClaimOrderSelection,
            EumDestination.ClaimRequest("order-1", "Cancel"),
            EumDestination.ClaimResult("request-1"),
            EumDestination.ClaimHistory,
            EumDestination.ClaimDetail("request-1"),
            EumDestination.ClaimWithdrawal("request-1"),
            EumDestination.ClaimRejection("request-1"),
            EumDestination.InquiryWrite,
            EumDestination.InquiryDetail("inquiry-1"),
            EumDestination.ReviewWrite("p1"),
            EumDestination.ProductInquiryWrite("p1"),
            EumDestination.ProductInquiryComplete("p1"),
            EumDestination.Favorites,
            EumDestination.OrderHistory,
            EumDestination.InquiryHistory,
            EumDestination.QuoteHistory,
            EumDestination.QuoteInquiryDetail("inquiry-1"),
            EumDestination.Coupons,
            EumDestination.Points,
            EumDestination.CheckoutAddress(),
            EumDestination.CheckoutPayment(),
            EumDestination.OrderComplete,
        ).forEach { destination ->
            assertTrue(destination.toString(), requiresLogin(destination))
        }
    }

    @Test
    fun publicDestinationsDoNotRequireLogin() {
        listOf(
            EumDestination.Home,
            EumDestination.ProductDetail("p1"),
            EumDestination.ProductReviews("p1"),
            EumDestination.BrandDetail("brand-a"),
            EumDestination.NoticeDetail("notice-1"),
            EumDestination.EventDetail("event-1"),
            EumDestination.AddressSearch,
            EumDestination.Signup,
            EumDestination.AccountRecovery(),
            EumDestination.CustomerSupport,
            EumDestination.RecentProducts,
        ).forEach { destination ->
            assertFalse(destination.toString(), requiresLogin(destination))
        }
    }

    @Test
    fun gateIfProtectedWrapsOnlyProtectedDestinationsWhenLoggedOut() {
        val protectedDestination = EumDestination.OrderDetail("order-1")

        assertEquals(
            EumDestination.Login(pendingDestination = protectedDestination),
            gateIfProtected(protectedDestination, isLoggedIn = false),
        )
        assertEquals(protectedDestination, gateIfProtected(protectedDestination, isLoggedIn = true))
        assertEquals(EumDestination.NoticeDetail("n"), gateIfProtected(EumDestination.NoticeDetail("n"), isLoggedIn = false))
    }
}
