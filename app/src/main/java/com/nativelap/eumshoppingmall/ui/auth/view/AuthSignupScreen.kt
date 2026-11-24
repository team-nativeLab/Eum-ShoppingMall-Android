package com.nativelap.eumshoppingmall.ui.auth.view

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthSignupUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.SignupStepRules
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
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x20),
        ) {
            SignupStepHeader(step = uiModel.step)
            when (uiModel.step) {
                0 -> {
                    AuthFieldActionRow(
                        actionLabel = stringResource(R.string.auth_email_duplicate_check),
                        onActionClick = { onEvent(AuthScreenEvent.EmailDuplicateCheckClicked) },
                    ) {
                        AuthTextField(
                            stringResource(R.string.auth_email_label),
                            uiModel.email,
                            { onEvent(AuthScreenEvent.SignupFieldChanged("email", it)) },
                            placeholder = stringResource(R.string.auth_email_placeholder),
                            helperText = if (uiModel.isEmailAvailable) stringResource(R.string.auth_email_available) else null,
                            keyboardType = KeyboardType.Email,
                            isRequired = true,
                        )
                    }
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

// 단계 표시: "n/4 단계" 문구, 진행 막대, 단계 제목. 진행 막대는 장식이고 의미는 문구가 전한다.
@Composable
private fun SignupStepHeader(step: Int) {
    val titleRes = when (step) {
        0 -> R.string.auth_signup_step_title_account
        1 -> R.string.auth_signup_step_title_password
        2 -> R.string.auth_signup_step_title_phone
        else -> R.string.auth_signup_step_title_consent
    }
    Column(Modifier.padding(top = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(
            stringResource(R.string.auth_signup_step_progress, step + 1, SignupStepRules.STEP_COUNT),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.eumColors.textSecondary,
        )
        LinearProgressIndicator(
            progress = { (step + 1).toFloat() / SignupStepRules.STEP_COUNT },
            modifier = Modifier.fillMaxWidth().height(4.dp).clearAndSetSemantics {},
            color = MaterialTheme.eumColors.textPrimary,
            trackColor = MaterialTheme.eumColors.line,
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp,
            drawStopIndicator = {},
        )
        Text(stringResource(titleRes), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

/** 입력 필드 오른쪽 액션 버튼 행. 버튼은 필드 상단에 맞추고 라벨 한 줄 높이만큼 내린다. */
@Composable
private fun AuthFieldActionRow(
    actionLabel: String,
    onActionClick: () -> Unit,
    field: @Composable () -> Unit,
) {
    val labelLineHeight = with(LocalDensity.current) { MaterialTheme.typography.labelMedium.lineHeight.toDp() }
    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.Top) {
        Box(Modifier.weight(1f)) { field() }
        AuthFieldActionButton(
            label = actionLabel,
            onClick = onActionClick,
            modifier = Modifier.padding(top = labelLineHeight + EumSpacing.x8),
        )
    }
}
