package com.nativelap.eumshoppingmall.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * 주소 검색(A-46)에서 고른 주소 ID를 배송지(A-08)로 돌려주기 위한 한 칸짜리 보관소.
 *
 * NavKey나 back stack에 값을 싣지 않는다. 주소 문자열은 개인정보라
 * rememberSaveable에 넣지 않고 ID 하나만 보관하며, 배송지 Route가 ID를 표시용 주소로 바꾼 뒤 [consume]으로 비운다.
 * 주소 검색은 항상 배송지 바로 위에 push되므로 칸이 차 있는 동안 받을 화면은 그 배송지 하나뿐이다.
 */
@Stable
class EumAddressSelection(
    private val pendingAddressIdState: MutableState<String?>,
) {
    val pendingAddressId: String?
        get() = pendingAddressIdState.value

    // 주소 검색 화면에서 고른 주소 ID를 맡긴다. 이어서 검색 화면을 pop하는 것은 호출부 몫이다.
    fun select(addressId: String) {
        pendingAddressIdState.value = addressId
    }

    // 배송지가 맡긴 ID를 반영한 뒤 칸을 비운다.
    fun consume() {
        pendingAddressIdState.value = null
    }
}

@Composable
fun rememberEumAddressSelection(): EumAddressSelection {
    val pendingAddressIdState = rememberSaveable { mutableStateOf<String?>(null) }
    return remember(pendingAddressIdState) { EumAddressSelection(pendingAddressIdState) }
}
