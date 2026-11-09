package com.nativelap.eumshoppingmall.navigation

/**
 * 드로어(A-12)와 마이(A-11) 화면이 내보내는 문자열 메뉴 ID를 목적지로 바꾸는 단일 매핑.
 *
 * 화면은 기존 문자열 ID를 그대로 내보내고, 어떤 ID가 어느 화면으로 가는지는 이 파일에서만 정한다.
 * 이번 범위에 화면 연결이 없는 ID(예: 설정, 내 정보 관리, 배송지 관리, 취소·반품·교환)는 null을 반환해 이동하지 않는다.
 * 제외 이유는 docs/APP_FLOW.md의 제외 표에 적는다.
 */
object EumMenuDestinations {
    /** A-12 드로어 메뉴 ID(AccountDrawerScreen의 MenuClicked). */
    fun drawerMenu(menuId: String): EumDestination? = when (menuId) {
        "events" -> EumDestination.EventList
        "quotes" -> EumDestination.QuoteRequest()
        "support" -> EumDestination.CustomerSupport
        "notices" -> EumDestination.NoticeList
        "company" -> EumDestination.CompanyIntro
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
        "quotes" -> EumDestination.QuoteHistory
        "inquiries" -> EumDestination.InquiryHistory
        "customer" -> EumDestination.CustomerSupport
        "notices" -> EumDestination.NoticeList
        else -> null
    }
}
