package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthLoginUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthLoginScreen(uiModel: AuthLoginUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.account_login), onBack = { onEvent(AuthScreenEvent.CloseClicked) }, isClose = true) },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Spacer(Modifier.height(EumSpacing.x8))
            Text(stringResource(R.string.auth_brand_title), style = MaterialTheme.typography.headlineMedium)
            AuthTextField(
                label = stringResource(R.string.auth_email_label),
                value = uiModel.email,
                onValueChange = { onEvent(AuthScreenEvent.LoginEmailChanged(it)) },
                placeholder = stringResource(R.string.auth_email_placeholder),
                keyboardType = KeyboardType.Email,
            )
            AuthPasswordField(
                label = stringResource(R.string.auth_password_label),
                value = uiModel.password,
                isVisible = uiModel.isPasswordVisible,
                onValueChange = { onEvent(AuthScreenEvent.LoginPasswordChanged(it)) },
                onVisibilityChange = { onEvent(AuthScreenEvent.LoginPasswordVisibilityChanged) },
                errorText = if (uiModel.isCredentialsInvalid) stringResource(R.string.auth_login_mismatch, uiModel.failedAttemptCount) else null,
            )
            Row(
                Modifier.fillMaxWidth().height(48.dp).clickable { onEvent(AuthScreenEvent.RememberLoginChanged(!uiModel.isRemembered)) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = uiModel.isRemembered, onCheckedChange = { onEvent(AuthScreenEvent.RememberLoginChanged(it)) })
                Text(stringResource(R.string.account_keep_signed_in), style = MaterialTheme.typography.bodySmall)
            }
            AuthPrimaryAction(stringResource(R.string.account_login), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.LoginSubmitted) }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                AuthHelpLink(stringResource(R.string.account_find_email), "find_email", onEvent)
                AuthHelpDivider()
                AuthHelpLink(stringResource(R.string.account_find_password), "find_password", onEvent)
                AuthHelpDivider()
                AuthHelpLink(stringResource(R.string.account_sign_up), "signup", onEvent)
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.eumColors.line)
                Text(stringResource(R.string.common_or), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.eumColors.line)
            }
            OutlinedButton(onClick = { onEvent(AuthScreenEvent.SocialLoginClicked("provider_a")) }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Text(stringResource(R.string.auth_social_login_a))
            }
            OutlinedButton(onClick = { onEvent(AuthScreenEvent.SocialLoginClicked("provider_b")) }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Text(stringResource(R.string.auth_social_login_b))
            }
            Spacer(Modifier.height(EumSpacing.x24))
        }
    }
}

@Composable
private fun AuthHelpLink(label: String, id: String, onEvent: (AuthScreenEvent) -> Unit) {
    Text(
        label,
        Modifier.clickable { onEvent(AuthScreenEvent.LoginHelpClicked(id)) }.heightIn(min = 48.dp).padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x12),
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
private fun AuthHelpDivider() {
    Text("|", color = MaterialTheme.eumColors.textTertiary, style = MaterialTheme.typography.labelSmall)
}
