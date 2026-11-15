package com.nativelap.eumshoppingmall.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * 로그인 여부만 보관하는 세션 홀더.
 *
 * 이메일·비밀번호·토큰은 담지 않는다. 실제 인증 연동 전까지의 임시 상태이며 모든 빌드에서 초기값은 비로그인이다.
 * Boolean 하나라 rememberSaveable로 프로세스 종료 후에도 복원된다.
 */
@Stable
class EumLoginSession(
    private val isLoggedInState: MutableState<Boolean>,
) {
    val isLoggedIn: Boolean
        get() = isLoggedInState.value

    // 인증이 성공한 직후에만 호출한다.
    fun markLoggedIn() {
        isLoggedInState.value = true
    }
}

@Composable
fun rememberEumLoginSession(): EumLoginSession {
    val isLoggedInState = rememberSaveable { mutableStateOf(false) }
    return remember(isLoggedInState) { EumLoginSession(isLoggedInState) }
}
