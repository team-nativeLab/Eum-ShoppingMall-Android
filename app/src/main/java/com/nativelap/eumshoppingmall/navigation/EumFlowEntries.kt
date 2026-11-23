package com.nativelap.eumshoppingmall.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.addresssearch.AddressSearchRoute
import com.nativelap.eumshoppingmall.ui.auth.AccountService
import com.nativelap.eumshoppingmall.ui.auth.view.AccountRecoveryRoute
import com.nativelap.eumshoppingmall.ui.auth.view.AuthSignupRoute
import com.nativelap.eumshoppingmall.ui.catalog.CatalogBrandDetailRoute
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.events.EventDetailRoute
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimDetailRoute
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimHistoryRoute
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimHubRoute
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimOrderSelectionRoute
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimRejectionRoute
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimRequestFormRoute
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimResultRoute
import com.nativelap.eumshoppingmall.ui.orders.view.ClaimWithdrawalRoute
import com.nativelap.eumshoppingmall.ui.orders.view.DeliveryTrackingRoute
import com.nativelap.eumshoppingmall.ui.orders.view.OrderDetailRoute
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.localClaimResultUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.orderRequestSubmissionSummary
import com.nativelap.eumshoppingmall.ui.productinquiry.ProductInquiryCompleteRoute
import com.nativelap.eumshoppingmall.ui.productinquiry.ProductInquiryRoute
import com.nativelap.eumshoppingmall.ui.productreview.ProductReviewRoute
import com.nativelap.eumshoppingmall.ui.productreview.ProductReviewWriteRoute
import com.nativelap.eumshoppingmall.ui.screendata.ShoppingScreenDataProvider
import com.nativelap.eumshoppingmall.ui.support.InquiryDetailRoute
import com.nativelap.eumshoppingmall.ui.support.InquiryWriteRoute
import com.nativelap.eumshoppingmall.ui.support.NoticeDetailRoute

/**
 * 엔트리 그룹 함수들이 함께 쓰는 의존성. 보호 화면 판단은 [open]에서 [gateIfProtected]에 맡기고 Navigator는 요청받은 목적지만 쌓는다.
 */
internal class EumEntryContext(
    val navigator: EumMainNavigator,
    val loginSession: EumLoginSession,
    val screenDataProvider: ShoppingScreenDataProvider,
    val onUnavailableAction: () -> Unit,
    val claimSubmissionDescriptions: Map<String, String>,
    val saveClaimSubmissionDescription: (String, String) -> Unit,
    val claimMockSubmissionDescriptionFormat: String,
) {
    // 로그인이 필요한 목적지는 비로그인이면 보류 목적지를 담은 Login으로 바꿔 쌓는다.
    fun open(destination: EumDestination) {
        navigator.push(gateIfProtected(destination, loginSession.isLoggedIn))
    }

    // 돌아갈 곳이 지금 화면이므로 보류 목적지 없이 로그인만 연다(찜 하트 등).
    fun openLogin() {
        navigator.push(EumDestination.Login())
    }

    // 장바구니 담기에서 온 로그인. 로그인 화면에 담기 안내 한 줄을 붙인다.
    fun openLoginForCart() {
        navigator.push(EumDestination.Login(cartNotice = true))
    }
}

/** 회원가입(A-14)·계정 찾기(A-27·A-28). 둘 다 Login 위에 쌓이므로 완료 시 [EumMainNavigator.completeLogin]이 함께 걷어 낸다. */
internal fun EntryProviderScope<NavKey>.addAuthEntries(
    context: EumEntryContext,
    accountService: AccountService,
    onSignupCompleted: () -> Unit,
) {
    val navigator = context.navigator
    entry<EumDestination.Signup> {
        AuthSignupRoute(
            accountService = accountService,
            onSignupSuccess = onSignupCompleted,
            onBackClick = { navigator.goBack() },
            onTermsDetailsClick = { navigator.push(EumDestination.Terms) },
            onPrivacyDetailsClick = { navigator.push(EumDestination.PrivacyPolicy) },
            // 마케팅 동의 상세 문서는 아직 목적지가 없어 준비 중 안내를 유지한다.
            onMarketingDetailsClick = context.onUnavailableAction,
        )
    }
    entry<EumDestination.AccountRecovery> { destination ->
        AccountRecoveryRoute(
            accountService = accountService,
            startsWithPasswordMode = destination.findsPassword,
            onCloseClick = { navigator.goBack() },
            // 계정 찾기는 항상 Login 위에서 열리므로 로그인 화면으로 돌아가는 것이 한 단계 뒤로가기다.
            onLoginClick = { navigator.goBack() },
            onSignupClick = { navigator.push(EumDestination.Signup) },
        )
    }
}

