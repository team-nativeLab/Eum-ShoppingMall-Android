package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderHistoryScreenEvent {
    data object BackClicked : OrderHistoryScreenEvent
    data object CancelReturnClicked : OrderHistoryScreenEvent
    data class FilterSelected(val filter: OrderHistoryFilter) : OrderHistoryScreenEvent
    data object FiltersReset : OrderHistoryScreenEvent
    data object PeriodPickerToggled : OrderHistoryScreenEvent
    data class PeriodSelected(val period: String) : OrderHistoryScreenEvent
    data class OrderDetailsClicked(val orderId: String) : OrderHistoryScreenEvent
    data class ActionClicked(val orderId: String, val action: OrderHistoryAction) : OrderHistoryScreenEvent
    data object BrowseProductsClicked : OrderHistoryScreenEvent
}
