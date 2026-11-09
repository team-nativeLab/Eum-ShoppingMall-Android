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
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogKeyValueUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionGroupUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailScreen
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductDetailUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogSelectedOptionUiModel
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
    name = "제품명",
    subtitle = "브랜드명",
    price = CatalogPriceUiModel.QuoteRequired,
    badgeLabel = "견적 상품",
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
            CatalogProductDetailUiModel(
                product = noPriceProduct,
                galleryImageResIds = List(5) { null },
                shippingRows = listOf(
                    CatalogKeyValueUiModel("배송", "일반 배송 (2~3일) · 무료"),
                    CatalogKeyValueUiModel("인증", "KC 인증 0000-0000"),
                    CatalogKeyValueUiModel("제조사", "제조사명"),
                ),
                specifications = listOf(
                    CatalogKeyValueUiModel("모델명", "모델명"),
                    CatalogKeyValueUiModel("소재", "소재 정보"),
                    CatalogKeyValueUiModel("사이즈", "S / M / L / XL"),
                    CatalogKeyValueUiModel("안전인증", "KC 인증 0000-0000"),
                    CatalogKeyValueUiModel("원산지", "원산지"),
                    CatalogKeyValueUiModel("제조사", "제조사명"),
                ),
                returnRows = listOf(
                    CatalogKeyValueUiModel("교환·반품", "수령 후 0일 이내 신청"),
                    CatalogKeyValueUiModel("반품 배송비", "편도 ₩0"),
                ),
                relatedProducts = listOf(
                    CatalogProductUiModel(id = "quote-related-1", name = "제품명", price = CatalogPriceUiModel.Available("₩00,000")),
                    CatalogProductUiModel(id = "quote-related-2", name = "제품명", price = CatalogPriceUiModel.Available("₩00,000")),
                    CatalogProductUiModel(id = "quote-related-3", name = "제품명", price = CatalogPriceUiModel.Available("₩00,000")),
                ),
            ),
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
                        label = "사이즈",
                        options = listOf(
                            CatalogOptionUiModel("size-l", "사이즈 L", 1, null),
                            CatalogOptionUiModel("size-xl", "사이즈 XL", 1, null),
                        ),
                    ),
                ),
                selectedOptions = listOf(
                    CatalogSelectedOptionUiModel("size", CatalogOptionUiModel("size-l", "사이즈 L", 1, null)),
                    CatalogSelectedOptionUiModel("size", CatalogOptionUiModel("size-xl", "사이즈 XL", 1, null)),
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
                otherReason = "입력한 내용",
                couponCount = 2,
                points = "1,000원",
                password = "12345678".toCharArray(),
                isSubmitEnabled = true,
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
                        id = "preview-quote-1",
                        name = "코팅 장갑 10켤레",
                        optionLabel = "옵션: 사이즈 L",
                        quantity = 1,
                        unitPriceLabel = "",
                        totalPriceLabel = "",
                        type = CartItemType.QuoteRequired,
                    ),
                    CartItemUiModel(
                        id = "preview-quote-2",
                        name = "방진 마스크 필터 세트",
                        optionLabel = "옵션: 사이즈 L",
                        quantity = 1,
                        unitPriceLabel = "",
                        totalPriceLabel = "",
                        type = CartItemType.QuoteRequired,
                    ),
                ),
                productAmountLabel = "₩0",
                shippingFeeLabel = "₩0",
                expectedPaymentLabel = "₩0",
            ),
            onEvent = {},
            isLoggedIn = false,
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
                    recipient = "홍길동",
                    phoneNumber = "010-0000",
                    postalCode = "",
                    address = "주소",
                    addressDetail = "",
                    deliveryRequest = "",
                    isDefault = false,
                ),
                isEditMode = false,
                isPhoneInvalid = true,
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

// Figma 필터 프레임(927:45466, 927:45597, 927:45728)의 목록. 필터마다 Figma에 보이는 카드만 둔다.
private val requestHistoryCancelItems = listOf(
    OrderRequestHistoryItemUiModel(
        id = "preview-cancel-rejected",
        requestDateLabel = "0000.00.00",
        requestType = OrderRequestType.Cancel,
        orderNumber = "0000000000",
        productName = "제품명",
        priceAndQuantityLabel = "₩00,000 · 1개",
        status = OrderRequestStatus.Rejected,
        hasRejectionReason = true,
    ),
)

private val requestHistoryReturnItems = listOf(
    OrderRequestHistoryItemUiModel(
        id = "preview-return-requested",
        requestDateLabel = "0000.00.00",
        requestType = OrderRequestType.Return,
        orderNumber = "0000000000",
        productName = "제품명",
        priceAndQuantityLabel = "₩00,000 · 1개",
        status = OrderRequestStatus.Requested,
        expectedAmountLabel = "₩00,000",
        canWithdraw = true,
    ),
    OrderRequestHistoryItemUiModel(
        id = "preview-return-collecting",
        requestDateLabel = "0000.00.00",
        requestType = OrderRequestType.Return,
        orderNumber = "0000000000",
        productName = "제품명",
        priceAndQuantityLabel = "₩00,000 · 1개",
        status = OrderRequestStatus.Collecting,
        expectedAmountLabel = "₩00,000",
    ),
)

private val requestHistoryExchangeItems = listOf(
    OrderRequestHistoryItemUiModel(
        id = "preview-exchange-completed",
        requestDateLabel = "0000.00.00",
        requestType = OrderRequestType.Exchange,
        orderNumber = "0000000000",
        productName = "제품명",
        priceAndQuantityLabel = "₩00,000 · 1개",
        status = OrderRequestStatus.Completed,
    ),
)

@Composable
private fun RequestHistoryFilteredPreview(filter: OrderRequestHistoryFilter, items: List<OrderRequestHistoryItemUiModel>) {
    EumShoppingMallTheme {
        OrderRequestHistoryScreen(
            uiModel = OrderRequestHistoryUiModel(filter = filter, items = items),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19l 취소 필터")
@Composable
private fun RequestHistoryCancelFilterPreview() {
    RequestHistoryFilteredPreview(OrderRequestHistoryFilter.Cancel, requestHistoryCancelItems)
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19l 반품 필터")
@Composable
private fun RequestHistoryReturnFilterPreview() {
    RequestHistoryFilteredPreview(OrderRequestHistoryFilter.Return, requestHistoryReturnItems)
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "A-19l 교환 필터")
@Composable
private fun RequestHistoryExchangeFilterPreview() {
    RequestHistoryFilteredPreview(OrderRequestHistoryFilter.Exchange, requestHistoryExchangeItems)
}
