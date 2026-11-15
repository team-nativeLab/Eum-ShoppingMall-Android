package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.AccountLookupResult
import com.nativelap.eumshoppingmall.ui.auth.AccountService
import com.nativelap.eumshoppingmall.ui.auth.AccountServiceResult
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountRecoveryMode
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountRecoveryUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPhoneVerificationUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * 이메일 찾기(A-27)·비밀번호 찾기(A-28).
 *
 * 이메일 찾기만 [accountService]로 조회한다. 비밀번호 찾기는 재설정 메일·문자 발송 서버가 없어 제출 시 준비 중 안내만 한다.
 * 인증번호는 저장 상태에 넣지 않고 화면을 떠날 때 지운다.
 */
@Composable
fun AccountRecoveryRoute(
    accountService: AccountService,
    startsWithPasswordMode: Boolean,
    onCloseClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val preparingMessage = stringResource(R.string.flow_service_preparing)
    val codeInvalidMessage = stringResource(R.string.flow_signup_code_invalid)
    var mode by rememberSaveable {
        mutableStateOf(if (startsWithPasswordMode) AccountRecoveryMode.FIND_PASSWORD else AccountRecoveryMode.FIND_EMAIL)
    }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isCodeRequested by remember { mutableStateOf(false) }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var isCodeInvalid by remember { mutableStateOf(false) }
    // 조회 결과는 마스킹된 이메일만 보관한다. null이면 결과 화면이 아니다.
    var maskedEmail by remember { mutableStateOf<String?>(null) }
    var isNotFound by remember { mutableStateOf(false) }
    var verificationCode by remember { mutableStateOf(charArrayOf()) }
    DisposableEffect(Unit) {
        onDispose { verificationCode.fill('\u0000') }
    }

    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    val isEmailMode = mode == AccountRecoveryMode.FIND_EMAIL
    val isSubmitEnabled = name.isNotBlank() && isPhoneVerified && (isEmailMode || email.isNotBlank())

    fun resetResult() {
        maskedEmail = null
        isNotFound = false
    }

    fun submitRecovery() {
        if (!isSubmitEnabled) return
        if (!isEmailMode) {
            showMessage(preparingMessage)
            return
        }
        scope.launch {
            val lookupResult = try {
                accountService.findMaskedEmail(name.trim(), phoneNumber.trim())
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                AccountLookupResult.ServiceUnavailable
            }
            when (lookupResult) {
                is AccountLookupResult.Found -> maskedEmail = lookupResult.maskedEmail
                AccountLookupResult.NotFound -> isNotFound = true
                AccountLookupResult.ServiceUnavailable -> showMessage(preparingMessage)
            }
        }
    }

    Box {
        AccountRecoveryScreen(
            uiModel = AccountRecoveryUiModel(
                mode = mode,
                isResultVisible = maskedEmail != null || isNotFound,
                isAccountFound = maskedEmail != null,
                name = name,
                email = email,
                maskedEmail = maskedEmail.orEmpty(),
                phone = AuthPhoneVerificationUiModel(
                    phoneNumber = phoneNumber,
                    verificationCode = verificationCode,
                    isCodeRequested = isCodeRequested,
                    isVerified = isPhoneVerified,
                    isCodeInvalid = isCodeInvalid,
                ),
                isSubmitEnabled = isSubmitEnabled,
            ),
            onEvent = { event ->
                when (event) {
                    AuthScreenEvent.BackClicked,
                    AuthScreenEvent.CloseClicked,
                    -> onCloseClick()

                    is AuthScreenEvent.RecoveryModeChanged -> {
                        mode = event.mode
                        resetResult()
                    }

                    is AuthScreenEvent.RecoveryNameChanged -> name = event.value

                    is AuthScreenEvent.RecoveryEmailChanged -> email = event.value

                    is AuthScreenEvent.RecoveryPhoneChanged -> {
                        phoneNumber = event.value
                        isPhoneVerified = false
                        isCodeInvalid = false
                    }

                    AuthScreenEvent.RecoveryPhoneResendClicked -> {
                        if (phoneNumber.isNotBlank()) isCodeRequested = true
                    }

                    is AuthScreenEvent.RecoveryVerificationCodeChanged -> {
                        verificationCode.fill('\u0000')
                        verificationCode = event.value
                        isCodeInvalid = false
                    }

                    AuthScreenEvent.RecoveryVerificationConfirmClicked -> {
                        val submittedCode = verificationCode.copyOf()
                        scope.launch {
                            val confirmResult = try {
                                accountService.confirmPhoneCode(phoneNumber, submittedCode)
                            } catch (exception: CancellationException) {
                                throw exception
                            } catch (exception: Exception) {
                                AccountServiceResult.ServiceUnavailable
                            } finally {
                                submittedCode.fill('\u0000')
                            }
                            when (confirmResult) {
                                AccountServiceResult.Success -> isPhoneVerified = true

                                AccountServiceResult.Rejected -> {
                                    isCodeInvalid = true
                                    showMessage(codeInvalidMessage)
                                }

                                AccountServiceResult.ServiceUnavailable -> showMessage(preparingMessage)
                            }
                        }
                    }

                    AuthScreenEvent.RecoverySubmitted -> submitRecovery()

                    AuthScreenEvent.RecoveryRetryClicked -> resetResult()

                    AuthScreenEvent.RecoveryLoginClicked -> onLoginClick()

                    AuthScreenEvent.RecoveryFindPasswordClicked -> {
                        mode = AccountRecoveryMode.FIND_PASSWORD
                        resetResult()
                    }

                    AuthScreenEvent.RecoverySignupClicked -> onSignupClick()

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
