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

/**
 * 옵션 변경 버튼 노출과 클릭 처리가 함께 쓰는 판정.
 * 저장된 줄만 옵션 편집 대상이다(샘플 폴백 줄 제외, S2). 견적 전용 줄도 편집 대상이 아니다.
 */
internal fun CartItemUiModel.canChangeOption(): Boolean = isSavedLine && type != CartItemType.QuoteRequired

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
            isSavedLine = true,
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
        unitPriceWon = unitWon,
        isSavedLine = true,
    )
}

/**
 * 고정 샘플 줄의 수량을 바꾼 뒤 줄 금액과 표시 문구를 다시 계산한다(N2).
 * 단가를 모르면 수량만 바꾸고 금액은 "가격 확인 필요"로 둔다. 저장된 줄은 CartLine에서 다시 매핑되므로 이 함수를 거치지 않는다.
 */
internal fun CartItemUiModel.withQuantity(newQuantity: Int, labels: CartPriceLabels): CartItemUiModel {
    val unitWon = unitPriceWon ?: return copy(quantity = newQuantity)
    val lineTotalWon = multiplyOrNull(unitWon, newQuantity.toLong())
    val unitLabel = formatWonLabel(unitWon)
    return copy(
        quantity = newQuantity,
        unitPriceLabel = if (newQuantity > 1) "$unitLabel × $newQuantity" else unitLabel,
        totalPriceLabel = lineTotalWon?.let(::formatWonLabel) ?: labels.priceCheck,
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
