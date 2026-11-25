package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthSignupUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.SignupStepRules
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthSignupScreen(uiModel: AuthSignupUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    val isLastStep = uiModel.step == SignupStepRules.STEP_COUNT - 1
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.account_sign_up), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            AuthBottomBar {
                if (isLastStep) {
                    Text(
                        stringResource(R.string.auth_signup_notice),
                        Modifier.fillMaxWidth(),
                        style = EumTypographyTokens.caption,
                        color = MaterialTheme.eumColors.textTertiary,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(EumSpacing.x8))

                    AuthPrimaryAction(stringResource(R.string.auth_signup_submit), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.SignupSubmitted) }
                } else {
                    AuthPrimaryAction(stringResource(R.string.common_next), uiModel.isStepValid) { onEvent(AuthScreenEvent.SignupNextClicked) }
                }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.pageGutter),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x20),
        ) {
            SignupStepHeader(step = uiModel.step)
            when (uiModel.step) {
                0 -> {
                    AuthTextField(
                        stringResource(R.string.auth_email_label),
                        uiModel.email,
                        { onEvent(AuthScreenEvent.SignupFieldChanged("email", it)) },
                        placeholder = stringResource(R.string.auth_email_placeholder),
                        helperText = if (uiModel.isEmailAvailable) stringResource(R.string.auth_email_available) else null,
                        keyboardType = KeyboardType.Email,
                        isRequired = true,
                        trailingAction = {
                            AuthFieldActionButton(
                                label = stringResource(R.string.auth_email_duplicate_check),
                                onClick = { onEvent(AuthScreenEvent.EmailDuplicateCheckClicked) },
                            )
                        },
                    )
                    AuthTextField(
                        stringResource(R.string.auth_full_name_label),
                        uiModel.name,
                        { onEvent(AuthScreenEvent.SignupFieldChanged("name", it)) },
                        placeholder = stringResource(R.string.auth_name_placeholder),
                        isRequired = true,
                    )
                }

                1 -> {
                    AuthPasswordField(
                        stringResource(R.string.auth_password_label),
                        uiModel.password,
                        isVisible = uiModel.isPasswordVisible,
                        onValueChange = { onEvent(AuthScreenEvent.SignupSecretChanged("password", it)) },
                        onVisibilityChange = { onEvent(AuthScreenEvent.SignupPasswordVisibilityChanged("password")) },
                        helperText = stringResource(R.string.auth_password_rule),
                        isRequired = true,
                    )
                    AuthPasswordField(
                        stringResource(R.string.account_password_confirmation),
                        uiModel.passwordConfirmation,
                        isVisible = uiModel.isPasswordConfirmationVisible,
                        onValueChange = { onEvent(AuthScreenEvent.SignupSecretChanged("password_confirmation", it)) },
                        onVisibilityChange = { onEvent(AuthScreenEvent.SignupPasswordVisibilityChanged("confirmation")) },
                        errorText = if (uiModel.passwordConfirmation.isNotEmpty() && !uiModel.password.contentEquals(uiModel.passwordConfirmation)) stringResource(R.string.auth_password_mismatch) else null,
                        isRequired = true,
                    )
                }

                2 -> AuthPhoneVerificationFields(
                    label = stringResource(R.string.auth_phone_number),
                    state = uiModel.phone,
                    onPhoneChanged = { onEvent(AuthScreenEvent.SignupFieldChanged("phone", it)) },
                    onResend = { onEvent(AuthScreenEvent.SignupPhoneResendClicked) },
                    onCodeChanged = { onEvent(AuthScreenEvent.SignupVerificationCodeChanged(it)) },
                    onConfirm = { onEvent(AuthScreenEvent.SignupVerificationConfirmClicked) },
                    isRequired = true,
                )

                else -> AuthConsentGroup(uiModel, onEvent)
            }

            Spacer(Modifier.height(EumSpacing.x4))
        }
    }
}

// 단계 표시: 4칸 진행 막대와 "N / 4 · 단계명" 라벨. 완료·현재 칸은 primary, 나머지는 회색이다.
// 막대는 장식이고 의미는 라벨이 전하므로 막대의 접근성 정보는 비운다.
@Composable
private fun SignupStepHeader(step: Int) {
    val stepNameRes = when (step) {
        0 -> R.string.auth_signup_step_name_account
        1 -> R.string.auth_signup_step_name_password
        2 -> R.string.auth_signup_step_name_phone
        else -> R.string.auth_signup_step_name_consent
    }
    Column(Modifier.padding(top = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Row(
            modifier = Modifier.fillMaxWidth().clearAndSetSemantics {},
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            repeat(SignupStepRules.STEP_COUNT) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(EumSpacing.x4)
                        .clip(RoundedCornerShape(EumRadius.capsule))
                        .background(if (index <= step) MaterialTheme.eumColors.primary else MaterialTheme.eumColors.line),
                )
            }
        }
        Text(
            stringResource(R.string.auth_signup_step_label, step + 1, SignupStepRules.STEP_COUNT, stringResource(stepNameRes)),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.eumColors.textSecondary,
        )
    }
}
