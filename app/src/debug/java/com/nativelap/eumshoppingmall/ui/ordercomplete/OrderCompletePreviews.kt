package com.nativelap.eumshoppingmall.ui.ordercomplete

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.ordercomplete.view.OrderCompleteScreen
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "주문 완료")
@Composable
private fun OrderCompletePreview() {
    EumShoppingMallTheme {
        OrderCompleteScreen(
            OrderCompleteUiModel("0000000000", "₩72,000", "신용카드", "서울특별시 00구 00로 00"),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 972, name = "가상계좌 주문 완료")
@Composable
private fun VirtualAccountOrderCompletePreview() {
    EumShoppingMallTheme {
        OrderCompleteScreen(
            OrderCompleteUiModel(
                orderNumber = "0000000000",
                paymentAmountLabel = "₩72,000",
                paymentMethodLabel = "가상계좌",
                addressLabel = "서울특별시 00구 00로 00",
                isVirtualAccount = true,
                bankLabel = "00은행",
                accountNumber = "000-000000-00000",
                depositorLabel = "예금주명",
                depositAmountLabel = "₩72,000",
                depositDeadlineLabel = "0000.00.00 00:00까지",
            ),
            onEvent = {},
        )
    }
}
