package com.nativelap.eumshoppingmall.ui.cart.viewmodel

import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.catalog.formatWonLabel

/** 저장 줄 화면 문구. 가격 미상은 "가격 확인 필요", 견적 상품은 "가격 문의"를 쓴다. */
internal data class CartPriceLabels(
    val priceCheck: String,
    val quotePrice: String,
)

/**
 * 저장된 장바구니 줄을 화면 모델로 바꾼다. 저장된 줄이 없을 때만 [fallback]을 쓴다.
 * 주문 가능한 줄은 기본 선택(isSelected=true)으로 둔다. 재진입하면 다시 이 기본값이 적용된다.
 */
internal fun cartUiModelOf(
    savedLines: List<CartLine>,
    labels: CartPriceLabels,
    fallback: () -> CartUiModel,
): CartUiModel = if (savedLines.isEmpty()) {
    fallback()
} else {
    CartUiModel(items = savedLines.map { line -> line.toCartItemUiModel(labels) })
}

/** 저장 줄의 항목 id. 편집(수량·삭제)이 같은 줄을 찾을 때 쓰는 값과 같다. */
internal fun CartLine.cartItemId(): String = lineId()

internal fun CartLine.toCartItemUiModel(labels: CartPriceLabels): CartItemUiModel {
    val extraWon = optionExtraWon?.takeIf { amount -> amount != 0L }
    val optionText = if (extraWon == null) optionLabel else "$optionLabel (+${formatWonLabel(extraWon)})"
    if (isQuoteRequired) {
        return CartItemUiModel(
            id = cartItemId(),
            name = productName,
            optionLabel = optionText,
            quantity = quantity,
            unitPriceLabel = labels.quotePrice,
            totalPriceLabel = labels.quotePrice,
            type = CartItemType.QuoteRequired,
        )
    }
    val unitWon = unitPriceWonOrNull()
    val lineTotalWon = unitWon?.let { unit -> multiplyOrNull(unit, quantity.toLong()) }
    val unitLabel = unitWon?.let(::formatWonLabel) ?: labels.priceCheck
    // 수량은 단가 쪽에 붙이고, 줄 가격 칸에는 줄 합계를 둔다(M3). 합계를 모르면 가격 확인 필요다.
    val unitDisplay = if (unitWon != null && quantity > 1) "$unitLabel × $quantity" else unitLabel
    val totalLabel = lineTotalWon?.let(::formatWonLabel) ?: labels.priceCheck
    return CartItemUiModel(
        id = cartItemId(),
        name = productName,
        optionLabel = optionText,
        quantity = quantity,
        unitPriceLabel = unitDisplay,
        totalPriceLabel = totalLabel,
        type = CartItemType.Orderable,
        isSelected = true,
        lineTotalWon = lineTotalWon,
    )
}

// 단가 = 상품 기본가 + 옵션 추가금. 둘 중 하나라도 모르면 단가는 미상이다.
private fun CartLine.unitPriceWonOrNull(): Long? {
    val basePrice = productUnitPriceWon ?: return null
    val extra = optionExtraWon ?: return null
    return try {
        Math.addExact(basePrice, extra)
    } catch (exception: ArithmeticException) {
        null
    }
}

private fun multiplyOrNull(unit: Long, quantity: Long): Long? = try {
    Math.multiplyExact(unit, quantity)
} catch (exception: ArithmeticException) {
    null
}
