package com.nativelap.eumshoppingmall.ui.account.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountDrawerScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountProfileUiModel
import com.nativelap.eumshoppingmall.ui.common.DialogLightStatusBarIcons
import com.nativelap.eumshoppingmall.ui.common.DisableDialogWindowDim
import com.nativelap.eumshoppingmall.ui.theme.rememberReducedMotion

/**
 * 사이드바 Route. 열릴 때 슬라이드 인하고, 닫기·dim 탭·시스템 뒤로가기·메뉴 선택은 모두 슬라이드 아웃이 끝난 뒤에
 * [onCloseClick] 또는 [onMenuClick]을 부른다. 그래서 호출부는 그 시점에 back stack에서 Drawer를 걷어 내면 된다.
 */
@Composable
fun AccountDrawerRoute(
    profile: AccountProfileUiModel,
    isLoggedIn: Boolean,
    onCloseClick: () -> Unit,
    onMenuClick: (menuId: String) -> Unit,
    onUnavailableAction: () -> Unit,
) {
    // 드로어는 Dialog 창에 뜬다. Screen이 디자인 토큰 dim을 직접 그리므로 창 기본 dim은 끈다.
    DisableDialogWindowDim()
    // 드로어 상단은 밝은 표면이므로 상태 바 아이콘을 어둡게 고정한다(D10). 시스템 다크 모드 설정에 기대지 않는다.
    DialogLightStatusBarIcons()
    val isReducedMotion = rememberReducedMotion()
    // 처음에는 닫힘 상태에서 목표만 열림으로 바꿔 입장 애니메이션을 만든다.
    val visibleState = remember { MutableTransitionState(false).apply { targetState = true } }
    // 퇴장이 끝나면 실행할 동작. 값이 있으면 이미 닫는 중이라 다른 닫기 요청은 무시한다.
    val pendingAfterClose = remember { mutableStateOf<(() -> Unit)?>(null) }

    fun closeThen(action: () -> Unit) {
        if (pendingAfterClose.value != null) return
        pendingAfterClose.value = action
        visibleState.targetState = false
    }

    LaunchedEffect(visibleState.currentState, visibleState.targetState) {
        if (!visibleState.currentState && !visibleState.targetState) {
            pendingAfterClose.value?.invoke()
        }
    }
    // 시스템 뒤로가기는 Dialog의 기본 닫기 대신 퇴장 애니메이션 뒤에 닫는다.
    BackHandler { closeThen(onCloseClick) }
    AccountDrawerScreen(
        profile = profile,
        isLoggedIn = isLoggedIn,
        visibleState = visibleState,
        isReducedMotion = isReducedMotion,
        onEvent = { event ->
            when (event) {
                AccountDrawerScreenEvent.CloseClicked -> closeThen(onCloseClick)

                is AccountDrawerScreenEvent.MenuClicked -> closeThen { onMenuClick(event.menuId) }

                // 내 정보(A-23)와 로그아웃은 아직 연결되어 있지 않다.
                AccountDrawerScreenEvent.ProfileClicked,
                AccountDrawerScreenEvent.LogoutClicked,
                -> closeThen(onUnavailableAction)
            }
        },
    )
}
