package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.LoginAuthResult
import com.nativelap.eumshoppingmall.ui.auth.LoginAuthenticator
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthLoginUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

// 안내 문구의 분모(/5회)와 맞추기 위한 표시 상한이다.
private const val MAX_LOGIN_ATTEMPTS_SHOWN = 5

/**
 * 로그인(A-13).
 *
 * 인증은 [authenticator]에 맡기고 성공하면 [onLoginSuccess]를 호출한다. 실패는 필드 아래 안내와 실패 횟수로 보여 준다.
 * 비밀번호는 저장 상태(rememberSaveable)나 NavKey에 넣지 않고, 인증에는 복사본을 넘겨 호출 직후 지우고 화면을 떠날 때도 지운다.
 * 지우기는 참조를 줄이는 최선 노력이다. 입력 필드가 만드는 문자열 복사본(AuthPasswordField의 concatToString 등)은 지울 수 없다.
 */
@Composable
fun AuthLoginRoute(
    authenticator: LoginAuthenticator,
    onLoginSuccess: () -> Unit,
    onCloseClick: () -> Unit,
    onFindEmailClick: () -> Unit = {},
    onFindPasswordClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val socialLoginPreparingMessage = stringResource(R.string.flow_social_login_preparing)
    var email by rememberSaveable { mutableStateOf("") }
    var isRemembered by rememberSaveable { mutableStateOf(false) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var failedAttemptCount by rememberSaveable { mutableIntStateOf(0) }
    var isServiceUnavailable by rememberSaveable { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf(charArrayOf()) }
    DisposableEffect(Unit) {
        onDispose {
            password.fill('\u0000')
        }
    }

    fun submitLogin() {
        if (isSubmitting) return
        isSubmitting = true
        val submittedEmail = email.trim()
        val submittedPassword = password.copyOf()
        scope.launch {
            val loginResult = try {
                authenticator.authenticate(submittedEmail, submittedPassword)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                LoginAuthResult.ServiceUnavailable
            } finally {
                submittedPassword.fill('\u0000')
                isSubmitting = false
            }
            when (loginResult) {
                LoginAuthResult.Success -> onLoginSuccess()

                LoginAuthResult.InvalidCredentials -> {
                    isServiceUnavailable = false
                    failedAttemptCount += 1
                }

                LoginAuthResult.ServiceUnavailable -> isServiceUnavailable = true
            }
        }
    }

    Box {
        AuthLoginScreen(
            uiModel = AuthLoginUiModel(
                email = email,
                password = password,
                isRemembered = isRemembered,
                isPasswordVisible = isPasswordVisible,
                isCredentialsInvalid = failedAttemptCount > 0 && !isServiceUnavailable,
                failedAttemptCount = failedAttemptCount.coerceAtMost(MAX_LOGIN_ATTEMPTS_SHOWN),
                isServiceUnavailable = isServiceUnavailable,
                isSubmitEnabled = email.isNotBlank() && password.isNotEmpty() && !isSubmitting,
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

                    AuthScreenEvent.LoginSubmitted -> submitLogin()

                    is AuthScreenEvent.LoginHelpClicked -> when (event.id) {
                        "find_email" -> onFindEmailClick()
                        "find_password" -> onFindPasswordClick()
                        "signup" -> onSignupClick()
                    }

                    // 소셜 로그인은 인증 연동 전에 성공 처리하지 않고 준비 중임을 알린다.
                    is AuthScreenEvent.SocialLoginClicked -> scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = socialLoginPreparingMessage,
                            duration = SnackbarDuration.Short,
                        )
                    }

                    else -> Unit
                }
            },
        )
        EumSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding(),
        )
    }
}
