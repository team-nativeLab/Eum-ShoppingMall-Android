package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPhoneVerificationUiModel
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthPhoneNumberChangeScreen(
    currentPhoneNumber: String,
    phoneState: AuthPhoneVerificationUiModel,
    isSubmitEnabled: Boolean,
    onEvent: (AuthScreenEvent) -> Unit,
) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.auth_phone_change_title), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            AuthBottomBar {
                AuthPrimaryAction(stringResource(R.string.auth_change), isSubmitEnabled) { onEvent(AuthScreenEvent.PhoneChangeSubmitted) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(stringResource(R.string.account_phone_verification_instruction), style = MaterialTheme.typography.headlineSmall)
                Text(stringResource(R.string.account_phone_verification_notice), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }

            Spacer(Modifier.height(EumSpacing.x24))

            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                AuthTextField(stringResource(R.string.auth_current_phone), currentPhoneNumber, {}, enabled = false)
                AuthPhoneVerificationFields(
                    label = stringResource(R.string.auth_new_phone),
                    state = phoneState,
                    onPhoneChanged = { onEvent(AuthScreenEvent.PhoneChangeFieldChanged("phone", it)) },
                    onResend = { onEvent(AuthScreenEvent.PhoneChangeResendClicked) },
                    onCodeChanged = { onEvent(AuthScreenEvent.PhoneChangeVerificationCodeChanged(it)) },
                    onConfirm = { onEvent(AuthScreenEvent.PhoneChangeVerificationConfirmClicked) },
                )
            }
        }
    }
}
