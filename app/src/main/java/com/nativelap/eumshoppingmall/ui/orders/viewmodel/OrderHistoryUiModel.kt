package com.nativelap.eumshoppingmall.ui.orders.viewmodel

import androidx.annotation.DrawableRes

enum class OrderHistoryFilter {
    All,
    WaitingPayment,
    PaymentComplete,
    Preparing,
    Shipping,
    Delivered,
}

enum class OrderStatus {
    WaitingPayment,
    PaymentComplete,
    Preparing,
    Shipping,
    Delivered,
}

enum class OrderHistoryEmptyState {
    NoOrders,
    NoMatchingOrders,
}

enum class OrderHistoryAction {
    Cancel,
    TrackDelivery,
    BuyAgain,
}

data class OrderHistoryProductUiModel(
    val name: String,
    val priceAndQuantityLabel: String,
    val productCountLabel: String? = null,
    @param:DrawableRes val imageResId: Int? = null,
)

data class OrderHistoryItemUiModel(
    val id: String,
    val orderDateLabel: String,
    val orderNumber: String,
    val status: OrderStatus,
    val product: OrderHistoryProductUiModel,
    val paymentDeadlineLabel: String? = null,
    val actions: List<OrderHistoryAction> = emptyList(),
)

data class OrderHistoryUiModel(
    val filter: OrderHistoryFilter = OrderHistoryFilter.All,
    val periodLabel: String,
    val periods: List<String> = emptyList(),
    val isPeriodPickerVisible: Boolean = false,
    val orders: List<OrderHistoryItemUiModel> = emptyList(),
    val emptyState: OrderHistoryEmptyState? = null,
)
