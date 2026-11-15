package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthPhoneVerificationUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val PhoneActionButtonPadding = PaddingValues(horizontal = 16.dp)
private val PhoneConfirmButtonMinWidth = 73.dp

@Composable
internal fun AuthPhoneVerificationFields(
    label: String,
    state: AuthPhoneVerificationUiModel,
    onPhoneChanged: (String) -> Unit,
    onResend: () -> Unit,
    onCodeChanged: (CharArray) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
) {
    val verificationCodeLabel = stringResource(R.string.auth_verification_code_placeholder)
    val showVerificationCodeLabel = stringResource(R.string.auth_verification_code_show)
    val hideVerificationCodeLabel = stringResource(R.string.auth_verification_code_hide)
    val phoneInvalidMessage = stringResource(R.string.auth_phone_invalid)
    val codeInvalidMessage = stringResource(R.string.auth_verification_code_invalid)
    var isVerificationCodeVisible by remember(
        state.phoneNumber,
        state.isCodeRequested,
        state.isVerified,
    ) {
        mutableStateOf(false)
    }
    val canToggleVerificationCodeVisibility = state.isCodeRequested && !state.isVerified && state.verificationCode.isNotEmpty()
    // 인증 완료 후 비활성이 된 번호·코드 필드도 38% 알파 대신 text-1로 보여 준다(코드는 Figma처럼 평문).
    val codeFieldColors = OutlinedTextFieldDefaults.colors(disabledTextColor = MaterialTheme.eumColors.textPrimary)
    val requiredDescription = stringResource(R.string.auth_field_required)

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        AuthFieldLabel(label, isRequired = isRequired)
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
            AuthOutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { phoneNumber ->
                    isVerificationCodeVisible = false
                    onPhoneChanged(phoneNumber)
                },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp).semantics {
                    contentDescription = label
                    if (isRequired) stateDescription = requiredDescription
                },
                enabled = !state.isVerified,
                placeholder = stringResource(R.string.auth_phone_placeholder),
                isError = state.isPhoneInvalid,
                errorDescription = phoneInvalidMessage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                colors = codeFieldColors,
            )
            OutlinedButton(
                onClick = {
                    isVerificationCodeVisible = false
                    onResend()
                },
                enabled = !state.isVerified,
                modifier = Modifier.heightIn(min = 48.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PhoneActionButtonPadding,
            ) {
                Text(stringResource(R.string.auth_phone_resend), style = MaterialTheme.typography.labelMedium)
            }
        }
        if (state.isPhoneInvalid) {
            AuthHelperMessage(phoneInvalidMessage, AuthHelperTone.Error)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
            AuthOutlinedTextField(
                value = state.verificationCode.concatToString(),
                onValueChange = { codeInput ->
                    onCodeChanged(codeInput.filter { it in '0'..'9' }.take(6).toCharArray())
                },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp).semantics {
                    contentDescription = verificationCodeLabel
                },
                enabled = state.isCodeRequested && !state.isVerified,
                placeholder = stringResource(R.string.auth_verification_code_placeholder),
                isError = state.isCodeInvalid,
                errorDescription = codeInvalidMessage,
                visualTransformation = if (isVerificationCodeVisible || state.isVerified) {
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
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.eumColors.textSecondary,
                            )
                        }
                    }
                } else {
                    null
                },
                colors = codeFieldColors,
            )
            OutlinedButton(
                onClick = onConfirm,
                enabled = state.isCodeRequested && !state.isVerified && state.verificationCode.isNotEmpty(),
                // 글꼴이 커져도 라벨이 잘리지 않도록 73dp는 최소 폭으로 둔다.
                modifier = Modifier.widthIn(min = PhoneConfirmButtonMinWidth).heightIn(min = 48.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PhoneActionButtonPadding,
            ) {
                Text(stringResource(R.string.auth_verify), style = MaterialTheme.typography.labelMedium)
            }
        }
        if (state.isCodeInvalid) {
            AuthHelperMessage(codeInvalidMessage, AuthHelperTone.Error)
        }
        when {
            state.isVerified -> AuthHelperMessage(stringResource(R.string.auth_verified), AuthHelperTone.Success)

            // 남은 시간 문구의 표시 상태와 스타일은 Figma 값이 미확인이라 기존 표시를 유지한다.
            state.isCodeRequested -> Text(
                stringResource(R.string.auth_verification_remaining, state.remainingTime),
                Modifier.padding(start = EumSpacing.x2),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textTertiary,
            )
        }
    }
}
