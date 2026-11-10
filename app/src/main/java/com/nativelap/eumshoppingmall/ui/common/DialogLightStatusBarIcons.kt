package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat

/**
 * 이 Composable이 놓인 Dialog 창에서 상태 바 아이콘을 어두운 색(light appearance)으로 명시한다.
 *
 * 시스템 다크 모드 설정에 기대지 않고 화면이 원하는 색을 직접 고른다. 나갈 때는 이전 값으로 되돌린다.
 * Dialog 창 밖에서 호출하면 아무 일도 하지 않는다.
 */
@Composable
fun DialogLightStatusBarIcons() {
    val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
    DisposableEffect(dialogWindow) {
        if (dialogWindow == null) {
            return@DisposableEffect onDispose {}
        }
        val insetsController = WindowCompat.getInsetsController(dialogWindow, dialogWindow.decorView)
        val previousValue = insetsController.isAppearanceLightStatusBars
        insetsController.isAppearanceLightStatusBars = true
        onDispose {
            insetsController.isAppearanceLightStatusBars = previousValue
        }
    }
}
