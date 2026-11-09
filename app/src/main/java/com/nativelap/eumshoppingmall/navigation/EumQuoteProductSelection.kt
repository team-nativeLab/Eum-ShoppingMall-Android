package com.nativelap.eumshoppingmall.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * 견적 문의(A-15)의 "상품 추가"에서 고른 상품 ID를 폼으로 돌려주기 위한 한 칸짜리 보관소.
 *
 * navigation3 1.1.7에는 결과 반환 API가 없다. 고른 값을 NavKey에 실으면 back stack 항목을 바꿔 끼워야 하고, UiModel을 실으면
 * back stack에 화면 데이터가 저장된다. 그래서 상품 선택 화면은 ID 하나를 여기에 두고 pop만 하며, 다시 그려진 폼 Route가
 * 그 ID를 자기 목록에 더한 뒤 [consume]으로 비운다. 값은 문자열 하나라 rememberSaveable로 프로세스 종료 후에도 복원된다.
 * 선택 화면은 항상 폼 바로 위에 push되므로, 칸이 차 있는 동안 composition에 있는 폼은 선택을 요청한 그 폼 하나뿐이다.
 */
@Stable
class EumQuoteProductSelection(
    private val pendingProductIdState: MutableState<String?>,
) {
    val pendingProductId: String?
        get() = pendingProductIdState.value

    // 상품 선택 화면에서 고른 상품 ID를 맡긴다. 이어서 선택 화면을 pop하는 것은 호출부 몫이다.
    fun select(productId: String) {
        pendingProductIdState.value = productId
    }

    // 폼이 맡긴 ID를 반영한 뒤 칸을 비운다.
    fun consume() {
        pendingProductIdState.value = null
    }
}

@Composable
fun rememberEumQuoteProductSelection(): EumQuoteProductSelection {
    val pendingProductIdState = rememberSaveable { mutableStateOf<String?>(null) }
    return remember(pendingProductIdState) { EumQuoteProductSelection(pendingProductIdState) }
}
