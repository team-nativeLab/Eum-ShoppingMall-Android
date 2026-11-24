package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.AccountService
import com.nativelap.eumshoppingmall.ui.auth.AccountServiceResult
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPhoneVerificationUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthSignupUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.SignupInputs
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.SignupStepRules
import com.nativelap.eumshoppingmall.ui.common.EumBottomBarMetrics
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * 회원가입(A-14). 이름·연락처·이메일은 개인정보라 저장 상태(rememberSaveable)에 넣지 않는다.
 *
 * 서버 호출은 [accountService]에 맡기고 가입이 성공하면 [onSignupSuccess]를 부른다. 비밀번호·인증번호는 저장 상태나 NavKey에
 * 넣지 않고 화면을 떠날 때 지운다(지우기는 참조를 줄이는 최선 노력이다).
 */
@Composable
fun AuthSignupRoute(
    accountService: AccountService,
    onSignupSuccess: () -> Unit,
    onBackClick: () -> Unit,
    onTermsDetailsClick: () -> Unit = {},
    onPrivacyDetailsClick: () -> Unit = {},
    onMarketingDetailsClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val preparingMessage = stringResource(R.string.flow_service_preparing)
    val emailInvalidMessage = stringResource(R.string.flow_signup_email_invalid)
    val codeInvalidMessage = stringResource(R.string.flow_signup_code_invalid)
    // 가입 단계는 입력값(remember)과 같은 생존 범위다. 회전·복원으로 입력이 비면 단계도 처음으로 돌아간다.
    var step by remember { mutableIntStateOf(0) }
    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var isEmailAvailable by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isCodeRequested by remember { mutableStateOf(false) }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var isCodeInvalid by remember { mutableStateOf(false) }
    // 동의 체크도 입력값·단계와 같은 remember 범위다. 회전 뒤 입력은 비었는데 동의만 남는 불일치를 막는다.
    var isTermsChecked by remember { mutableStateOf(false) }
    var isPrivacyChecked by remember { mutableStateOf(false) }
    var isMarketingChecked by remember { mutableStateOf(false) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmationVisible by rememberSaveable { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf(charArrayOf()) }
    var passwordConfirmation by remember { mutableStateOf(charArrayOf()) }
    var verificationCode by remember { mutableStateOf(charArrayOf()) }
    DisposableEffect(Unit) {
        onDispose {
            password.fill('\u0000')
            passwordConfirmation.fill('\u0000')
            verificationCode.fill('\u0000')
        }
    }

    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    // 콜백에서도 최신 상태를 읽도록 함수로 둔다. 검증 규칙은 SignupStepRules가 정한다.
    fun currentInputs() = SignupInputs(
        email = email,
        isEmailAvailable = isEmailAvailable,
        name = name,
        password = password,
        passwordConfirmation = passwordConfirmation,
        isPhoneVerified = isPhoneVerified,
        isTermsChecked = isTermsChecked,
        isPrivacyChecked = isPrivacyChecked,
    )

    fun isReadyToSubmit(): Boolean = SignupStepRules.isReadyToSubmit(currentInputs())
    val isSubmitEnabled = isReadyToSubmit() && !isSubmitting

    fun isStepValid(targetStep: Int): Boolean = SignupStepRules.isStepValid(targetStep, currentInputs())

    // 단계를 옮길 때 포커스를 풀어 키보드를 닫는다. 첫 단계에서 뒤로 가면 화면을 닫는다.
    fun goToPreviousStepOrClose() {
        focusManager.clearFocus()
        if (step > 0) step -= 1 else onBackClick()
    }
    BackHandler(enabled = step > 0) { goToPreviousStepOrClose() }

    // 서비스 호출을 한 곳에서 감싸 취소만 전파하고, 그 밖의 실패는 ServiceUnavailable로 본다.
    fun runAccountCall(call: suspend () -> AccountServiceResult, onResult: (AccountServiceResult) -> Unit) {
        scope.launch {
            val callResult = try {
                call()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                AccountServiceResult.ServiceUnavailable
            }
            onResult(callResult)
        }
    }

    fun submitSignup() {
        if (isSubmitting || !isReadyToSubmit()) return
        isSubmitting = true
        val submittedEmail = email.trim()
        val submittedPassword = password.copyOf()
        scope.launch {
            val signupResult = try {
                accountService.register(submittedEmail, submittedPassword)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                AccountServiceResult.ServiceUnavailable
            } finally {
                submittedPassword.fill('\u0000')
                isSubmitting = false
            }
            when (signupResult) {
                AccountServiceResult.Success -> onSignupSuccess()
                AccountServiceResult.Rejected -> showMessage(emailInvalidMessage)
                AccountServiceResult.ServiceUnavailable -> showMessage(preparingMessage)
            }
        }
    }

    Box {
        AuthSignupScreen(
            uiModel = AuthSignupUiModel(
                step = step,
                isStepValid = isStepValid(step),
                email = email,
                isEmailAvailable = isEmailAvailable,
                password = password,
                passwordConfirmation = passwordConfirmation,
                isPasswordVisible = isPasswordVisible,
                isPasswordConfirmationVisible = isConfirmationVisible,
                name = name,
                phone = AuthPhoneVerificationUiModel(
                    phoneNumber = phoneNumber,
                    verificationCode = verificationCode,
                    isCodeRequested = isCodeRequested,
                    isVerified = isPhoneVerified,
                    isCodeInvalid = isCodeInvalid,
                ),
                isAllConsentChecked = isTermsChecked && isPrivacyChecked && isMarketingChecked,
                isTermsChecked = isTermsChecked,
                isPrivacyChecked = isPrivacyChecked,
                isMarketingChecked = isMarketingChecked,
                isSubmitEnabled = isSubmitEnabled,
            ),
            onEvent = { event ->
                when (event) {
                    AuthScreenEvent.BackClicked,
                    AuthScreenEvent.CloseClicked,
                    -> goToPreviousStepOrClose()

                    AuthScreenEvent.SignupNextClicked -> if (step < SignupStepRules.STEP_COUNT - 1 && isStepValid(step)) {
                        focusManager.clearFocus()
                        step += 1
                    }

                    is AuthScreenEvent.SignupFieldChanged -> when (event.fieldId) {
                        "email" -> {
                            email = event.value
                            isEmailAvailable = false
                        }

                        "name" -> name = event.value

                        "phone" -> {
                            phoneNumber = event.value
                            isPhoneVerified = false
                            isCodeInvalid = false
                        }
                    }

                    is AuthScreenEvent.SignupSecretChanged -> when (event.fieldId) {
                        "password" -> {
                            password.fill('\u0000')
                            password = event.value
                        }

                        "password_confirmation" -> {
                            passwordConfirmation.fill('\u0000')
                            passwordConfirmation = event.value
                        }
                    }

                    is AuthScreenEvent.SignupPasswordVisibilityChanged -> if (event.fieldId == "password") {
                        isPasswordVisible = !isPasswordVisible
                    } else {
                        isConfirmationVisible = !isConfirmationVisible
                    }

                    AuthScreenEvent.EmailDuplicateCheckClicked -> {
                        val checkedEmail = email.trim()
                        runAccountCall({ accountService.checkEmailAvailable(checkedEmail) }) { checkResult ->
                            when (checkResult) {
                                AccountServiceResult.Success -> isEmailAvailable = checkedEmail == email.trim()
                                AccountServiceResult.Rejected -> showMessage(emailInvalidMessage)
                                AccountServiceResult.ServiceUnavailable -> showMessage(preparingMessage)
                            }
                        }
                    }

                    AuthScreenEvent.SignupPhoneResendClicked -> {
                        if (phoneNumber.isNotBlank()) isCodeRequested = true
                    }

                    is AuthScreenEvent.SignupVerificationCodeChanged -> {
                        verificationCode.fill('\u0000')
                        verificationCode = event.value
                        isCodeInvalid = false
                    }

                    AuthScreenEvent.SignupVerificationConfirmClicked -> {
                        val submittedCode = verificationCode.copyOf()
                        runAccountCall({
                            try {
                                accountService.confirmPhoneCode(phoneNumber, submittedCode)
                            } finally {
                                submittedCode.fill('\u0000')
                            }
                        }) { confirmResult ->
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

                    is AuthScreenEvent.SignupConsentChanged -> when (event.consentId) {
                        "all" -> {
                            isTermsChecked = event.checked
                            isPrivacyChecked = event.checked
                            isMarketingChecked = event.checked
                        }

                        "terms" -> isTermsChecked = event.checked

                        "privacy" -> isPrivacyChecked = event.checked

                        "marketing" -> isMarketingChecked = event.checked
                    }

                    AuthScreenEvent.SignupTermsDetailsClicked -> onTermsDetailsClick()

                    AuthScreenEvent.SignupPrivacyDetailsClicked -> onPrivacyDetailsClick()

                    AuthScreenEvent.SignupMarketingDetailsClicked -> onMarketingDetailsClick()

                    AuthScreenEvent.SignupSubmitted -> submitSignup()

                    else -> Unit
                }
            },
        )
        EumSnackbarHost(
            hostState = snackbarHostState,
            // 하단 CTA 바(마지막 단계는 안내 문구 포함)와 겹치지 않게 그 높이만큼 올리고, 키보드가 열리면 키보드 위로 올린다.
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().imePadding()
                .padding(bottom = if (step == SignupStepRules.STEP_COUNT - 1) EumBottomBarMetrics.AuthCtaBarMinHeight + EumSpacing.x32 else EumBottomBarMetrics.AuthCtaBarMinHeight),
        )
    }
}