/** 주문 상세(A-17)·배송 조회(A-18)·취소/반품/교환(A-19, A-50~A-53)·주소 검색(A-46). */
internal fun EntryProviderScope<NavKey>.addOrderEntries(
    context: EumEntryContext,
    addressSelection: EumAddressSelection,
) {
    val navigator = context.navigator
    val provider = context.screenDataProvider

    // 신청 상세·철회·반려·완료에서 내역으로 갈 때 사이에 쌓인 신청 화면을 걷어 내고 내역 하나만 남긴다.
    fun showClaimHistory() {
        navigator.popWhileAndPush(EumDestination.ClaimHistory, ::isClaimHistoryCleanupTarget)
    }

    entry<EumDestination.OrderDetail> { destination ->
        OrderDetailRoute(
            uiModel = remember(destination) { provider.orderDetail(destination.orderId) },
            onBackClick = { navigator.goBack() },
            onTrackDeliveryClick = { context.open(EumDestination.DeliveryTracking(destination.orderId)) },
            onExchangeReturnClick = {
                context.open(EumDestination.ClaimRequest(destination.orderId, OrderRequestType.Return.name))
            },
            onUnavailableAction = context.onUnavailableAction,
        )
    }
    entry<EumDestination.DeliveryTracking> { destination ->
        DeliveryTrackingRoute(
            uiModel = remember(destination) { provider.deliveryTracking(destination.orderId) },
            onBackClick = { navigator.goBack() },
            onUnavailableAction = context.onUnavailableAction,
        )
    }
    entry<EumDestination.ClaimHub> {
        ClaimHubRoute(
            onBackClick = { navigator.goBack() },
            onOrdersClick = { navigator.push(EumDestination.OrderHistory) },
            onRequestHistoryClick = { context.open(EumDestination.ClaimHistory) },
            onStartRequestClick = { context.open(EumDestination.ClaimOrderSelection) },
        )
    }
    entry<EumDestination.ClaimOrderSelection> {
        ClaimOrderSelectionRoute(
            uiModel = remember { provider.claimOrders() },
            onBackClick = { navigator.goBack() },
            onOrderDetailsClick = { orderId -> context.open(EumDestination.OrderDetail(orderId)) },
            onOrderDeliveryClick = { orderId -> context.open(EumDestination.DeliveryTracking(orderId)) },
            onOrderSelected = { orderId, requestType ->
                context.open(EumDestination.ClaimRequest(orderId, requestType.name))
            },
        )
    }
    entry<EumDestination.ClaimRequest> { destination ->
        val requestType = OrderRequestType.entries.firstOrNull { type -> type.name == destination.requestType }
            ?: OrderRequestType.Cancel
        ClaimRequestFormRoute(
            uiModel = remember(destination) { provider.claimForm(destination.orderId, requestType) },
            onBackClick = { navigator.goBack() },
            onUnavailableAction = context.onUnavailableAction,
            onSubmit = { submission ->
                // 서버 연동 전이라 신청 ID가 없어 주문 ID를 임시 신청 ID로 쓴다. 완료 뒤 뒤로가기로 폼에 다시 들어가지 않게 걷어 낸다.
                val submittedItems = orderRequestSubmissionSummary(submission)
                val submissionDescription = context.claimMockSubmissionDescriptionFormat.replace("%1\$s", submittedItems)
                context.saveClaimSubmissionDescription(destination.orderId, submissionDescription)
                navigator.popWhileAndPushIfTop(
                    topDestination = destination,
                    destination = EumDestination.ClaimResult(destination.orderId),
                    shouldRemove = ::isClaimSubmitCleanupTarget,
                )
            },
        )
    }
    entry<EumDestination.ClaimResult> { destination ->
        val submittedDescription = context.claimSubmissionDescriptions[destination.requestId]
        val restoredDemoNotice = stringResource(R.string.request_mock_result_restored_notice)
        ClaimResultRoute(
            uiModel = remember(destination, submittedDescription, restoredDemoNotice) {
                localClaimResultUiModel(submittedDescription, restoredDemoNotice)
            },
            onBackClick = { navigator.goBack() },
            onRequestHistoryClick = ::showClaimHistory,
        )
    }
    entry<EumDestination.ClaimHistory> {
        ClaimHistoryRoute(
            uiModel = remember { provider.claimHistory() },
            onBackClick = { navigator.goBack() },
            onRequestDetailsClick = { requestId -> context.open(EumDestination.ClaimDetail(requestId)) },
            onWithdrawalClick = { requestId -> context.open(EumDestination.ClaimWithdrawal(requestId)) },
            onRejectionReasonClick = { requestId -> context.open(EumDestination.ClaimRejection(requestId)) },
            onOrdersClick = { navigator.push(EumDestination.OrderHistory) },
        )
    }
    entry<EumDestination.ClaimDetail> { destination ->
        ClaimDetailRoute(
            uiModel = remember(destination) { provider.claimDetail(destination.requestId) },
            onBackClick = { navigator.goBack() },
            onWithdrawalClick = { context.open(EumDestination.ClaimWithdrawal(destination.requestId)) },
            onRejectionReasonClick = { context.open(EumDestination.ClaimRejection(destination.requestId)) },
            onRequestHistoryClick = ::showClaimHistory,
        )
    }
    entry<EumDestination.ClaimWithdrawal> { destination ->
        ClaimWithdrawalRoute(
            requestNumber = remember(destination) { provider.claimDetail(destination.requestId).requestNumber },
            completedUiModel = remember(destination) { provider.claimWithdrawalResult(destination.requestId) },
            onBackClick = { navigator.goBack() },
            onRequestHistoryClick = ::showClaimHistory,
        )
    }
    entry<EumDestination.ClaimRejection> { destination ->
        ClaimRejectionRoute(
            uiModel = remember(destination) { provider.claimRejection(destination.requestId) },
            onBackClick = { navigator.goBack() },
            onRequestHistoryClick = ::showClaimHistory,
        )
    }
    entry<EumDestination.AddressSearch> {
        AddressSearchRoute(
            searchAddresses = provider::addressSearch,
            onBackClick = { navigator.goBack() },
            onAddressConfirmed = { addressId ->
                // 퇴장 애니메이션 중 두 번째 확정이 소비되지 않는 ID를 남기지 않도록, 검색 화면이 맨 위여서 pop에 성공했을 때만 ID를 맡긴다.
                if (navigator.popIfTop(EumDestination.AddressSearch)) {
                    addressSelection.select(addressId)
                }
            },
        )
    }
}

