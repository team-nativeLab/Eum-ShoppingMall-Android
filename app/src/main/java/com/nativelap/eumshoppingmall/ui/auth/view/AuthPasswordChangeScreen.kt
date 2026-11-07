package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPasswordChangeUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthPasswordChangeScreen(uiModel: AuthPasswordChangeUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.auth_password_change_title), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().imePadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                AuthPrimaryAction(stringResource(R.string.auth_change), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.PasswordChangeSubmitted) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Text(stringResource(R.string.account_new_password_instruction), style = MaterialTheme.typography.headlineSmall)
            Text(stringResource(R.string.account_password_periodic_change_notice), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
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
            Text(
                stringResource(R.string.account_password_forgotten) + "  " + stringResource(R.string.account_find_password),
                Modifier.clickable { onEvent(AuthScreenEvent.PasswordChangeFindPasswordClicked) }.heightIn(min = 48.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textSecondary,
            )
        }
    }
}
