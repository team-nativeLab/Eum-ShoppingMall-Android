package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthLoginUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent

/**
 * 로그인(A-13).
 *
 * 인증 연동이 없으므로 로그인 제출은 아무 동작도 하지 않는다. 비밀번호는 저장 상태(rememberSaveable)나 NavKey에 넣지 않고
 * 화면을 떠날 때 지운다.
 */
@Composable
fun AuthLoginRoute(onCloseClick: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var isRemembered by rememberSaveable { mutableStateOf(false) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var password by remember { mutableStateOf(charArrayOf()) }
    DisposableEffect(Unit) {
        onDispose {
            password.fill('\u0000')
        }
    }
    AuthLoginScreen(
        uiModel = AuthLoginUiModel(
            email = email,
            password = password,
            isRemembered = isRemembered,
            isPasswordVisible = isPasswordVisible,
            isSubmitEnabled = email.isNotBlank() && password.isNotEmpty(),
        ),
        onEvent = { event ->
            when (event) {
                AuthScreenEvent.BackClicked,
                AuthScreenEvent.CloseClicked,
                -> onCloseClick()

                is AuthScreenEvent.LoginEmailChanged -> email = event.value

                is AuthScreenEvent.LoginPasswordChanged -> {
                    password.fill('\u0000')
                    password = event.value
                }

                is AuthScreenEvent.RememberLoginChanged -> isRemembered = event.checked

                AuthScreenEvent.LoginPasswordVisibilityChanged -> isPasswordVisible = !isPasswordVisible

                // 로그인 성공 경로, 계정 찾기·가입(auth 하위 흐름), 소셜 로그인은 후속 작업이다.
                else -> Unit
            }
        },
    )
}
