package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderRequestFormScreenEvent {
    data object BackClicked : OrderRequestFormScreenEvent
    data class TypeSelected(val type: OrderRequestType) : OrderRequestFormScreenEvent
    data class ProductSelectionChanged(val productId: String, val isSelected: Boolean) : OrderRequestFormScreenEvent
    data class ProductQuantityChanged(val productId: String, val quantityDelta: Int) : OrderRequestFormScreenEvent
    data class ReasonSelected(val reason: OrderRequestReason) : OrderRequestFormScreenEvent
    data class ReasonDetailsChanged(val value: String) : OrderRequestFormScreenEvent
    data object PhotoAttachmentClicked : OrderRequestFormScreenEvent
    data class CollectionMethodSelected(val method: OrderRequestCollectionMethod) : OrderRequestFormScreenEvent
    data object CollectionAddressChangeClicked : OrderRequestFormScreenEvent
    data object ExchangeOptionClicked : OrderRequestFormScreenEvent
    data object SubmitClicked : OrderRequestFormScreenEvent
}
