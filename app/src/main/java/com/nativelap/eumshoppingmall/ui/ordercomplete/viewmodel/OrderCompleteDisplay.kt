package com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel

/**
 * 화면에 보여 줄 값이면 그대로 돌려주고, 비었거나 공백만 있으면 null을 돌려준다.
 * null이면 Screen이 리소스 문구(정보 없음, 주문 시 확인 등)로 대체한다. 문자열을 조립하지 않는다.
 */
internal fun orderCompleteValueOrNull(value: String): String? = value.takeUnless { it.isBlank() }

/** 주문번호가 없으면 주문 상세 보기를 비활성으로 둔다(상세 화면은 주문번호로 조회한다). */
internal fun isOrderDetailsAvailable(orderNumber: String): Boolean = orderCompleteValueOrNull(orderNumber) != null
