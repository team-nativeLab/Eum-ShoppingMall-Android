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
    onUnavailableAction: () -> Unit,
) {
    OrderCompleteScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                OrderCompleteScreenEvent.ContinueShoppingClicked -> onContinueShoppingClick()

                OrderCompleteScreenEvent.CloseClicked -> onCloseClick()

                OrderCompleteScreenEvent.ViewOrderDetailsClicked -> onViewOrderDetailsClick()

                OrderCompleteScreenEvent.CopyAccountNumberClicked -> onUnavailableAction()
            }
        },
    )
}
