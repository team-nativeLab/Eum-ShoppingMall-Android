package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderRequestHubScreenEvent {
    data object BackClicked : OrderRequestHubScreenEvent
    data object OrdersClicked : OrderRequestHubScreenEvent
    data object RequestHistoryClicked : OrderRequestHubScreenEvent
    data object StartRequestClicked : OrderRequestHubScreenEvent
}
