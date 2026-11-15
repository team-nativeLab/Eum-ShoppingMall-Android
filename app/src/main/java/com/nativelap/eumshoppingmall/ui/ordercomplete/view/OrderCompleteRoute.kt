package com.nativelap.eumshoppingmall.ui.ordercomplete.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteScreenEvent
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel

@Composable
fun OrderCompleteRoute(
    uiModel: OrderCompleteUiModel,
    onContinueShoppingClick: () -> Unit,
    onCloseClick: () -> Unit,
    onViewOrderDetailsClick: () -> Unit = {},
) {
    OrderCompleteScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                OrderCompleteScreenEvent.ContinueShoppingClicked -> onContinueShoppingClick()

                OrderCompleteScreenEvent.CloseClicked -> onCloseClick()

                OrderCompleteScreenEvent.ViewOrderDetailsClicked -> onViewOrderDetailsClick()

                // 계좌 복사는 클립보드 연동 전이라 연결하지 않는다.
                OrderCompleteScreenEvent.CopyAccountNumberClicked -> Unit
            }
        },
    )
}
