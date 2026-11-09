package com.nativelap.eumshoppingmall.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 앱의 모든 Navigation 3 목적지.
 *
 * 인자는 ID와 검색어만 담는다. 화면 표시 데이터(UiModel), 가격, 개인정보, 비밀번호는 back stack에 저장되므로 넣지 않는다.
 * 주석의 A-xx는 Figma `06 Design — App` 프레임 번호다.
 */
@Serializable
sealed interface EumDestination : NavKey {
    /** A-02 홈 탭 루트. */
    @Serializable
    data object Home : EumDestination

    /** A-03 카테고리 탭 루트. */
    @Serializable
    data object Categories : EumDestination

    /** A-06 검색 탭 루트. */
    @Serializable
    data object Search : EumDestination

    /** A-07 장바구니 탭 루트. */
    @Serializable
    data object Cart : EumDestination

    /** A-11 마이 탭 루트. */
    @Serializable
    data object MyPage : EumDestination

    /** A-12 드로어 메뉴. 현재 화면 위 overlay(Dialog scene)로 표시한다. */
    @Serializable
    data object Drawer : EumDestination

    /** A-04 상품 목록. categoryId가 null이면 전체 상품이다. */
    @Serializable
    data class ProductList(val categoryId: String? = null) : EumDestination

    /** A-06b 검색 결과 / A-06z 검색 결과 0건. 같은 키에서 Route가 결과 유무로 화면을 고른다. */
    @Serializable
    data class SearchResults(val query: String) : EumDestination

    /** A-05 상품 상세. */
    @Serializable
    data class ProductDetail(val productId: String) : EumDestination

    /** A-08 주문하기 1 배송지. */
    @Serializable
    data object CheckoutAddress : EumDestination

    /** A-09 주문하기 2 결제. */
    @Serializable
    data object CheckoutPayment : EumDestination

    /** A-10 주문 완료. */
    @Serializable
    data object OrderComplete : EumDestination

    /** A-15 견적 문의. productId가 있으면 해당 상품을 담아 시작한다(F7). */
    @Serializable
    data class QuoteRequest(val productId: String? = null) : EumDestination

    /** A-15 견적 문의의 "상품 추가"에서 여는 상품 선택. 화면은 A-04 전체 상품 목록을 그대로 쓴다. */
    @Serializable
    data object QuoteProductPicker : EumDestination

    /** A-15d 견적 문의 완료. */
    @Serializable
    data object QuoteComplete : EumDestination

    /** A-25 견적 문의 내역. */
    @Serializable
    data object QuoteHistory : EumDestination

    /** A-25에서 진입하는 견적 문의 상세. */
    @Serializable
    data class QuoteInquiryDetail(val inquiryId: String) : EumDestination

    /** A-16 주문 내역. */
    @Serializable
    data object OrderHistory : EumDestination

    /** A-20 찜한 상품. */
    @Serializable
    data object Favorites : EumDestination

    /** A-21 최근 본 상품. */
    @Serializable
    data object RecentProducts : EumDestination

    /** A-22 쿠폰함. */
    @Serializable
    data object Coupons : EumDestination

    /** A-22p 적립금. */
    @Serializable
    data object Points : EumDestination

    /** A-31 1:1 문의 내역. */
    @Serializable
    data object InquiryHistory : EumDestination

    /** A-42 고객센터. */
    @Serializable
    data object CustomerSupport : EumDestination

    /** A-40 공지사항 목록. */
    @Serializable
    data object NoticeList : EumDestination

    /** A-54 이벤트 목록. */
    @Serializable
    data object EventList : EumDestination

    /** A-43 회사 소개. */
    @Serializable
    data object CompanyIntro : EumDestination

    /**
     * A-13 로그인(F4).
     *
     * 로그인이 필요한 화면으로 가려다 막힌 경우 그 목적지를 pendingDestination에 담아 push한다.
     * 로그인 성공 시 [EumMainNavigator.completeLogin]이 이 항목을 pop한 뒤 pendingDestination을 push한다.
     */
    @Serializable
    data class Login(val pendingDestination: EumDestination? = null) : EumDestination
}
