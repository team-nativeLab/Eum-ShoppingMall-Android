package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPasswordResetUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthPasswordResetScreen(uiModel: AuthPasswordResetUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.auth_password_reset_title), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().imePadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                AuthPrimaryAction(stringResource(R.string.auth_change), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.ResetSubmitted) }
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
            Text(stringResource(R.string.auth_reset_password_notice), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            AuthPasswordField(
                stringResource(R.string.auth_new_password_label),
                uiModel.password,
                uiModel.isPasswordVisible,
                { onEvent(AuthScreenEvent.ResetSecretChanged("password", it)) },
                { onEvent(AuthScreenEvent.ResetPasswordVisibilityChanged) },
                helperText = stringResource(R.string.auth_password_rule),
            )
            AuthPasswordField(
                stringResource(R.string.account_password_confirmation),
                uiModel.passwordConfirmation,
                uiModel.isConfirmationVisible,
                { onEvent(AuthScreenEvent.ResetSecretChanged("confirmation", it)) },
                { onEvent(AuthScreenEvent.ResetConfirmationVisibilityChanged) },
                errorText = if (uiModel.isPasswordInvalid) stringResource(R.string.auth_password_mismatch) else null,
            )
        }
    }
}
