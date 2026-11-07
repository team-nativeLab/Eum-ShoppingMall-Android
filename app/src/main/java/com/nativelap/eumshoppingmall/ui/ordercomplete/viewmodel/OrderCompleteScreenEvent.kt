package com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel

sealed interface OrderCompleteScreenEvent {
    data object ContinueShoppingClicked : OrderCompleteScreenEvent
    data object ViewOrderDetailsClicked : OrderCompleteScreenEvent
    data object CopyAccountNumberClicked : OrderCompleteScreenEvent
    data object CloseClicked : OrderCompleteScreenEvent
}
