package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileEditUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileScreenEvent
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountProfileScreen(
    uiModel: AccountProfileEditUiModel,
    isPasswordVerification: Boolean,
    onEvent: (AccountProfileScreenEvent) -> Unit,
) {
    val title = if (isPasswordVerification) stringResource(R.string.account_profile_manage) else stringResource(R.string.account_profile_edit)
    Scaffold(
        topBar = { CatalogTopBar(title, onBackClick = { onEvent(AccountProfileScreenEvent.BackClicked) }, modifier = Modifier.statusBarsPadding()) },
        bottomBar = {
            AccountStickyPrimaryAction(
                label = stringResource(if (isPasswordVerification) R.string.common_confirm else R.string.account_save_changes),
                onClick = { onEvent(if (isPasswordVerification) AccountProfileScreenEvent.PasswordSubmitted else AccountProfileScreenEvent.SaveClicked) },
                enabled = uiModel.isSaveEnabled,
            )
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        if (isPasswordVerification) {
            Column(
                Modifier.fillMaxSize().padding(contentPadding).imePadding().padding(horizontal = EumSpacing.x20),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(top = EumSpacing.x24),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    Icon(
                        painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp).background(MaterialTheme.eumColors.backgroundSubtle, androidx.compose.foundation.shape.CircleShape).padding(20.dp),
                        tint = MaterialTheme.eumColors.textPrimary,
                    )
                    Text(stringResource(R.string.account_password_reenter), style = MaterialTheme.typography.titleLarge)
                    Text(stringResource(R.string.account_password_verify_description), color = MaterialTheme.eumColors.textSecondary, style = MaterialTheme.typography.bodySmall, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
                AccountTextField(stringResource(R.string.account_email), uiModel.profile.email, {}, enabled = false)
                AccountTextField(
                    stringResource(R.string.account_password),
                    uiModel.passwordValue.concatToString(),
                    { onEvent(AccountProfileScreenEvent.PasswordChangedValue(it.toCharArray())) },
                    placeholder = stringResource(R.string.account_password_placeholder),
                    isError = uiModel.isPasswordInvalid,
                    errorMessage = if (uiModel.isPasswordInvalid) stringResource(R.string.account_password_incorrect) else null,
                    keyboardType = KeyboardType.Password,
                    password = true,
                )
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(contentPadding).imePadding(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x24),
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        Text(stringResource(R.string.account_basic_information), style = MaterialTheme.typography.titleMedium)
                        AccountTextField(stringResource(R.string.account_email), uiModel.profile.email, {}, enabled = false)
                        AccountTextField(stringResource(R.string.account_name), uiModel.profile.name, { onEvent(AccountProfileScreenEvent.NameChanged(it)) })
                        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            AccountTextField(stringResource(R.string.account_phone_number), uiModel.profile.phoneNumber, { onEvent(AccountProfileScreenEvent.PhoneChanged(it)) }, modifier = Modifier.weight(1f))
                            androidx.compose.material3.OutlinedButton(onClick = { onEvent(AccountProfileScreenEvent.PhoneChangeClicked) }, modifier = Modifier.heightIn(min = 48.dp)) {
                                Text(stringResource(R.string.account_change))
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth().heightIn(min = 60.dp).clickable { onEvent(AccountProfileScreenEvent.PasswordChanged) }.padding(horizontal = EumSpacing.x12),
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                        ) {
                            Text(stringResource(R.string.account_password_change), Modifier.weight(1f))
                            Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, tint = MaterialTheme.eumColors.textTertiary)
                        }
                    }
                }
                if (uiModel.profile.isBusinessMember) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                            Text(stringResource(R.string.account_business_information), style = MaterialTheme.typography.titleMedium)
                            Text(stringResource(R.string.account_business_information_notice), color = MaterialTheme.eumColors.textTertiary, style = MaterialTheme.typography.bodySmall)
                            AccountTextField(stringResource(R.string.account_company_name), uiModel.profile.companyName, { onEvent(AccountProfileScreenEvent.CompanyChanged(it)) })
                            AccountTextField(stringResource(R.string.business_registration_number), uiModel.profile.businessRegistrationNumber, {}, enabled = false)
                            AccountTextField(stringResource(R.string.account_department), uiModel.profile.department, { onEvent(AccountProfileScreenEvent.DepartmentChanged(it)) })
                        }
                    }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(stringResource(R.string.account_receive_consent), style = MaterialTheme.typography.titleMedium)
                        AccountConsentRow(stringResource(R.string.account_sms_consent), uiModel.profile.isSmsConsentEnabled) { onEvent(AccountProfileScreenEvent.SmsConsentChanged(it)) }
                        AccountConsentRow(stringResource(R.string.account_email_consent), uiModel.profile.isEmailConsentEnabled) { onEvent(AccountProfileScreenEvent.EmailConsentChanged(it)) }
                        Text(stringResource(R.string.account_order_notice_consent_independent), color = MaterialTheme.eumColors.textTertiary, style = MaterialTheme.typography.labelSmall)
                    }
                }
                item {
                    Text(stringResource(R.string.account_withdrawal), Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable { onEvent(AccountProfileScreenEvent.WithdrawalClicked) }.padding(vertical = EumSpacing.x8), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                }
            }
        }
    }
}

@Composable
private fun AccountTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(EumSpacing.x6)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            enabled = enabled,
            singleLine = true,
            isError = isError,
            placeholder = placeholder?.let { { Text(it) } },
            supportingText = errorMessage?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        )
    }
}

@Composable
private fun AccountConsentRow(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 48.dp), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
        Text(label, Modifier.padding(top = EumSpacing.x12), style = MaterialTheme.typography.bodySmall)
    }
}
