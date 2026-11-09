package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountSettingUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountSettingsScreenEvent
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// Figma 설정 프레임의 꺼짐 토글 트랙과 기기 알림 경고 배경은 기존 토큰과 값이 달라 이 화면 전용 상수로 둔다.
private val settingSwitchOffColor = Color(0xFF777777)
private val settingWarningContainerColor = Color(0xFFFFE9E9)

// Figma 설정 설명 문구는 기존 caption(12sp)보다 크게 측정되어 이 화면에서만 13sp로 둔다.
private val settingDescriptionStyle = EumTypographyTokens.caption.copy(fontSize = 13.sp, lineHeight = 16.sp)

@Composable
fun AccountSettingsScreen(uiModel: AccountSettingUiModel, onEvent: (AccountSettingsScreenEvent) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { CatalogTopBar(stringResource(R.string.settings_title), onBackClick = { onEvent(AccountSettingsScreenEvent.BackClicked) }, modifier = Modifier.statusBarsPadding()) },
            containerColor = MaterialTheme.eumColors.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            ResponsiveContentContainer(Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                    Modifier.fillMaxSize().navigationBarsPadding(),
                    contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x24),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x32),
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                            SettingSection(stringResource(R.string.settings_notification_group)) {
                                SettingSwitchRow(
                                    label = stringResource(R.string.settings_app_push),
                                    description = stringResource(R.string.settings_order_push_description),
                                    enabled = uiModel.isDeviceNotificationEnabled,
                                    onClick = { onEvent(AccountSettingsScreenEvent.DeviceNotificationChanged) },
                                )
                            }
                            if (uiModel.isDeviceNotificationWarningVisible) {
                                DeviceNotificationWarning(onClick = { onEvent(AccountSettingsScreenEvent.NotificationSettingsClicked) })
                            }
                        }
                    }
                    if (uiModel.isLoggedIn) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                MarketingIntro()
                                SettingSection(null) {
                                    SettingSwitchRow(stringResource(R.string.settings_marketing_app), enabled = uiModel.isAppMarketingEnabled, onClick = { onEvent(AccountSettingsScreenEvent.MarketingChanged("app", !uiModel.isAppMarketingEnabled)) })
                                    SettingDivider()
                                    SettingSwitchRow(stringResource(R.string.settings_marketing_sms), enabled = uiModel.isSmsMarketingEnabled, onClick = { onEvent(AccountSettingsScreenEvent.MarketingChanged("sms", !uiModel.isSmsMarketingEnabled)) })
                                    SettingDivider()
                                    SettingSwitchRow(stringResource(R.string.settings_marketing_email), enabled = uiModel.isEmailMarketingEnabled, onClick = { onEvent(AccountSettingsScreenEvent.MarketingChanged("email", !uiModel.isEmailMarketingEnabled)) })
                                    SettingDivider()
                                    SettingSwitchRow(stringResource(R.string.settings_marketing_night), stringResource(R.string.settings_marketing_night_description), uiModel.isNightMarketingEnabled, onClick = { onEvent(AccountSettingsScreenEvent.MarketingChanged("night", !uiModel.isNightMarketingEnabled)) })
                                }
                                Text(stringResource(R.string.settings_consent_changed, uiModel.lastConsentChangedLabel), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                            }
                        }
                    } else {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                MarketingIntro()
                                Column(
                                    Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.lineNeutral, RoundedCornerShape(EumRadius.x12)).padding(EumSpacing.x20),
                                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                                ) {
                                    Text(stringResource(R.string.settings_guest_marketing_message), style = MaterialTheme.typography.bodySmall)
                                    OutlinedButton(
                                        onClick = { onEvent(AccountSettingsScreenEvent.LoginClicked) },
                                        shape = RoundedCornerShape(EumRadius.x8),
                                        border = BorderStroke(1.dp, MaterialTheme.eumColors.textPrimary),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
                                        contentPadding = PaddingValues(horizontal = EumSpacing.x16),
                                    ) {
                                        Text(stringResource(R.string.account_login), style = MaterialTheme.typography.labelMedium)
                                    }
                                }
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
                            ).forEachIndexed { index, (id, label) ->
                                if (index > 0) SettingDivider()
                                SettingNavigationRow(stringResource(label)) { onEvent(AccountSettingsScreenEvent.PolicyClicked(id)) }
                            }
                        }
                    }
                    item {
                        SettingSection(stringResource(R.string.settings_app_info)) {
                            Row(Modifier.fillMaxWidth().heightIn(min = 56.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(R.string.settings_app_version), Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                                Text(uiModel.appVersionLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                            }
                        }
                    }
                    if (uiModel.isLoggedIn) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                SettingSection(stringResource(R.string.settings_account)) {
                                    SettingNavigationRow(stringResource(R.string.account_logout), showChevron = false) { onEvent(AccountSettingsScreenEvent.LogoutClicked) }
                                }
                                Text(
                                    stringResource(R.string.account_withdrawal),
                                    Modifier.clickable { onEvent(AccountSettingsScreenEvent.WithdrawalClicked) }.heightIn(min = 48.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.eumColors.textTertiary,
                                    textDecoration = TextDecoration.Underline,
                                )
                            }
                        }
                    }
                }
            }
        }
        if (uiModel.isMarketingToastVisible) {
            Snackbar(
                Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                shape = RoundedCornerShape(EumRadius.x8),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_circle_check), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(EumSpacing.x8))
                    Text(stringResource(R.string.settings_marketing_updated))
                }
            }
        }
        if (uiModel.isLogoutDialogVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(AccountSettingsScreenEvent.LogoutDismissed) },
                shape = RoundedCornerShape(16.dp),
                title = { Text(stringResource(R.string.settings_logout_confirm)) },
                text = { Text(stringResource(R.string.settings_logout_body), style = MaterialTheme.typography.bodySmall) },
                confirmButton = {
                    Button(
                        onClick = { onEvent(AccountSettingsScreenEvent.LogoutConfirmed) },
                        shape = RoundedCornerShape(EumRadius.x8),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.eumColors.textPrimary),
                        modifier = Modifier.heightIn(min = 48.dp).widthIn(min = 120.dp),
                    ) { Text(stringResource(R.string.settings_confirm_logout)) }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { onEvent(AccountSettingsScreenEvent.LogoutDismissed) },
                        shape = RoundedCornerShape(EumRadius.x8),
                        border = BorderStroke(1.dp, MaterialTheme.eumColors.textPrimary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
                        modifier = Modifier.heightIn(min = 48.dp).widthIn(min = 120.dp),
                    ) { Text(stringResource(R.string.settings_cancel)) }
                },
            )
        }
    }
}

