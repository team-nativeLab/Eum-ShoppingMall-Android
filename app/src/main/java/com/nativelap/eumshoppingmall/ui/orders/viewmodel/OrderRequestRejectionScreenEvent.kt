package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderRequestRejectionScreenEvent {
    data object BackClicked : OrderRequestRejectionScreenEvent
    data object RequestHistoryClicked : OrderRequestRejectionScreenEvent
}
