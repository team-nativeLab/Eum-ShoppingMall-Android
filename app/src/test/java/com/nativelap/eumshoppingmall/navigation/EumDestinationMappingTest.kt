package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.catalog.ProductListPreset
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
        assertNull(EumMenuDestinations.drawerMenu("removed-menu"))
        assertEquals(EumDestination.CustomerSupport, EumMenuDestinations.drawerMenu("support"))
        assertEquals(EumDestination.NoticeList, EumMenuDestinations.drawerMenu("notices"))
        assertEquals(EumDestination.CompanyIntro, EumMenuDestinations.drawerMenu("company"))
        assertEquals(EumDestination.Login(), EumMenuDestinations.drawerMenu("login"))
        assertEquals(EumDestination.ProductList(preset = ProductListPreset.Best), EumMenuDestinations.drawerMenu("best"))
        assertEquals(EumDestination.ProductList(preset = ProductListPreset.Recommended), EumMenuDestinations.drawerMenu("recommended"))
        assertEquals(EumDestination.ProductList(preset = ProductListPreset.Discount), EumMenuDestinations.drawerMenu("discount"))
        assertEquals(EumDestination.Favorites, EumMenuDestinations.drawerMenu("favorites"))
        assertEquals(EumDestination.UsageGuide, EumMenuDestinations.drawerMenu("guide"))
        assertEquals(EumDestination.Terms, EumMenuDestinations.drawerMenu("terms"))
        assertEquals(EumDestination.PrivacyPolicy, EumMenuDestinations.drawerMenu("privacy"))
        assertEquals(EumDestination.Notifications, EumMenuDestinations.drawerMenu("notifications"))
        assertEquals(EumDestination.AccountSettings, EumMenuDestinations.drawerMenu("settings"))
    }

    @Test
    fun everyDrawerMenuIdResolvesToADestination() {
        listOf("best", "recommended", "discount", "favorites", "events", "notices", "support", "guide", "terms", "privacy", "company", "notifications", "settings")
            .forEach { menuId -> assertNotNull(menuId, EumMenuDestinations.drawerMenu(menuId)) }
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
        assertNull(EumMenuDestinations.myPageMenuRow("removed-menu"))
        assertEquals(EumDestination.CustomerSupport, EumMenuDestinations.myPageMenuRow("customer"))
        assertEquals(EumDestination.NoticeList, EumMenuDestinations.myPageMenuRow("notices"))
        assertEquals(EumDestination.InquiryHistory, EumMenuDestinations.myPageMenuRow("inquiries"))
        assertEquals(EumDestination.ClaimHub, EumMenuDestinations.myPageMenuRow("returns"))
        // 내 정보 관리와 배송지 관리는 저장 경로가 없어 목적지가 없다. 호출부가 "준비 중" 안내만 보여 준다.
        assertNull(EumMenuDestinations.myPageMenuRow("profile"))
        assertNull(EumMenuDestinations.myPageMenuRow("addresses"))
    }
}
