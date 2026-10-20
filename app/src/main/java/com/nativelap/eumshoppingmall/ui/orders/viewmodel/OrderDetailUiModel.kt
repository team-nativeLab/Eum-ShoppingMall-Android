package com.nativelap.eumshoppingmall.ui.orders.viewmodel

import androidx.annotation.DrawableRes

data class OrderDetailProductUiModel(
    val id: String,
    val name: String,
    val optionLabel: String,
    val priceAndQuantityLabel: String,
    val status: OrderStatus,
    @param:DrawableRes val imageResId: Int? = null,
)

data class OrderDetailAddressUiModel(
    val recipientName: String,
    val contactLabel: String,
    val addressLabel: String,
    val requestLabel: String,
)

data class OrderPaymentBreakdownUiModel(
    val productAmountLabel: String,
    val shippingFeeLabel: String,
    val couponDiscountLabel: String,
    val pointsUsedLabel: String,
    val totalAmountLabel: String,
    val paymentMethodLabel: String,
)

data class OrderDetailUiModel(
    val orderDateLabel: String,
    val orderNumber: String,
    val products: List<OrderDetailProductUiModel>,
    val address: OrderDetailAddressUiModel,
    val payment: OrderPaymentBreakdownUiModel,
    val expectedPointsLabel: String,
    val isProductsExpanded: Boolean = true,
    val canTrackDelivery: Boolean = true,
    val canRequestExchangeOrReturn: Boolean = true,
)
