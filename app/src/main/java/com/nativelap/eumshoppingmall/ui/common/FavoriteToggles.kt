package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * 찜 하트의 로컬 토글. 로그인이 필요해서 비로그인이면 토글하지 않고 [onLoginRequired]만 부른다.
 *
 * 서버·저장소 연동 전이라 찜 상태는 화면이 떠나면 사라지는 로컬 값이다. 로그인 후 같은 하트를 자동으로 다시 누르지 않는다.
 */
class FavoriteToggles(
    private val favoriteIdsState: MutableState<Set<String>>,
    private val isLoggedIn: Boolean,
    private val onLoginRequired: () -> Unit,
) {
    fun isFavorite(productId: String): Boolean = productId in favoriteIdsState.value

    fun toggle(productId: String) {
        if (!isLoggedIn) {
            onLoginRequired()
            return
        }
        val currentIds = favoriteIdsState.value
        favoriteIdsState.value = if (productId in currentIds) currentIds - productId else currentIds + productId
    }
}

@Composable
fun rememberFavoriteToggles(
    initialFavoriteIds: Set<String>,
    isLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
): FavoriteToggles {
    val favoriteIdsState = rememberSaveable { mutableStateOf(initialFavoriteIds) }
    return FavoriteToggles(favoriteIdsState, isLoggedIn, onLoginRequired)
}
