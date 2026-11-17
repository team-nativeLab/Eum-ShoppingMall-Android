package com.nativelap.eumshoppingmall.ui.cart.viewmodel

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

/**
 * 샘플(폴백) 장바구니 줄의 수량 변경을 탭 전환·회전에서도 남기는 저장 형식(F2).
 * 저장된 실제 줄은 저장소(DataStore)가 이미 보관하므로 이 형식을 쓰지 않는다.
 */
internal val FallbackQuantitySaver: Saver<Map<String, Int>, Any> = listSaver(
    save = { quantities -> quantities.flatMap { (id, quantity) -> listOf(id, quantity) } },
    restore = { saved -> saved.chunked(2).associate { pair -> pair[0] as String to pair[1] as Int } },
)

/** 샘플 줄에 저장된 수량을 적용한다. 저장 수량이 없는 줄은 그대로 둔다. 금액은 수량에 맞춰 다시 계산한다. */
internal fun List<CartItemUiModel>.withFallbackQuantities(quantities: Map<String, Int>, labels: CartPriceLabels): List<CartItemUiModel> = map { item -> quantities[item.id]?.let { quantity -> item.withQuantity(quantity, labels) } ?: item }
