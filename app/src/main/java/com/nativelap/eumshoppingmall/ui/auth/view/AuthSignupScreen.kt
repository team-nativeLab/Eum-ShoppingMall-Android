package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthMemberType
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthScreenEvent
import com.nativelap.eumshoppingmall.ui.auth.viewmodel.AuthSignupUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AuthSignupScreen(uiModel: AuthSignupUiModel, onEvent: (AuthScreenEvent) -> Unit) {
    Scaffold(
        topBar = { AuthTopBar(stringResource(R.string.account_sign_up), onBack = { onEvent(AuthScreenEvent.BackClicked) }) },
        bottomBar = {
            AuthBottomBar {
                Text(
                    stringResource(R.string.auth_signup_notice),
                    Modifier.fillMaxWidth(),
                    style = EumTypographyTokens.caption,
                    color = MaterialTheme.eumColors.textTertiary,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(EumSpacing.x8))

                AuthPrimaryAction(stringResource(R.string.auth_signup_submit), uiModel.isSubmitEnabled) { onEvent(AuthScreenEvent.SignupSubmitted) }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x20),
        ) {
            AuthSegmentedControl(
                labels = listOf(stringResource(R.string.auth_member_personal), stringResource(R.string.auth_member_business)),
                selectedIndex = if (uiModel.memberType == AuthMemberType.PERSONAL) 0 else 1,
                onSelected = { onEvent(AuthScreenEvent.SignupMemberTypeChanged(if (it == 0) AuthMemberType.PERSONAL else AuthMemberType.BUSINESS)) },
            )
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
            AuthTextField(
                stringResource(if (uiModel.memberType == AuthMemberType.BUSINESS) R.string.auth_contact_name_label else R.string.auth_full_name_label),
                uiModel.name,
                { onEvent(AuthScreenEvent.SignupFieldChanged("name", it)) },
                placeholder = stringResource(R.string.auth_name_placeholder),
                isRequired = true,
            )
            AuthPhoneVerificationFields(
                label = stringResource(R.string.auth_phone_number),
                state = uiModel.phone,
                onPhoneChanged = { onEvent(AuthScreenEvent.SignupFieldChanged("phone", it)) },
                onResend = { onEvent(AuthScreenEvent.SignupPhoneResendClicked) },
                onCodeChanged = { onEvent(AuthScreenEvent.SignupVerificationCodeChanged(it)) },
                onConfirm = { onEvent(AuthScreenEvent.SignupVerificationConfirmClicked) },
                isRequired = true,
            )
            if (uiModel.memberType == AuthMemberType.BUSINESS) {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x20)) {
                    Text(stringResource(R.string.auth_business_information), style = MaterialTheme.typography.labelMedium)
                    AuthFieldActionRow(
                        actionLabel = stringResource(R.string.auth_verify),
                        onActionClick = { onEvent(AuthScreenEvent.SignupBusinessVerificationClicked) },
                    ) {
                        AuthTextField(stringResource(R.string.auth_business_registration_number), uiModel.businessRegistrationNumber, { onEvent(AuthScreenEvent.SignupFieldChanged("registration_number", it)) }, placeholder = stringResource(R.string.auth_business_registration_placeholder), keyboardType = KeyboardType.Number)
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

            Spacer(Modifier.height(EumSpacing.x4))
        }
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
        OutlinedButton(
            onClick = onActionClick,
            modifier = Modifier.padding(top = labelLineHeight + EumSpacing.x8).heightIn(min = 48.dp),
        ) {
            Text(actionLabel, style = MaterialTheme.typography.labelMedium)
        }
    }
}
