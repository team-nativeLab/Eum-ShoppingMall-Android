package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.MyPageUiModel
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

@Composable
fun MyPageRoute(
    uiModel: MyPageUiModel,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onLoginClick: () -> Unit,
    onDrawerClick: () -> Unit,
    onShortcutClick: (shortcutId: String) -> Unit,
    onMenuRowClick: (rowId: String) -> Unit,
) {
    MyPageScreen(
        uiModel = uiModel,
        cartItemCount = cartItemCount,
        onEvent = { event ->
            when (event) {
                MyPageScreenEvent.LoginClicked -> onLoginClick()

                MyPageScreenEvent.DrawerClicked -> onDrawerClick()

                is MyPageScreenEvent.ShortcutClicked -> onShortcutClick(event.shortcutId)

                is MyPageScreenEvent.MenuRowClicked -> onMenuRowClick(event.rowId)

                is MyPageScreenEvent.BottomNavigationClicked -> onTabSelected(event.tab)

                // 내 정보 관리(A-23)와 설정(A-35)은 이번 범위가 아니다.
                MyPageScreenEvent.ProfileClicked,
                MyPageScreenEvent.SettingsClicked,
                -> Unit
            }
        },
    )
}
