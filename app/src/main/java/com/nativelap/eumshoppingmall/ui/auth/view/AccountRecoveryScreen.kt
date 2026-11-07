package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountRecoveryMode
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AccountRecoveryUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountRecoveryScreen(uiModel: AccountRecoveryUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    if (uiModel.isResultVisible) {
        AccountRecoveryResultScreen(uiModel, onEvent)
    } else {
        AccountRecoveryFormScreen(uiModel, onEvent)
    }
}

@Composable
private fun AccountRecoveryFormScreen(uiModel: AccountRecoveryUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    val isEmailMode = uiModel.mode == AccountRecoveryMode.FIND_EMAIL
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.auth_find_account), onBack = { onEvent(AuthScreenEvent.BackClicked) }, isClose = true) },
        bottomBar = {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().imePadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                AuthPrimaryAction(
                    stringResource(if (isEmailMode) R.string.account_find_email else R.string.common_next),
                    uiModel.isSubmitEnabled,
                ) { onEvent(AuthScreenEvent.RecoverySubmitted) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            AuthSegmentedControl(
                labels = listOf(stringResource(R.string.account_find_email), stringResource(R.string.account_find_password)),
                selectedIndex = if (isEmailMode) 0 else 1,
                onSelected = { onEvent(AuthScreenEvent.RecoveryModeChanged(if (it == 0) AccountRecoveryMode.FIND_EMAIL else AccountRecoveryMode.FIND_PASSWORD)) },
            )
            Text(
                stringResource(if (isEmailMode) R.string.account_find_email_instruction else R.string.account_find_password_instruction),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                stringResource(if (isEmailMode) R.string.account_find_email_description else R.string.account_identity_verification_notice),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textSecondary,
            )
            if (isEmailMode) {
                AuthTextField(
                    stringResource(R.string.auth_full_name_label),
                    uiModel.name,
                    { onEvent(AuthScreenEvent.RecoveryNameChanged(it)) },
                    placeholder = stringResource(R.string.auth_name_placeholder),
                )
            } else {
                AuthTextField(
                    stringResource(R.string.auth_email_label),
                    uiModel.email,
                    { onEvent(AuthScreenEvent.RecoveryEmailChanged(it)) },
                    placeholder = stringResource(R.string.auth_email_placeholder),
                    keyboardType = KeyboardType.Email,
                )
            }
            AuthPhoneVerificationFields(
                label = stringResource(R.string.auth_phone_number),
                state = uiModel.phone,
                onPhoneChanged = { onEvent(AuthScreenEvent.RecoveryPhoneChanged(it)) },
                onResend = { onEvent(AuthScreenEvent.RecoveryPhoneResendClicked) },
                onCodeChanged = { onEvent(AuthScreenEvent.RecoveryVerificationCodeChanged(it)) },
                onConfirm = { onEvent(AuthScreenEvent.RecoveryVerificationConfirmClicked) },
            )
            if (isEmailMode) {
                Text(
                    stringResource(R.string.account_password_reset_phone_notice),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
            }
        }
    }
}

@Composable
private fun AccountRecoveryResultScreen(uiModel: AccountRecoveryUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    val found = uiModel.mode == AccountRecoveryMode.FIND_EMAIL && uiModel.isAccountFound
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.auth_find_account), onBack = { onEvent(AuthScreenEvent.CloseClicked) }, isClose = true) },
        bottomBar = {
            Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                AuthPrimaryAction(
                    stringResource(if (found) R.string.account_login else R.string.account_find_email),
                    true,
                    modifier = Modifier.weight(1f),
                ) { onEvent(if (found) AuthScreenEvent.RecoveryLoginClicked else AuthScreenEvent.RecoveryRetryClicked) }
                AuthSecondaryAction(
                    stringResource(if (found) R.string.account_find_password else R.string.account_sign_up),
                    Modifier.weight(1f),
                ) { onEvent(if (found) AuthScreenEvent.RecoveryFindPasswordClicked else AuthScreenEvent.RecoverySignupClicked) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = EumSpacing.x20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(shape = MaterialTheme.shapes.large, color = if (found) MaterialTheme.eumColors.success else MaterialTheme.eumColors.surfaceSecondary) {
                Icon(
                    painterResource(if (found) R.drawable.ic_check else R.drawable.ic_circle_x),
                    contentDescription = null,
                    modifier = Modifier.padding(EumSpacing.x20),
                    tint = if (found) MaterialTheme.eumColors.background else MaterialTheme.eumColors.textSecondary,
                )
            }
            Spacer(Modifier.padding(EumSpacing.x8))
            Text(
                stringResource(if (found) R.string.account_email_found else R.string.auth_recovery_not_found_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            Text(
                if (found) stringResource(R.string.auth_recovery_email_masked, uiModel.name) else stringResource(R.string.auth_recovery_not_found_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textSecondary,
                textAlign = TextAlign.Center,
            )
            if (found) {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Surface(
                        shape = RoundedCornerShape(EumRadius.x12),
                        color = MaterialTheme.eumColors.surfaceSecondary,
                        border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                    ) {
                        Column(Modifier.fillMaxWidth().padding(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                            Text(uiModel.maskedEmail, style = MaterialTheme.typography.titleMedium)
                            Text(stringResource(R.string.auth_member_joined_at, uiModel.joinedAt), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                        }
                    }
                    Text(stringResource(R.string.auth_recovery_email_safety), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
        }
    }
}

@Composable
private fun AuthSecondaryAction(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier.heightIn(min = 56.dp)) { Text(label) }
}
