package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPhoneVerificationUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun AuthPhoneVerificationFields(
    label: String,
    state: AuthPhoneVerificationUiModel,
    onPhoneChanged: (String) -> Unit,
    onResend: () -> Unit,
    onCodeChanged: (CharArray) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val verificationCodeLabel = stringResource(R.string.auth_verification_code_placeholder)
    val showVerificationCodeLabel = stringResource(R.string.auth_verification_code_show)
    val hideVerificationCodeLabel = stringResource(R.string.auth_verification_code_hide)
    var isVerificationCodeVisible by remember(
        state.phoneNumber,
        state.isCodeRequested,
        state.isVerified,
    ) {
        mutableStateOf(false)
    }
    val canToggleVerificationCodeVisibility = state.isCodeRequested && !state.isVerified

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { phoneNumber ->
                    isVerificationCodeVisible = false
                    onPhoneChanged(phoneNumber)
                },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp).semantics { contentDescription = label },
                singleLine = true,
                placeholder = { Text(stringResource(R.string.auth_phone_placeholder)) },
                isError = state.isPhoneInvalid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                shape = MaterialTheme.shapes.medium,
            )
            OutlinedButton(
                onClick = {
                    isVerificationCodeVisible = false
                    onResend()
                },
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(stringResource(R.string.auth_phone_resend))
            }
        }
        if (state.isPhoneInvalid) {
            Text(stringResource(R.string.auth_phone_invalid), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.danger)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            OutlinedTextField(
                value = state.verificationCode.concatToString(),
                onValueChange = { value ->
                    onCodeChanged(value.filter { it in '0'..'9' }.take(6).toCharArray())
                },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp).semantics {
                    contentDescription = verificationCodeLabel
                },
                singleLine = true,
                placeholder = { Text(stringResource(R.string.auth_verification_code_placeholder)) },
                isError = state.isCodeInvalid,
                enabled = state.isCodeRequested && !state.isVerified,
                visualTransformation = if (isVerificationCodeVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                trailingIcon = if (canToggleVerificationCodeVisibility) {
                    {
                        IconButton(
                            onClick = {
                                isVerificationCodeVisible = !isVerificationCodeVisible
                            },
                            modifier = Modifier.size(48.dp).semantics {
                                contentDescription = if (isVerificationCodeVisible) {
                                    hideVerificationCodeLabel
                                } else {
                                    showVerificationCodeLabel
                                }
                            },
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (isVerificationCodeVisible) R.drawable.ic_eye else R.drawable.ic_eye_off,
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.eumColors.textSecondary,
                            )
                        }
                    }
                } else {
                    null
                },
                shape = MaterialTheme.shapes.medium,
            )
            OutlinedButton(onClick = onConfirm, enabled = state.isCodeRequested && !state.isVerified, modifier = Modifier.heightIn(min = 48.dp)) {
                Text(stringResource(R.string.auth_verify))
            }
        }
        if (state.isCodeInvalid) {
            Text(stringResource(R.string.auth_verification_code_invalid), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.danger)
        }
        val message = when {
            state.isVerified -> stringResource(R.string.auth_verified)
            state.isCodeRequested -> stringResource(R.string.auth_verification_remaining, state.remainingTime)
            else -> null
        }
        if (message != null) {
            Text(
                message,
                Modifier.padding(start = EumSpacing.x2),
                style = MaterialTheme.typography.labelSmall,
                color = if (state.isVerified) MaterialTheme.eumColors.success else MaterialTheme.eumColors.textTertiary,
            )
        }
    }
}
