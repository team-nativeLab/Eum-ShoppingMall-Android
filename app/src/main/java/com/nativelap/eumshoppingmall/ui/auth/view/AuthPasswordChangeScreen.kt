package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPasswordChangeUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthPasswordChangeScreen(uiModel: AuthPasswordChangeUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.auth_password_change_title), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            AuthBottomBar {
                AuthPrimaryAction(stringResource(R.string.auth_change), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.PasswordChangeSubmitted) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(stringResource(R.string.account_new_password_instruction), style = MaterialTheme.typography.headlineSmall)
                Text(stringResource(R.string.account_password_periodic_change_notice), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }

            Spacer(Modifier.height(EumSpacing.x24))

            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                AuthPasswordField(
                    stringResource(R.string.auth_current_password),
                    uiModel.currentPassword,
                    uiModel.isCurrentPasswordVisible,
                    { onEvent(AuthScreenEvent.PasswordChangeSecretChanged("current", it)) },
                    { onEvent(AuthScreenEvent.PasswordChangeVisibilityChanged("current")) },
                    errorText = if (uiModel.isCurrentPasswordInvalid) stringResource(R.string.auth_current_password_invalid) else null,
                )
                AuthPasswordField(
                    stringResource(R.string.auth_new_password_label),
                    uiModel.newPassword,
                    uiModel.isNewPasswordVisible,
                    { onEvent(AuthScreenEvent.PasswordChangeSecretChanged("new", it)) },
                    { onEvent(AuthScreenEvent.PasswordChangeVisibilityChanged("new")) },
                    helperText = stringResource(R.string.auth_password_rule),
                )
                AuthPasswordField(
                    stringResource(R.string.account_password_confirmation),
                    uiModel.confirmation,
                    uiModel.isConfirmationVisible,
                    { onEvent(AuthScreenEvent.PasswordChangeSecretChanged("confirmation", it)) },
                    { onEvent(AuthScreenEvent.PasswordChangeVisibilityChanged("confirmation")) },
                    successText = if (uiModel.isConfirmationValid) stringResource(R.string.auth_password_confirmation_valid) else null,
                )
            }

            Spacer(Modifier.height(EumSpacing.x24))

            val forgottenPromptColor = MaterialTheme.eumColors.textTertiary
            val findPasswordColor = MaterialTheme.eumColors.textPrimary
            val findPasswordPrompt = buildAnnotatedString {
                withStyle(SpanStyle(color = forgottenPromptColor)) { append(stringResource(R.string.account_password_forgotten)) }
                // 두 문구 사이 4dp 간격은 12sp 공백 한 칸으로 근사한다.
                append(" ")
                withStyle(SpanStyle(color = findPasswordColor, textDecoration = TextDecoration.Underline)) { append(stringResource(R.string.account_find_password)) }
            }
            Box(
                Modifier.clickable { onEvent(AuthScreenEvent.PasswordChangeFindPasswordClicked) }.heightIn(min = 48.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(findPasswordPrompt, style = EumTypographyTokens.caption)
            }
        }
    }
}
