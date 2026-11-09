package com.nativelap.eumshoppingmall.ui.ordercomplete.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteScreenEvent
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel

@Composable
fun OrderCompleteRoute(
    uiModel: OrderCompleteUiModel,
    onContinueShoppingClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    OrderCompleteScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                OrderCompleteScreenEvent.ContinueShoppingClicked -> onContinueShoppingClick()

                OrderCompleteScreenEvent.CloseClicked -> onCloseClick()

                // 주문 상세(A-17)와 계좌 복사는 이번 범위가 아니다.
                OrderCompleteScreenEvent.ViewOrderDetailsClicked,
                OrderCompleteScreenEvent.CopyAccountNumberClicked,
                -> Unit
            }
        },
    )
}