/** 1:1 문의 상세·작성, 공지 상세(A-41), 이벤트 상세(A-55). */
internal fun EntryProviderScope<NavKey>.addSupportEntries(context: EumEntryContext) {
    val navigator = context.navigator
    val provider = context.screenDataProvider
    entry<EumDestination.InquiryDetail> { destination ->
        InquiryDetailRoute(
            uiModel = remember(destination) { provider.inquiryDetail(destination.inquiryId) },
            onBackClick = { navigator.goBack() },
            onUnavailableAction = context.onUnavailableAction,
        )
    }
    entry<EumDestination.InquiryWrite> {
        InquiryWriteRoute(
            uiModel = remember { provider.inquiryForm() },
            onBackClick = { navigator.goBack() },
            onUnavailableAction = context.onUnavailableAction,
        )
    }
    entry<EumDestination.NoticeDetail> { destination ->
        NoticeDetailRoute(
            uiModel = remember(destination) { provider.noticeDetail(destination.noticeId) },
            onBackClick = { navigator.goBack() },
        )
    }
    entry<EumDestination.EventDetail> { destination ->
        EventDetailRoute(
            uiModel = remember(destination) { provider.eventDetail(destination.eventId) },
            onBackClick = { navigator.goBack() },
            onBrowseProductsClick = { navigator.closeTopAndOpenTabRoot(MainNavigationTab.Home) },
        )
    }
}

/** 브랜드 상세(A-34), 상품 전체 리뷰(A-49)·리뷰 작성, 상품 문의 작성·완료(A-44·A-45). */
internal fun EntryProviderScope<NavKey>.addCatalogDetailEntries(context: EumEntryContext) {
    val navigator = context.navigator
    val provider = context.screenDataProvider
    entry<EumDestination.BrandDetail> { destination ->
        CatalogBrandDetailRoute(
            uiModel = remember(destination) { provider.brandDetail(destination.brandId) },
            onBackClick = { navigator.goBack() },
            onProductClick = { productId -> navigator.push(EumDestination.ProductDetail(productId)) },
            onOtherBrandClick = { brandId -> navigator.push(EumDestination.BrandDetail(brandId)) },
            onViewAllBrandsClick = { navigator.navigateToTab(MainNavigationTab.Categories) },
            isLoggedIn = context.loginSession.isLoggedIn,
            onLoginRequired = context::openLogin,
            onUnavailableAction = context.onUnavailableAction,
        )
    }
    entry<EumDestination.ProductReviews> { destination ->
        ProductReviewRoute(
            uiModel = remember(destination) { provider.productReviews(destination.productId) },
            onBackClick = { navigator.goBack() },
            onReviewWriteClick = { context.open(EumDestination.ReviewWrite(destination.productId)) },
        )
    }
    entry<EumDestination.ReviewWrite> { destination ->
        ProductReviewWriteRoute(
            uiModel = remember(destination) { provider.reviewWrite(destination.productId) },
            onBackClick = { navigator.goBack() },
            onSearchClick = { navigator.navigateToTab(MainNavigationTab.Search) },
            onSubmitted = { navigator.popIfTop(destination) },
            onUnavailableAction = context.onUnavailableAction,
        )
    }
    // 제출은 서버 연동 전이라 저장·완료 이동 없이 준비 중 안내만 낸다(1:1 문의와 같은 방식).
    entry<EumDestination.ProductInquiryWrite> {
        ProductInquiryRoute(
            onBackClick = { navigator.goBack() },
            onUnavailableAction = context.onUnavailableAction,
        )
    }
    // 완료 목적지는 남겨 두었지만 앱 흐름에서 열리지 않는다(서버 연동 후 제출 완료 처리 때 다시 연결한다).
    entry<EumDestination.ProductInquiryComplete> { destination ->
        ProductInquiryCompleteRoute(
            onBackClick = { navigator.goBack() },
            // 상품 문의 내역 화면이 없어 상품 상세로 돌아간다.
            onInquiriesClick = { navigator.popIfTop(destination) },
        )
    }
}
