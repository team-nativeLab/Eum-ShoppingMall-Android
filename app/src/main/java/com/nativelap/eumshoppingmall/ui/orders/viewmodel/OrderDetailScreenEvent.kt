package com.nativelap.eumshoppingmall.ui.orders.viewmodel

sealed interface OrderDetailScreenEvent {
    data object BackClicked : OrderDetailScreenEvent
    data class ProductsExpandedChanged(val isExpanded: Boolean) : OrderDetailScreenEvent
    data object DeliveryTrackingClicked : OrderDetailScreenEvent
    data object ExchangeReturnRequestClicked : OrderDetailScreenEvent
    data object StatementDownloadClicked : OrderDetailScreenEvent
    data object ReceiptViewClicked : OrderDetailScreenEvent
}
