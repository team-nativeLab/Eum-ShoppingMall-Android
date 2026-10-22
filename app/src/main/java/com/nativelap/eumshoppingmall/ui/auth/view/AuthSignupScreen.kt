package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthMemberType
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthSignupUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthSignupScreen(uiModel: AuthSignupUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.account_sign_up), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            Column(Modifier.fillMaxWidth().navigationBarsPadding().imePadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                AuthPrimaryAction(stringResource(R.string.account_sign_up), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.SignupSubmitted) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            AuthSegmentedControl(
                labels = listOf(stringResource(R.string.auth_member_personal), stringResource(R.string.auth_member_business)),
                selectedIndex = if (uiModel.memberType == AuthMemberType.PERSONAL) 0 else 1,
                onSelected = { onEvent(AuthScreenEvent.SignupMemberTypeChanged(if (it == 0) AuthMemberType.PERSONAL else AuthMemberType.BUSINESS)) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.Bottom) {
                androidx.compose.foundation.layout.Box(Modifier.weight(1f)) {
                    AuthTextField(
                        stringResource(R.string.auth_email_label),
                        uiModel.email,
                        { onEvent(AuthScreenEvent.SignupFieldChanged("email", it)) },
                        placeholder = stringResource(R.string.auth_email_placeholder),
                        helperText = if (uiModel.isEmailAvailable) stringResource(R.string.auth_email_available) else null,
                        keyboardType = KeyboardType.Email,
                    )
                }
                OutlinedButton(onClick = { onEvent(AuthScreenEvent.EmailDuplicateCheckClicked) }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(stringResource(R.string.auth_email_duplicate_check))
                }
            }
            AuthPasswordField(
                stringResource(R.string.auth_password_label),
                uiModel.password,
                isVisible = uiModel.isPasswordVisible,
                onValueChange = { onEvent(AuthScreenEvent.SignupSecretChanged("password", it)) },
                onVisibilityChange = { onEvent(AuthScreenEvent.SignupPasswordVisibilityChanged("password")) },
                helperText = stringResource(R.string.auth_password_rule),
            )
            AuthPasswordField(
                stringResource(R.string.account_password_confirmation),
                uiModel.passwordConfirmation,
                isVisible = uiModel.isPasswordConfirmationVisible,
                onValueChange = { onEvent(AuthScreenEvent.SignupSecretChanged("password_confirmation", it)) },
                onVisibilityChange = { onEvent(AuthScreenEvent.SignupPasswordVisibilityChanged("confirmation")) },
                errorText = if (uiModel.passwordConfirmation.isNotEmpty() && !uiModel.password.contentEquals(uiModel.passwordConfirmation)) stringResource(R.string.auth_password_mismatch) else null,
            )
            AuthTextField(
                stringResource(R.string.auth_full_name_label),
                uiModel.name,
                { onEvent(AuthScreenEvent.SignupFieldChanged("name", it)) },
                placeholder = stringResource(R.string.auth_name_placeholder),
            )
            AuthPhoneVerificationFields(
                label = stringResource(R.string.auth_phone_number),
                state = uiModel.phone,
                onPhoneChanged = { onEvent(AuthScreenEvent.SignupFieldChanged("phone", it)) },
                onResend = { onEvent(AuthScreenEvent.SignupPhoneResendClicked) },
                onCodeChanged = { onEvent(AuthScreenEvent.SignupVerificationCodeChanged(it)) },
                onConfirm = { onEvent(AuthScreenEvent.SignupVerificationConfirmClicked) },
            )
            if (uiModel.memberType == AuthMemberType.BUSINESS) {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                    Text(stringResource(R.string.auth_business_information), style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.Bottom) {
                        androidx.compose.foundation.layout.Box(Modifier.weight(1f)) {
                            AuthTextField(stringResource(R.string.auth_business_registration_number), uiModel.businessRegistrationNumber, { onEvent(AuthScreenEvent.SignupFieldChanged("registration_number", it)) }, placeholder = stringResource(R.string.auth_business_registration_placeholder), keyboardType = KeyboardType.Number)
                        }
                        OutlinedButton(onClick = { onEvent(AuthScreenEvent.SignupBusinessVerificationClicked) }, modifier = Modifier.heightIn(min = 48.dp)) {
                            Text(stringResource(R.string.auth_verify))
                        }
                    }
                    AuthTextField(stringResource(R.string.auth_company_name), uiModel.companyName, { onEvent(AuthScreenEvent.SignupFieldChanged("company_name", it)) }, placeholder = stringResource(R.string.auth_company_name_placeholder))
                    AuthTextField(stringResource(R.string.auth_representative_name), uiModel.representativeName, { onEvent(AuthScreenEvent.SignupFieldChanged("representative", it)) }, placeholder = stringResource(R.string.auth_representative_name_placeholder))
                    AuthTextField(stringResource(R.string.auth_industry), uiModel.industry, { onEvent(AuthScreenEvent.SignupFieldChanged("industry", it)) }, placeholder = stringResource(R.string.auth_industry_placeholder))
                    AuthTextField(stringResource(R.string.auth_tax_email), uiModel.taxEmail, { onEvent(AuthScreenEvent.SignupFieldChanged("tax_email", it)) }, placeholder = stringResource(R.string.auth_email_placeholder), keyboardType = KeyboardType.Email)
                    Surface(
                        modifier = Modifier.fillMaxWidth().heightIn(min = 88.dp).clickable { onEvent(AuthScreenEvent.BusinessRegistrationFileClicked) },
                        shape = RoundedCornerShape(EumRadius.x8),
                        border = BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
                        color = MaterialTheme.eumColors.surfaceSecondary,
                    ) {
                        Column(Modifier.padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stringResource(R.string.auth_registration_file), style = MaterialTheme.typography.labelMedium)
                            Text(
                                stringResource(if (uiModel.isRegistrationFileSelected) R.string.auth_registration_file_selected else R.string.auth_registration_file_upload),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.eumColors.textTertiary,
                            )
                        }
                    }
                }
            }
            AuthConsentGroup(uiModel, onEvent)
            Text(stringResource(R.string.auth_signup_notice), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
            androidx.compose.foundation.layout.Spacer(Modifier.padding(bottom = EumSpacing.x16))
        }
    }
}
