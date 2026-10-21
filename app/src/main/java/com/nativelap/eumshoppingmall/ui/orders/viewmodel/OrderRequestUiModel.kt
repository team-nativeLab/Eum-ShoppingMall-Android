package com.nativelap.eumshoppingmall.ui.orders.viewmodel

import androidx.annotation.DrawableRes

enum class OrderRequestType {
    Cancel,
    Return,
    Exchange,
}

enum class OrderRequestReason {
    ChangedMind,
    ProductDefective,
    WrongProduct,
    DeliveryDelayed,
    Other,
}

enum class OrderRequestStatus {
    Requested,
    Collecting,
    Processing,
    Completed,
    Rejected,
    Withdrawn,
}

enum class OrderRequestHistoryFilter {
    All,
    Cancel,
    Return,
    Exchange,
}

enum class OrderRequestCollectionMethod {
    CourierPickup,
    SelfShipment,
}

data class OrderRequestProductUiModel(
    val id: String,
    val name: String,
    val optionLabel: String,
    val priceAndQuantityLabel: String,
    val quantityLabel: String,
    val isSelected: Boolean,
    val isSelectable: Boolean = true,
    @param:DrawableRes val imageResId: Int? = null,
)

data class RequestableOrderUiModel(
    val id: String,
    val orderDateLabel: String,
    val orderNumber: String,
    val status: OrderStatus,
    val products: List<OrderRequestProductUiModel>,
    val canSelectForRequest: Boolean,
)

data class OrderRequestOrderSelectionUiModel(
    val filter: OrderHistoryFilter = OrderHistoryFilter.All,
    val periods: List<String> = emptyList(),
    val periodLabel: String,
    val isPeriodPickerVisible: Boolean = false,
    val orders: List<RequestableOrderUiModel> = emptyList(),
    val emptyState: OrderHistoryEmptyState? = null,
)

data class OrderRequestFormUiModel(
    val type: OrderRequestType,
    val products: List<OrderRequestProductUiModel>,
    val reasons: List<OrderRequestReason>,
    val selectedReason: OrderRequestReason? = null,
    val reasonDetails: String = "",
    val photoCount: Int = 0,
    val maxPhotoCount: Int = 3,
    val collectionMethod: OrderRequestCollectionMethod = OrderRequestCollectionMethod.CourierPickup,
    val collectionAddressLabel: String = "",
    val collectionContactLabel: String = "",
    val exchangeOptionLabel: String = "",
    val exchangeShippingLabel: String = "",
    val productAmountLabel: String,
    val feeLabel: String,
    val expectedAmountLabel: String,
    val paymentMethodLabel: String,
    val canSubmit: Boolean = false,
)

data class OrderRequestHistoryItemUiModel(
    val id: String,
    val requestDateLabel: String,
    val requestType: OrderRequestType,
    val orderNumber: String,
    val productName: String,
    val priceAndQuantityLabel: String,
    val status: OrderRequestStatus,
    val expectedAmountLabel: String? = null,
    val canWithdraw: Boolean = false,
    val hasRejectionReason: Boolean = false,
    @param:DrawableRes val imageResId: Int? = null,
)

data class OrderRequestHistoryUiModel(
    val filter: OrderRequestHistoryFilter = OrderRequestHistoryFilter.All,
    val items: List<OrderRequestHistoryItemUiModel> = emptyList(),
    val hasNoRequests: Boolean = false,
)

data class OrderRequestDetailUiModel(
    val requestNumber: String,
    val requestType: OrderRequestType,
    val requestDateLabel: String,
    val status: OrderRequestStatus,
    val statusDescription: String,
    val productSummaryLabel: String,
    val reasonSummaryLabel: String,
    val collectionAddressLabel: String,
    val canWithdraw: Boolean,
    val rejectionReasonLabel: String? = null,
)

data class OrderRequestResultUiModel(
    val requestNumber: String,
    val statusLabel: String,
    val statusDescription: String,
)

data class OrderRequestRejectionUiModel(
    val requestNumber: String,
    val productSummaryLabel: String,
    val requestReasonLabel: String,
    val statusDescription: String,
    val rejectionReasonLabel: String,
)
