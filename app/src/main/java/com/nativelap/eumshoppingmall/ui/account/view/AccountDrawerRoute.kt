package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountDrawerScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.common.DialogLightStatusBarIcons
import com.nativelap.eumshoppingmall.ui.common.DisableDialogWindowDim

@Composable
fun AccountDrawerRoute(
    profile: AccountProfileUiModel,
    isLoggedIn: Boolean,
    onCloseClick: () -> Unit,
    onMenuClick: (menuId: String) -> Unit,
) {
    // 드로어는 Dialog 창에 뜬다. Screen이 디자인 토큰 dim을 직접 그리므로 창 기본 dim은 끈다.
    DisableDialogWindowDim()
    // 드로어 상단은 밝은 표면이므로 상태 바 아이콘을 어둡게 고정한다(D10). 시스템 다크 모드 설정에 기대지 않는다.
    DialogLightStatusBarIcons()
    AccountDrawerScreen(
        profile = profile,
        isLoggedIn = isLoggedIn,
        onEvent = { event ->
            when (event) {
                AccountDrawerScreenEvent.CloseClicked -> onCloseClick()

                is AccountDrawerScreenEvent.MenuClicked -> onMenuClick(event.menuId)

                // 알림(A-32)·내 정보(A-23)·설정(A-35)·로그아웃은 이번 범위가 아니다.
                AccountDrawerScreenEvent.NotificationClicked,
                AccountDrawerScreenEvent.ProfileClicked,
                AccountDrawerScreenEvent.SettingsClicked,
                AccountDrawerScreenEvent.LogoutClicked,
                -> Unit
            }
        },
    )
}