@Composable
private fun MarketingIntro() {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
        Text(stringResource(R.string.settings_marketing_consent), style = MaterialTheme.typography.labelMedium)
        Text(stringResource(R.string.settings_marketing_description), style = settingDescriptionStyle, color = MaterialTheme.eumColors.textTertiary)
    }
}

@Composable
private fun DeviceNotificationWarning(onClick: () -> Unit) {
    val message = stringResource(R.string.settings_device_notification_warning_message)
    val settingsLink = stringResource(R.string.settings_device_notification_settings_link)
    Row(
        Modifier.fillMaxWidth()
            .background(settingWarningContainerColor, RoundedCornerShape(EumRadius.x8))
            .clickable(role = Role.Button) { onClick() }
            .heightIn(min = 48.dp)
            .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(painterResource(R.drawable.ic_info), contentDescription = null, tint = MaterialTheme.eumColors.danger, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(EumSpacing.x8))
        Text(
            buildAnnotatedString {
                append(message)
                append(" ")
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append(settingsLink) }
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.danger,
        )
    }
}

@Composable
private fun SettingSection(title: String?, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        if (title != null) Text(title, style = MaterialTheme.typography.labelMedium)
        Column(
            Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.lineNeutral, RoundedCornerShape(EumRadius.x12)).padding(horizontal = EumSpacing.x20),
            content = content,
        )
    }
}

@Composable
private fun SettingDivider() {
    HorizontalDivider(color = MaterialTheme.eumColors.lineNeutral)
}

@Composable
private fun settingSwitchColors(): SwitchColors {
    val tokens = MaterialTheme.eumColors
    return SwitchDefaults.colors(
        checkedThumbColor = tokens.inverse,
        checkedTrackColor = tokens.textPrimary,
        checkedBorderColor = tokens.textPrimary,
        uncheckedThumbColor = tokens.inverse,
        uncheckedTrackColor = settingSwitchOffColor,
        uncheckedBorderColor = settingSwitchOffColor,
    )
}

@Composable
private fun SettingSwitchRow(label: String, description: String? = null, enabled: Boolean, onClick: () -> Unit) {
    // 스위치 자체 높이(48dp 터치 영역)가 행 높이를 키우지 않도록 세로 여백은 텍스트 열에만 준다.
    Row(
        Modifier.fillMaxWidth().heightIn(min = 60.dp).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f).padding(vertical = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            if (description != null) Text(description, style = settingDescriptionStyle, color = MaterialTheme.eumColors.textTertiary)
        }
        // Figma 토글(약 36x20dp)에 맞추려고 오른쪽 끝 기준으로 축소한다. 행 전체가 클릭을 받으므로 터치 영역은 행이 담당한다.
        Switch(
            checked = enabled,
            onCheckedChange = { onClick() },
            modifier = Modifier.graphicsLayer(scaleX = 0.7f, scaleY = 0.7f, transformOrigin = TransformOrigin(1f, 0.5f)),
            colors = settingSwitchColors(),
        )
    }
}

@Composable
private fun SettingNavigationRow(label: String, showChevron: Boolean = true, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        if (showChevron) {
            Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, tint = MaterialTheme.eumColors.textTertiary)
        }
    }
}
