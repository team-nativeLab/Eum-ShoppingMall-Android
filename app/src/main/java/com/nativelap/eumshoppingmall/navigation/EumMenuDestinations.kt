package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.catalog.ProductListPreset

/**
 * 드로어(A-12)와 마이(A-11) 화면이 내보내는 문자열 메뉴 ID를 목적지로 바꾸는 단일 매핑.
 *
 * 화면은 기존 문자열 ID를 그대로 내보내고, 어떤 ID가 어느 화면으로 가는지는 이 파일에서만 정한다.
 * 화면 연결이 없는 ID(예: 설정, 내 정보 관리, 배송지 관리)는 null을 반환하고, 호출부는 "준비 중" 안내만 보여 준다.
 * 제외 이유는 docs/APP_FLOW.md의 제외 표에 적는다.
 */
object EumMenuDestinations {
    /** A-12 드로어 메뉴 ID(AccountDrawerScreen의 MenuClicked). */
    fun drawerMenu(menuId: String): EumDestination? = when (menuId) {
        "best" -> EumDestination.ProductList(preset = ProductListPreset.Best)
        "recommended" -> EumDestination.ProductList(preset = ProductListPreset.Recommended)
        "discount" -> EumDestination.ProductList(preset = ProductListPreset.Discount)
        "favorites" -> EumDestination.Favorites
        "events" -> EumDestination.EventList
        "support" -> EumDestination.CustomerSupport
        "guide" -> EumDestination.UsageGuide
        "terms" -> EumDestination.Terms
        "privacy" -> EumDestination.PrivacyPolicy
        "company" -> EumDestination.CompanyIntro
        "notifications" -> EumDestination.Notifications
        "settings" -> EumDestination.AccountSettings
        "login" -> EumDestination.Login()
        else -> null
    }

    /** A-11 마이 화면의 바로가기 ID(MyPageScreenEvent.ShortcutClicked). */
    fun myPageShortcut(shortcutId: String): EumDestination? = when (shortcutId) {
        "orders" -> EumDestination.OrderHistory
        "favorites" -> EumDestination.Favorites
        "recent" -> EumDestination.RecentProducts
        "coupons" -> EumDestination.Coupons
        "points" -> EumDestination.Points
        else -> null
    }

    /** A-11 마이 화면의 메뉴 행 ID(MyPageScreenEvent.MenuRowClicked). */
    fun myPageMenuRow(rowId: String): EumDestination? = when (rowId) {
        "inquiries" -> EumDestination.InquiryHistory
        "customer" -> EumDestination.CustomerSupport
        "returns" -> EumDestination.ClaimHub
        else -> null
    }
}
