package com.nativelap.eumshoppingmall.ui.cart.viewmodel

/** 주문 가능 구역에 나열하는 상품. 품절 상품도 포함하며 품절 표시로 구분한다. */
internal fun List<CartItemUiModel>.orderableItems(): List<CartItemUiModel> = filter { item -> item.type == CartItemType.Orderable }

/**
 * 실제로 체크해 주문에 담을 수 있는 상품(주문 가능이면서 품절이 아님).
 * 상단 "주문 가능 상품 (N)" 집계와 전체 선택이 이 기준을 쓴다(M6).
 */
internal fun List<CartItemUiModel>.selectableItems(): List<CartItemUiModel> = orderableItems().filterNot { item -> item.isSoldOut }
