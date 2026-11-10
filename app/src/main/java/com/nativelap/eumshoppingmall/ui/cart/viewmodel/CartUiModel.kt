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
    // 이 줄의 합계(원). (단가+추가금)×수량. 모르면 null이고 요약은 "가격 확인 필요"로 둔다.
    val lineTotalWon: Long? = null,
)

data class CartUiModel(
    val items: List<CartItemUiModel> = emptyList(),
    val productAmountLabel: String = "",
    val shippingFeeLabel: String = "",
    val expectedPaymentLabel: String = "",
    // 배송비(원). 0이면 무료, null이면 미상이다.
    val shippingFeeWon: Long? = null,
    // 결제 예정 금액(원). null이면 미상이라 주문 버튼 문구에 금액을 넣지 않는다.
    val expectedPaymentWon: Long? = null,
)
