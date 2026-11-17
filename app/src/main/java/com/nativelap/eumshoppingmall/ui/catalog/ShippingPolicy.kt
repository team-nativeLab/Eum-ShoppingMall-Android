package com.nativelap.eumshoppingmall.ui.catalog

/** 무료배송 기준 금액(원). 홈 서비스 띠의 기준 문구(home_free_shipping_threshold)와 같은 금액이다. */
internal const val FREE_SHIPPING_THRESHOLD_WON = 50_000L

/** 기준 미만일 때 적용하는 기본 배송비(원). */
internal const val STANDARD_SHIPPING_FEE_WON = 3_000L

/**
 * 상품 목록 칩·상품 상세 배송 문구. 상품 판매가가 기준 이상이면 "무료배송", 미만이면 "배송비 ₩3,000"이다.
 * 판매가를 모르면 null이다.
 */
internal fun productShippingFeeLabel(unitPriceWon: Long?): String? = unitPriceWon?.let { price ->
    if (price >= FREE_SHIPPING_THRESHOLD_WON) "무료배송" else "배송비 ${formatWonLabel(STANDARD_SHIPPING_FEE_WON)}"
}

/** 상품 상세 배송비 행의 값. 기준 이상이면 "무료", 미만이면 기본 배송비 금액이다. */
internal fun productShippingFeeValue(unitPriceWon: Long?): String? = unitPriceWon?.let { price ->
    if (price >= FREE_SHIPPING_THRESHOLD_WON) "무료" else formatWonLabel(STANDARD_SHIPPING_FEE_WON)
}

/**
 * 장바구니 배송비. 선택한 상품금액 합계 기준으로 판정한다.
 * 그래서 상품별 칩의 기준과 달라도 된다(합계가 기준 이상이면 상품 하나가 기준 미만이어도 무료).
 */
internal fun cartShippingFeeWon(productTotalWon: Long): Long = if (productTotalWon >= FREE_SHIPPING_THRESHOLD_WON) 0L else STANDARD_SHIPPING_FEE_WON
