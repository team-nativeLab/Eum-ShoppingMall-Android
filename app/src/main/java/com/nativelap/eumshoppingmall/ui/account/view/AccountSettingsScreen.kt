package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountSettingUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountSettingsScreenEvent
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountSettingsScreen(uiModel: AccountSettingUiModel, onEvent: (AccountSettingsScreenEvent) -> Unit) {
    androidx.compose.foundation.layout.Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { CatalogTopBar(stringResource(R.string.settings_title), onBackClick = { onEvent(AccountSettingsScreenEvent.BackClicked) }, modifier = Modifier.statusBarsPadding()) },
            containerColor = MaterialTheme.eumColors.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).navigationBarsPadding(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x24),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x24),
            ) {
                if (!uiModel.isLoggedIn) {
                    item {
                        Column(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12)).padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                            Text(stringResource(R.string.settings_guest_login_message), style = MaterialTheme.typography.bodyMedium)
                            AccountPrimaryButton(stringResource(R.string.account_login), onClick = { onEvent(AccountSettingsScreenEvent.LoginClicked) })
                        }
                    }
                }
                item {
                    SettingSection(stringResource(R.string.settings_notification_group)) {
                        SettingSwitchRow(stringResource(R.string.settings_app_push), stringResource(R.string.settings_order_push_description), uiModel.isDeviceNotificationEnabled, onClick = { onEvent(AccountSettingsScreenEvent.DeviceNotificationChanged) })
                    }
                    if (uiModel.isDeviceNotificationWarningVisible) {
                        Text(
                            stringResource(R.string.settings_device_notification_disabled),
                            Modifier.fillMaxWidth().padding(top = EumSpacing.x8).border(1.dp, MaterialTheme.eumColors.dangerTint, RoundedCornerShape(EumRadius.x8)).clickable(role = Role.Button) { onEvent(AccountSettingsScreenEvent.NotificationSettingsClicked) }.heightIn(min = 48.dp).padding(EumSpacing.x12),
                            color = MaterialTheme.eumColors.danger,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (uiModel.isLoggedIn) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            Text(stringResource(R.string.settings_marketing_consent), style = MaterialTheme.typography.labelMedium)
                            Text(stringResource(R.string.settings_marketing_description), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                            SettingSection(null) {
                                SettingSwitchRow(stringResource(R.string.settings_marketing_app), enabled = uiModel.isAppMarketingEnabled, onClick = { onEvent(AccountSettingsScreenEvent.MarketingChanged("app", !uiModel.isAppMarketingEnabled)) })
                                SettingSwitchRow(stringResource(R.string.settings_marketing_sms), enabled = uiModel.isSmsMarketingEnabled, onClick = { onEvent(AccountSettingsScreenEvent.MarketingChanged("sms", !uiModel.isSmsMarketingEnabled)) })
                                SettingSwitchRow(stringResource(R.string.settings_marketing_email), enabled = uiModel.isEmailMarketingEnabled, onClick = { onEvent(AccountSettingsScreenEvent.MarketingChanged("email", !uiModel.isEmailMarketingEnabled)) })
                                SettingSwitchRow(stringResource(R.string.settings_marketing_night), stringResource(R.string.settings_marketing_night_description), uiModel.isNightMarketingEnabled, onClick = { onEvent(AccountSettingsScreenEvent.MarketingChanged("night", !uiModel.isNightMarketingEnabled)) })
                            }
                            Text(stringResource(R.string.settings_consent_changed, uiModel.lastConsentChangedLabel), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                        }
                    }
                }
                item {
                    SettingSection(stringResource(R.string.settings_terms_policy)) {
                        listOf(
                            "terms" to R.string.settings_terms,
                            "privacy" to R.string.settings_privacy,
                            "guide" to R.string.settings_usage_guide,
                            "open_source" to R.string.settings_open_source,
                        ).forEach { (id, label) -> SettingNavigationRow(stringResource(label)) { onEvent(AccountSettingsScreenEvent.PolicyClicked(id)) } }
                    }
                }
                item {
                    SettingSection(stringResource(R.string.settings_app_info)) {
                        Row(Modifier.fillMaxWidth().heightIn(min = 52.dp).padding(horizontal = EumSpacing.x16), verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.settings_app_version), Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                            Text(uiModel.appVersionLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                        }
                    }
                }
                if (uiModel.isLoggedIn) {
                    item {
                        SettingSection(stringResource(R.string.settings_account)) {
                            SettingNavigationRow(stringResource(R.string.account_logout)) { onEvent(AccountSettingsScreenEvent.LogoutClicked) }
                        }
                        Text(stringResource(R.string.account_withdrawal), Modifier.clickable { onEvent(AccountSettingsScreenEvent.WithdrawalClicked) }.padding(vertical = EumSpacing.x8), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                    }
                }
            }
        }
        if (uiModel.isMarketingToastVisible) {
            androidx.compose.material3.Snackbar(
                Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
            ) { Text(stringResource(R.string.settings_marketing_updated)) }
        }
        if (uiModel.isLogoutDialogVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(AccountSettingsScreenEvent.LogoutDismissed) },
                title = { Text(stringResource(R.string.account_logout)) },
                text = { Text(stringResource(R.string.settings_logout_confirm)) },
                confirmButton = { TextButton(onClick = { onEvent(AccountSettingsScreenEvent.LogoutConfirmed) }) { Text(stringResource(R.string.settings_confirm_logout)) } },
                dismissButton = { TextButton(onClick = { onEvent(AccountSettingsScreenEvent.LogoutDismissed) }) { Text(stringResource(R.string.settings_cancel)) } },
            )
        }
    }
}

@Composable
private fun SettingSection(title: String?, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        if (title != null) Text(title, style = MaterialTheme.typography.labelMedium)
        Column(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12)).padding(horizontal = EumSpacing.x16), content = content)
    }
}

@Composable
private fun SettingSwitchRow(label: String, description: String? = null, enabled: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable { onClick() }.padding(vertical = EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x2)) {
            Text(label, style = MaterialTheme.typography.bodySmall)
            if (description != null) Text(description, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
        }
        Switch(checked = enabled, onCheckedChange = { onClick() })
    }
}

@Composable
private fun SettingNavigationRow(label: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 52.dp).clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
        androidx.compose.material3.Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, tint = MaterialTheme.eumColors.textTertiary)
    }
}
