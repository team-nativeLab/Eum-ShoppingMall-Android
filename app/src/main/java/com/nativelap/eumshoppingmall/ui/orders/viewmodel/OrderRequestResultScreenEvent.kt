package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderRequestResultScreenEvent {
    data object BackClicked : OrderRequestResultScreenEvent
    data object RequestHistoryClicked : OrderRequestResultScreenEvent
}
