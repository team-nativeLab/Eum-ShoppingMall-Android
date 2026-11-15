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
import com.nativelap.eumshoppingmall.ui.auth.AccountService
import com.nativelap.eumshoppingmall.ui.auth.AccountServiceResult
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthMemberType
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPhoneVerificationUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthSignupUiModel
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

private const val MIN_PASSWORD_LENGTH = 8

/**
 * 회원가입(A-14). 이름·연락처·이메일·사업자 정보는 개인정보라 저장 상태(rememberSaveable)에 넣지 않는다.
 *
 * 서버 호출은 [accountService]에 맡기고 가입이 성공하면 [onSignupSuccess]를 부른다. 비밀번호·인증번호는 저장 상태나 NavKey에
 * 넣지 않고 화면을 떠날 때 지운다(지우기는 참조를 줄이는 최선 노력이다). 사업자 인증·등록증 업로드는 연동 전이라 안내만 한다.
 */
@Composable
fun AuthSignupRoute(
    accountService: AccountService,
    onSignupSuccess: () -> Unit,
    onBackClick: () -> Unit,
    onConsentDetailsClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val preparingMessage = stringResource(R.string.flow_service_preparing)
    val emailInvalidMessage = stringResource(R.string.flow_signup_email_invalid)
    val codeInvalidMessage = stringResource(R.string.flow_signup_code_invalid)
    var memberType by rememberSaveable { mutableStateOf(AuthMemberType.PERSONAL) }
    var email by remember { mutableStateOf("") }
    var isEmailAvailable by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isCodeRequested by remember { mutableStateOf(false) }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var isCodeInvalid by remember { mutableStateOf(false) }
    var isTermsChecked by rememberSaveable { mutableStateOf(false) }
    var isPrivacyChecked by rememberSaveable { mutableStateOf(false) }
    var isMarketingChecked by rememberSaveable { mutableStateOf(false) }
    var registrationNumber by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var representativeName by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var taxEmail by remember { mutableStateOf("") }
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

    fun isPasswordAcceptable(candidate: CharArray): Boolean = candidate.size >= MIN_PASSWORD_LENGTH &&
        candidate.any(Char::isLetter) &&
        candidate.any(Char::isDigit)

    val isBusinessInformationReady = memberType == AuthMemberType.PERSONAL ||
        (registrationNumber.isNotBlank() && companyName.isNotBlank() && representativeName.isNotBlank())

    // 콜백에서도 최신 상태를 읽도록 함수로 둔다.
    fun isReadyToSubmit(): Boolean = isEmailAvailable &&
        email.isNotBlank() &&
        isPasswordAcceptable(password) &&
        password.contentEquals(passwordConfirmation) &&
        name.isNotBlank() &&
        isPhoneVerified &&
        isTermsChecked &&
        isPrivacyChecked &&
        isBusinessInformationReady
    val isSubmitEnabled = isReadyToSubmit() && !isSubmitting

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
                memberType = memberType,
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
                businessRegistrationNumber = registrationNumber,
                companyName = companyName,
                representativeName = representativeName,
                industry = industry,
                taxEmail = taxEmail,
                isSubmitEnabled = isSubmitEnabled,
            ),
            onEvent = { event ->
                when (event) {
                    AuthScreenEvent.BackClicked,
                    AuthScreenEvent.CloseClicked,
                    -> onBackClick()

                    is AuthScreenEvent.SignupMemberTypeChanged -> memberType = event.value

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

                        "registration_number" -> registrationNumber = event.value

                        "company_name" -> companyName = event.value

                        "representative" -> representativeName = event.value

                        "industry" -> industry = event.value

                        "tax_email" -> taxEmail = event.value
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

                    AuthScreenEvent.SignupConsentDetailsClicked -> onConsentDetailsClick()

                    // 사업자 인증·등록증 파일 선택은 서버·파일 선택 연동 전이라 안내만 한다.
                    AuthScreenEvent.SignupBusinessVerificationClicked,
                    AuthScreenEvent.BusinessRegistrationFileClicked,
                    -> showMessage(preparingMessage)

                    AuthScreenEvent.SignupSubmitted -> submitSignup()

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
