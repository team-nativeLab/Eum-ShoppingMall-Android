package com.nativelap.eumshoppingmall.ui.ordercomplete.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

@ResponsiveUiPreview
@Composable
private fun OrderCompleteScreenPreview() {
    EumShoppingMallTheme {
        OrderCompleteScreen(
            uiModel = OrderCompleteUiModel(
                orderNumber = "20260101-0001",
                paymentAmountLabel = "113,000원",
                paymentMethodLabel = "신용카드",
                addressLabel = "서울특별시 중구 세종대로 110 3층",
            ),
            onEvent = {},
        )
    }
}

// 주문번호·결제금액·결제수단·배송지가 모두 비었을 때: 상세 보기 비활성, 리소스 문구 대체.
@ResponsiveUiPreview
@Composable
private fun OrderCompleteEmptyValuesPreview() {
    EumShoppingMallTheme {
        OrderCompleteScreen(
            uiModel = OrderCompleteUiModel(
                orderNumber = "",
                paymentAmountLabel = "",
                paymentMethodLabel = "",
                addressLabel = "",
            ),
            onEvent = {},
        )
    }
}
