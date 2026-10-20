package com.nativelap.eumshoppingmall.ui.cart.viewmodel

import androidx.annotation.DrawableRes

enum class CartItemType {
    Orderable,
    QuoteRequired,
}

data class CartItemUiModel(
    val id: String,
    val name: String,
    val optionLabel: String,
    val quantity: Int,
    val unitPriceLabel: String,
    val totalPriceLabel: String,
    val type: CartItemType = CartItemType.Orderable,
    val isSelected: Boolean = false,
    val isSoldOut: Boolean = false,
    @param:DrawableRes val imageResId: Int? = null,
)

data class CartUiModel(
    val items: List<CartItemUiModel> = emptyList(),
    val productAmountLabel: String = "",
    val shippingFeeLabel: String = "",
    val expectedPaymentLabel: String = "",
)
