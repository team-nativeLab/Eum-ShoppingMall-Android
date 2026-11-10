package com.nativelap.eumshoppingmall.ui.catalog

import java.util.Locale

/**
 * 선택 옵션의 추가금 합계(원). 상품 기본가는 빼고 옵션 추가금만 더한다.
 * 옵션이 없거나, 추가금을 모르는 옵션이 있거나, 합계가 0원이면 null이라 호출부는 합계 줄을 숨긴다.
 * 곱셈·덧셈이 Long 범위를 넘으면 합계를 만들지 않고 null을 돌려준다.
 */
internal fun calculateSelectedOptionTotalWon(selectedOptions: List<CatalogSelectedOptionUiModel>): Long? {
    if (selectedOptions.isEmpty()) return null
    return try {
        var extraTotalWon = 0L
        for (selected in selectedOptions) {
            val extraPerUnitWon = selected.option.unitPriceWon ?: return null
            val quantity = selected.option.quantity.coerceAtLeast(1).toLong()
            extraTotalWon = Math.addExact(extraTotalWon, Math.multiplyExact(extraPerUnitWon, quantity))
        }
        extraTotalWon.takeIf { amount -> amount != 0L }
    } catch (exception: ArithmeticException) {
        null
    }
}

/** 원화 표시 문자열. 예: 18000 → "₩18,000". */
internal fun formatWonLabel(amountWon: Long): String = String.format(Locale.KOREA, "₩%,d", amountWon)
