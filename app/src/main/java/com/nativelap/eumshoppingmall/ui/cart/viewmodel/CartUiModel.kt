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
    // 단가(원, 추가금 포함). 수량을 바꿀 때 줄 금액과 문구를 다시 계산하는 데 쓴다. 모르면 null이다(N2).
    val unitPriceWon: Long? = null,
    // 저장된 장바구니 줄에서 만든 항목이면 true. 샘플(폴백) 줄은 저장 줄이 없어 옵션 변경 대상이 아니다(S2).
    val isSavedLine: Boolean = false,
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
