package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderRequestHistoryScreenEvent {
    data object BackClicked : OrderRequestHistoryScreenEvent
    data class FilterSelected(val filter: OrderRequestHistoryFilter) : OrderRequestHistoryScreenEvent
    data class RequestDetailsClicked(val requestId: String) : OrderRequestHistoryScreenEvent
    data class WithdrawalClicked(val requestId: String) : OrderRequestHistoryScreenEvent
    data class RejectionReasonClicked(val requestId: String) : OrderRequestHistoryScreenEvent
    data object OrdersClicked : OrderRequestHistoryScreenEvent
}
