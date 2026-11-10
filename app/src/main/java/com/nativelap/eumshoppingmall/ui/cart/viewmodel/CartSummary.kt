package com.nativelap.eumshoppingmall.ui.cart.viewmodel

/**
 * 선택된 주문 가능 줄의 상품금액(원). 선택 줄이 없으면 0이다.
 * 선택된 줄 중 합계를 모르는 줄이 하나라도 있으면 null이다(가격 확인 필요).
 */
internal fun cartProductAmountWon(items: List<CartItemUiModel>): Long? = try {
    items
        .filter { item -> item.isSelected && item.type == CartItemType.Orderable && !item.isSoldOut }
        .fold(0L) { total, item -> Math.addExact(total, item.lineTotalWon ?: return null) }
} catch (exception: ArithmeticException) {
    null
}

/**
 * 선택된 주문 가능 줄의 금액 집계(N2). 확인된 금액 합과, 합계를 알 수 있었던 줄 수와 모르는 줄 수를 따로 센다.
 * 선택 줄이 없으면 확인된 금액 0, 두 줄 수 모두 0이다.
 */
internal data class CartAmountSummary(
    val confirmedWon: Long,
    val knownLineCount: Int,
    val unknownLineCount: Int,
)

internal fun cartAmountSummary(items: List<CartItemUiModel>): CartAmountSummary {
    var confirmedWon = 0L
    var knownLineCount = 0
    var unknownLineCount = 0
    items
        .filter { item -> item.isSelected && item.type == CartItemType.Orderable && !item.isSoldOut }
        .forEach { item ->
            val lineTotalWon = item.lineTotalWon
            val nextTotal = lineTotalWon?.let { amount -> runCatching { Math.addExact(confirmedWon, amount) }.getOrNull() }
            if (nextTotal != null) {
                confirmedWon = nextTotal
                knownLineCount += 1
            } else {
                // 합계를 모르거나 더하다 넘치는 줄은 모르는 줄로 센다.
                unknownLineCount += 1
            }
        }
    return CartAmountSummary(confirmedWon, knownLineCount, unknownLineCount)
}

/**
 * 요약 금액 문구. 모르는 줄이 없으면 확인 금액, 확인 금액이 전혀 없으면 [priceCheck],
 * 둘이 섞이면 [partialTemplate](확인 금액, 모르는 줄 수) 형식이다.
 */
internal fun cartAmountLabel(
    summary: CartAmountSummary,
    priceCheck: String,
    partialTemplate: String,
    formatWon: (Long) -> String,
): String = when {
    summary.unknownLineCount == 0 -> formatWon(summary.confirmedWon)
    summary.knownLineCount == 0 -> priceCheck
    else -> String.format(partialTemplate, formatWon(summary.confirmedWon), summary.unknownLineCount)
}

/**
 * 결제 예정 금액 줄 문구. 상품 금액을 모르면 [priceCheck], 상품 금액은 알지만 배송비를 모르면 [shippingAtOrder],
 * 둘 다 알면 합계 금액이다. 합계가 Long 범위를 넘으면 [priceCheck]다.
 */
internal fun cartExpectedPaymentText(
    productWon: Long?,
    shippingWon: Long?,
    priceCheck: String,
    shippingAtOrder: String,
    formatWon: (Long) -> String,
): String {
    if (productWon == null) return priceCheck
    if (shippingWon == null) return shippingAtOrder
    return try {
        formatWon(Math.addExact(productWon, shippingWon))
    } catch (exception: ArithmeticException) {
        priceCheck
    }
}
