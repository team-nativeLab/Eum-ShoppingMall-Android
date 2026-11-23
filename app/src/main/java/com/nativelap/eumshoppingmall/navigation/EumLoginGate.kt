package com.nativelap.eumshoppingmall.navigation

import androidx.navigation3.runtime.NavKey

/**
 * 로그인이 필요한 목적지로 가기 전에 거치는 게이트.
 *
 * 로그인 상태면 [target]을 그대로, 아니면 [target]을 보류 목적지로 담은 Login을 돌려준다.
 * Login·Drawer는 다시 감싸지 않아 Login(Login(...))이 쌓이지 않는다.
 */
fun gateDestination(
    target: EumDestination,
    isLoggedIn: Boolean,
): EumDestination = when {
    isLoggedIn -> target

    // Login과 Drawer(overlay)는 보류 목적지로 감싸지 않는다.
    target is EumDestination.Login || target is EumDestination.Drawer -> target

    else -> EumDestination.Login(pendingDestination = target)
}

/** 로그인이 있어야 열 수 있는 목적지인지 판별한다. 보호 화면이 늘면 여기에 더한다. */
fun requiresLogin(target: EumDestination): Boolean = when (target) {
    // 개인 데이터·결제·주문·클레임·문의·리뷰 작성은 로그인이 필요하다. else가 없어 새 목적지는 여기 분류가 컴파일 때 강제된다.
    EumDestination.OrderComplete,
    EumDestination.QuoteHistory,
    EumDestination.OrderHistory,
    EumDestination.Favorites,
    EumDestination.Coupons,
    EumDestination.Points,
    EumDestination.InquiryHistory,
    EumDestination.ClaimHub,
    EumDestination.ClaimOrderSelection,
    EumDestination.ClaimHistory,
    EumDestination.InquiryWrite,
    is EumDestination.CheckoutAddress,
    is EumDestination.CheckoutPayment,
    is EumDestination.QuoteInquiryDetail,
    is EumDestination.OrderDetail,
    is EumDestination.DeliveryTracking,
    is EumDestination.ClaimRequest,
    is EumDestination.ClaimResult,
    is EumDestination.ClaimDetail,
    is EumDestination.ClaimWithdrawal,
    is EumDestination.ClaimRejection,
    is EumDestination.ReviewWrite,
    is EumDestination.ProductInquiryWrite,
    is EumDestination.ProductInquiryComplete,
    is EumDestination.InquiryDetail,
    -> true

    EumDestination.Home,
    EumDestination.Categories,
    EumDestination.Search,
    EumDestination.Cart,
    EumDestination.MyPage,
    EumDestination.Drawer,
    EumDestination.QuoteProductPicker,
    EumDestination.QuoteComplete,
    EumDestination.RecentProducts,
    EumDestination.CustomerSupport,
    EumDestination.NoticeList,
    EumDestination.EventList,
    EumDestination.CompanyIntro,
    EumDestination.Terms,
    EumDestination.PrivacyPolicy,
    EumDestination.UsageGuide,
    EumDestination.Signup,
    EumDestination.AddressSearch,
    is EumDestination.ProductList,
    is EumDestination.SearchResults,
    is EumDestination.ProductDetail,
    is EumDestination.QuoteRequest,
    is EumDestination.AccountRecovery,
    is EumDestination.BrandDetail,
    is EumDestination.ProductReviews,
    is EumDestination.NoticeDetail,
    is EumDestination.EventDetail,
    is EumDestination.Login,
    -> false
}

/** [requiresLogin]인 목적지만 [gateDestination]을 거치게 한다. 호출부가 보호 여부를 따로 판단하지 않게 하는 진입 함수다. */
fun gateIfProtected(
    target: EumDestination,
    isLoggedIn: Boolean,
): EumDestination = if (requiresLogin(target)) gateDestination(target, isLoggedIn) else target

/** 로그인·가입·계정 찾기처럼 인증을 진행하는 화면인지 판별한다. 인증 계열 화면이 늘면 여기에 더한다. */
internal fun isAuthDestination(navKey: NavKey): Boolean = navKey is EumDestination.Login ||
    navKey is EumDestination.Signup ||
    navKey is EumDestination.AccountRecovery
