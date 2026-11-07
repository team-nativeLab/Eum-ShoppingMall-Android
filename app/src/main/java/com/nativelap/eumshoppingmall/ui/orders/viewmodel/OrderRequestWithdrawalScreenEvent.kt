package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderRequestWithdrawalScreenEvent {
    data object BackClicked : OrderRequestWithdrawalScreenEvent
    data object WithdrawalConfirmed : OrderRequestWithdrawalScreenEvent
    data object WithdrawalCancelled : OrderRequestWithdrawalScreenEvent
    data object RequestHistoryClicked : OrderRequestWithdrawalScreenEvent
}
