package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderRequestDetailScreenEvent {
    data object BackClicked : OrderRequestDetailScreenEvent
    data object WithdrawalClicked : OrderRequestDetailScreenEvent
    data object RejectionReasonClicked : OrderRequestDetailScreenEvent
    data object RequestHistoryClicked : OrderRequestDetailScreenEvent
}
