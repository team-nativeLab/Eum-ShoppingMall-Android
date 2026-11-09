package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class EumDestinationMappingTest {
    @Test
    fun eachTabHasDistinctRootDestination() {
        val rootDestinations = MainNavigationTab.entries.map { tab -> tab.rootDestination() }

        assertEquals(
            listOf(
                EumDestination.Home,
                EumDestination.Categories,
                EumDestination.Search,
                EumDestination.Cart,
                EumDestination.MyPage,
            ),
            rootDestinations,
        )
    }

    @Test
    fun tabOrderMatchesFigmaBottomNavigation() {
        assertEquals(
            listOf("Home", "Categories", "Search", "Cart", "MyPage"),
            MainNavigationTab.entries.map { tab -> tab.name },
        )
        assertEquals(MainNavigationTab.Home, EumStartTab)
    }

    @Test
    fun drawerMenuIdsMapToDrawerDestinations() {
        assertEquals(EumDestination.EventList, EumMenuDestinations.drawerMenu("events"))
        assertEquals(EumDestination.QuoteRequest(), EumMenuDestinations.drawerMenu("quotes"))
        assertEquals(EumDestination.CustomerSupport, EumMenuDestinations.drawerMenu("support"))
        assertEquals(EumDestination.NoticeList, EumMenuDestinations.drawerMenu("notices"))
        assertEquals(EumDestination.CompanyIntro, EumMenuDestinations.drawerMenu("company"))
        assertEquals(EumDestination.Login(), EumMenuDestinations.drawerMenu("login"))
        assertNull(EumMenuDestinations.drawerMenu("settings"))
    }

    @Test
    fun myPageShortcutIdsMapToFirstLevelDestinations() {
        assertEquals(EumDestination.OrderHistory, EumMenuDestinations.myPageShortcut("orders"))
        assertEquals(EumDestination.Favorites, EumMenuDestinations.myPageShortcut("favorites"))
        assertEquals(EumDestination.RecentProducts, EumMenuDestinations.myPageShortcut("recent"))
        assertEquals(EumDestination.Coupons, EumMenuDestinations.myPageShortcut("coupons"))
        assertEquals(EumDestination.Points, EumMenuDestinations.myPageShortcut("points"))
        assertNull(EumMenuDestinations.myPageShortcut("unknown"))
    }

    @Test
    fun myPageMenuRowIdsMapToFirstLevelDestinations() {
        assertEquals(EumDestination.QuoteHistory, EumMenuDestinations.myPageMenuRow("quotes"))
        assertEquals(EumDestination.CustomerSupport, EumMenuDestinations.myPageMenuRow("customer"))
        assertEquals(EumDestination.NoticeList, EumMenuDestinations.myPageMenuRow("notices"))
        assertEquals(EumDestination.InquiryHistory, EumMenuDestinations.myPageMenuRow("inquiries"))
        // 아래 ID는 연결할 화면 흐름이 이번 범위 밖이다(docs/APP_FLOW.md 제외 표).
        assertNull(EumMenuDestinations.myPageMenuRow("profile"))
        assertNull(EumMenuDestinations.myPageMenuRow("addresses"))
        assertNull(EumMenuDestinations.myPageMenuRow("returns"))
    }
}
