package com.nativelap.eumshoppingmall.ui.auditpreview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.account.view.AccountAddressFormScreen
import com.nativelap.eumshoppingmall.ui.account.view.AccountProfileScreen
import com.nativelap.eumshoppingmall.ui.account.view.AccountSettingsScreen
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressFormUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileEditUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountSettingUiModel
import com.nativelap.eumshoppingmall.ui.auth.view.AccountWithdrawalReasonScreen
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountWithdrawalReasonUiModel
import com.nativelap.eumshoppingmall.ui.cart.view.CartScreen
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionGroupUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailScreen
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.view.OrderRequestHistoryScreen
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val noPriceProduct = CatalogProductUiModel(
    id = "preview-quote-required",
    name = "현장 맞춤형 자재",
    subtitle = "규격 확인 후 견적 제공",
    price = CatalogPriceUiModel.QuoteRequired,
)

private val profileGatePreview = AccountProfileUiModel(
    name = "홍길동",
    email = "example@email.com",
    phoneNumber = "010-0000-0000",
    companyName = "회사명",
    businessRegistrationNumber = "000-00-00000",
    department = "안전관리팀",
    isBusinessMember = true,
    isSmsConsentEnabled = true,
    isEmailConsentEnabled = false,
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-23 비밀번호 재확인")
@Composable
private fun AccountProfilePasswordVerificationPreview() {
    EumShoppingMallTheme {
        AccountProfileScreen(
            uiModel = AccountProfileEditUiModel(profile = profileGatePreview),
            isPasswordVerification = true,
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-05h 상품 가격 미표시")
@Composable
private fun ProductDetailNoPricePreview() {
    EumShoppingMallTheme {
        CatalogProductDetailScreen(
            CatalogProductDetailUiModel(product = noPriceProduct),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-05sh 옵션 가격 미표시")
@Composable
private fun ProductOptionsNoPricePreview() {
    EumShoppingMallTheme {
        CatalogProductDetailScreen(
            CatalogProductDetailUiModel(
                product = noPriceProduct,
                optionsSheetVisible = true,
                optionGroups = listOf(
                    CatalogOptionGroupUiModel(
                        id = "size",
                        label = "규격 선택",
                        options = listOf(
                            CatalogOptionUiModel("standard", "기본 규격", 1, null),
                            CatalogOptionUiModel("custom", "현장 맞춤 규격", 1, null),
                        ),
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-26r 탈퇴 사유")
@Composable
private fun AccountWithdrawalReasonPreview() {
    EumShoppingMallTheme {
        AccountWithdrawalReasonScreen(
            AccountWithdrawalReasonUiModel(
                selectedReasonIndex = 5,
                otherReason = "서비스 이용 종료",
                couponCount = 2,
                points = "1,000원",
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-07h2 주문 가능 상품 없음")
@Composable
private fun CartNoOrderableItemsPreview() {
    EumShoppingMallTheme {
        CartScreen(
            CartUiModel(
                items = listOf(
                    CartItemUiModel(
                        id = "preview-unavailable",
                        name = "현재 주문할 수 없는 상품",
                        optionLabel = "옵션: 기본",
                        quantity = 1,
                        unitPriceLabel = "₩10,000",
                        totalPriceLabel = "₩10,000",
                        isSelected = false,
                        isSoldOut = true,
                    ),
                ),
                productAmountLabel = "₩0",
                shippingFeeLabel = "₩0",
                expectedPaymentLabel = "₩0",
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-24e 배송지 추가")
@Composable
private fun AccountAddressAddPreview() {
    EumShoppingMallTheme {
        AccountAddressFormScreen(
            AccountAddressFormUiModel(
                address = AccountAddressUiModel(
                    id = "preview-new-address",
                    label = "",
                    recipient = "",
                    phoneNumber = "",
                    postalCode = "",
                    address = "",
                    addressDetail = "",
                    deliveryRequest = "",
                    isDefault = true,
                ),
                isEditMode = false,
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-35p 기기 알림 꺼짐")
@Composable
private fun AccountSettingsNotificationsDisabledPreview() {
    EumShoppingMallTheme {
        AccountSettingsScreen(
            AccountSettingUiModel(
                isLoggedIn = true,
                isDeviceNotificationEnabled = false,
                isAppMarketingEnabled = true,
                isSmsMarketingEnabled = false,
                isEmailMarketingEnabled = true,
                isNightMarketingEnabled = false,
                isDeviceNotificationWarningVisible = true,
                isMarketingToastVisible = false,
                isLogoutDialogVisible = false,
                appVersionLabel = "0.0.0 · 최신 버전",
                lastConsentChangedLabel = "0000.00.00",
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "A-35l 로그아웃 확인")
@Composable
private fun AccountSettingsLogoutDialogPreview() {
    EumShoppingMallTheme {
        AccountSettingsScreen(
            AccountSettingUiModel(
                isLoggedIn = true,
                isDeviceNotificationEnabled = true,
                isAppMarketingEnabled = true,
                isSmsMarketingEnabled = false,
                isEmailMarketingEnabled = true,
                isNightMarketingEnabled = false,
                isDeviceNotificationWarningVisible = false,
                isMarketingToastVisible = false,
                isLogoutDialogVisible = true,
                appVersionLabel = "0.0.0 · 최신 버전",
                lastConsentChangedLabel = "0000.00.00",
            ),
            onEvent = {},
        )
    }
}

private val requestHistoryByType = listOf(
    OrderRequestHistoryItemUiModel(
        id = "preview-cancel-request",
        requestDateLabel = "2026.11.08",
        requestType = OrderRequestType.Cancel,
        orderNumber = "ORDER-0001",
        productName = "안전모 제품명",
        priceAndQuantityLabel = "₩18,000 · 1개",
        status = OrderRequestStatus.Processing,
    ),
    OrderRequestHistoryItemUiModel(
        id = "preview-return-request",
        requestDateLabel = "2026.11.07",
        requestType = OrderRequestType.Return,
        orderNumber = "ORDER-0002",
        productName = "작업용 보호 장갑",
        priceAndQuantityLabel = "₩24,000 · 2개",
        status = OrderRequestStatus.Requested,
    ),
    OrderRequestHistoryItemUiModel(
        id = "preview-exchange-request",
        requestDateLabel = "2026.11.06",
        requestType = OrderRequestType.Exchange,
        orderNumber = "ORDER-0003",
        productName = "경량 안전화",
        priceAndQuantityLabel = "₩54,000 · 1개",
        status = OrderRequestStatus.Collecting,
    ),
)

@Composable
private fun RequestHistoryFilteredPreview(filter: OrderRequestHistoryFilter) {
    EumShoppingMallTheme {
        OrderRequestHistoryScreen(
            uiModel = OrderRequestHistoryUiModel(
                filter = filter,
                items = requestHistoryByType.filter { item ->
                    when (filter) {
                        OrderRequestHistoryFilter.All -> true
                        OrderRequestHistoryFilter.Cancel -> item.requestType == OrderRequestType.Cancel
                        OrderRequestHistoryFilter.Return -> item.requestType == OrderRequestType.Return
                        OrderRequestHistoryFilter.Exchange -> item.requestType == OrderRequestType.Exchange
                    }
                },
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19l 취소 필터")
@Composable
private fun RequestHistoryCancelFilterPreview() {
    RequestHistoryFilteredPreview(OrderRequestHistoryFilter.Cancel)
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19l 반품 필터")
@Composable
private fun RequestHistoryReturnFilterPreview() {
    RequestHistoryFilteredPreview(OrderRequestHistoryFilter.Return)
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19l 교환 필터")
@Composable
private fun RequestHistoryExchangeFilterPreview() {
    RequestHistoryFilteredPreview(OrderRequestHistoryFilter.Exchange)
}
