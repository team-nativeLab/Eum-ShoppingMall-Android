package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderRequestOrderSelectionScreenEvent {
    data object BackClicked : OrderRequestOrderSelectionScreenEvent
    data class FilterSelected(val filter: OrderHistoryFilter) : OrderRequestOrderSelectionScreenEvent
    data object PeriodPickerToggled : OrderRequestOrderSelectionScreenEvent
    data object FiltersReset : OrderRequestOrderSelectionScreenEvent
    data class PeriodSelected(val period: String) : OrderRequestOrderSelectionScreenEvent
    data class OrderDetailsClicked(val orderId: String) : OrderRequestOrderSelectionScreenEvent
    data class OrderRequestSelected(val orderId: String) : OrderRequestOrderSelectionScreenEvent
    data class OrderDeliveryClicked(val orderId: String) : OrderRequestOrderSelectionScreenEvent
}
