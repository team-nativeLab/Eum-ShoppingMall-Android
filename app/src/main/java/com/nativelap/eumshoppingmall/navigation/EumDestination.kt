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

    /**
     * A-08 주문하기 1 배송지.
     *
     * orderedCartLineIds는 주문 대상 장바구니 줄 id다. 장바구니에서 온 경우 선택한 줄, 바로구매는 빈 목록이며
     * 결제 성공 시 이 줄들만 장바구니에서 제거한다.
     */
    @Serializable
    data class CheckoutAddress(val orderedCartLineIds: List<String> = emptyList()) : EumDestination

    /** A-09 주문하기 2 결제. orderedCartLineIds는 [CheckoutAddress]에서 이어받는다. */
    @Serializable
    data class CheckoutPayment(val orderedCartLineIds: List<String> = emptyList()) : EumDestination

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

    /** A-14 회원가입. Login 위에 push하므로 로그인의 보류 목적지는 그 아래 Login이 그대로 가지고, completeLogin이 함께 걷어 낸다. */
    @Serializable
    data object Signup : EumDestination

    /** A-27 이메일 찾기 / A-28 비밀번호 찾기. findsPassword가 true면 비밀번호 찾기 탭으로 시작한다. */
    @Serializable
    data class AccountRecovery(val findsPassword: Boolean = false) : EumDestination

    /** A-17 주문 상세. */
    @Serializable
    data class OrderDetail(val orderId: String) : EumDestination

    /** A-18 배송 조회. */
    @Serializable
    data class DeliveryTracking(val orderId: String) : EumDestination

    /** A-19 취소·반품·교환 안내 허브. */
    @Serializable
    data object ClaimHub : EumDestination

    /** A-19 신청할 주문 선택. */
    @Serializable
    data object ClaimOrderSelection : EumDestination

    /** A-19 취소·반품·교환 신청 폼. requestType은 OrderRequestType 이름이다. */
    @Serializable
    data class ClaimRequest(val orderId: String, val requestType: String) : EumDestination

    /** A-50 신청 완료. */
    @Serializable
    data class ClaimResult(val requestId: String) : EumDestination

    /** A-51 신청 내역. */
    @Serializable
    data object ClaimHistory : EumDestination

    /** A-51 신청 상세. */
    @Serializable
    data class ClaimDetail(val requestId: String) : EumDestination

    /** A-52 신청 철회 확인·완료. */
    @Serializable
    data class ClaimWithdrawal(val requestId: String) : EumDestination

    /** A-53 반려 사유. */
    @Serializable
    data class ClaimRejection(val requestId: String) : EumDestination

    /** A-46 주소 검색. 고른 주소 ID는 [EumAddressSelection]이 받는다. */
    @Serializable
    data object AddressSearch : EumDestination

    /** A-34 브랜드 상세. */
    @Serializable
    data class BrandDetail(val brandId: String) : EumDestination

    /** A-49 상품 전체 리뷰. */
    @Serializable
    data class ProductReviews(val productId: String) : EumDestination

    /** 리뷰 작성. */
    @Serializable
    data class ReviewWrite(val productId: String) : EumDestination

    /** A-44 상품 문의 작성. */
    @Serializable
    data class ProductInquiryWrite(val productId: String) : EumDestination

    /** A-45 상품 문의 완료. */
    @Serializable
    data class ProductInquiryComplete(val productId: String) : EumDestination

    /** A-31 하위 1:1 문의 상세. */
    @Serializable
    data class InquiryDetail(val inquiryId: String) : EumDestination

    /** A-31 하위 1:1 문의 작성. */
    @Serializable
    data object InquiryWrite : EumDestination

    /** A-41 공지 상세. */
    @Serializable
    data class NoticeDetail(val noticeId: String) : EumDestination

    /** A-55 이벤트 상세. */
    @Serializable
    data class EventDetail(val eventId: String) : EumDestination

    /**
     * A-13 로그인(F4).
     *
     * 로그인이 필요한 화면으로 가려다 막힌 경우 그 목적지를 pendingDestination에 담아 push한다.
     * 로그인 성공 시 [EumMainNavigator.completeLogin]이 이 항목을 pop한 뒤 pendingDestination을 push한다.
     */
    @Serializable
    data class Login(val pendingDestination: EumDestination? = null) : EumDestination
}
