package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountSettingUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountSettingsScreenEvent

/**
 * 설정(A-35). 알림·마케팅 수신 설정은 저장·전송 경로가 없어 토글을 비활성으로 두고 준비 중으로 고지한다.
 * 로그아웃·탈퇴·기기 알림 설정 열기도 연결 전이라 준비 중 안내만 한다(인증 경계는 바꾸지 않는다).
 * 약관·개인정보처리방침·이용안내는 [onPolicyClick]이 목적지로 연결한다(ID: terms, privacy, guide). 그 밖의 ID는 준비 중이다.
 */
@Composable
fun AccountSettingsRoute(
    isLoggedIn: Boolean,
    appVersionLabel: String,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onPolicyClick: (policyId: String) -> Boolean,
    onUnavailableAction: () -> Unit,
) {
    val uiModel = remember(isLoggedIn, appVersionLabel) {
        AccountSettingUiModel(
            isLoggedIn = isLoggedIn,
            isDeviceNotificationEnabled = false,
            isAppMarketingEnabled = false,
            isSmsMarketingEnabled = false,
            isEmailMarketingEnabled = false,
            isNightMarketingEnabled = false,
            isDeviceNotificationWarningVisible = false,
            isMarketingToastVisible = false,
            isLogoutDialogVisible = false,
            appVersionLabel = appVersionLabel,
            lastConsentChangedLabel = "",
            areTogglesAvailable = false,
        )
    }
    AccountSettingsScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                AccountSettingsScreenEvent.BackClicked -> onBackClick()

                AccountSettingsScreenEvent.LoginClicked -> onLoginClick()

                is AccountSettingsScreenEvent.PolicyClicked -> if (!onPolicyClick(event.policyId)) onUnavailableAction()

                AccountSettingsScreenEvent.DeviceNotificationChanged,
                is AccountSettingsScreenEvent.MarketingChanged,
                AccountSettingsScreenEvent.LogoutClicked,
                AccountSettingsScreenEvent.LogoutConfirmed,
                AccountSettingsScreenEvent.LogoutDismissed,
                AccountSettingsScreenEvent.NotificationSettingsClicked,
                AccountSettingsScreenEvent.WithdrawalClicked,
                -> onUnavailableAction()
            }
        },
    )
}
