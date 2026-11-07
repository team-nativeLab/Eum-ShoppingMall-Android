package com.nativelap.eumshoppingmall.ui.orders.viewmodel

enum class DeliveryProgressStage {
    PaymentComplete,
    ProductPreparing,
    ShippingStarted,
    InTransit,
    Delivered,
}

data class DeliveryTimelineItemUiModel(
    val title: String,
    val locationAndTimeLabel: String,
    val isCurrent: Boolean = false,
)

data class DeliveryTrackingUiModel(
    val status: OrderStatus,
    val expectedArrivalLabel: String,
    val carrierName: String,
    val trackingNumber: String,
    val progressStage: DeliveryProgressStage,
    val timeline: List<DeliveryTimelineItemUiModel> = emptyList(),
    val productsSummaryLabel: String,
    val noticeLabel: String,
)
