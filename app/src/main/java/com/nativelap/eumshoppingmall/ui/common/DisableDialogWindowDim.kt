package com.nativelap.eumshoppingmall.ui.common

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider

/**
 * 이 Composable이 놓인 Dialog 창의 기본 dim을 끈다.
 *
 * 화면이 디자인 토큰 scrim을 직접 그리는 overlay(예: 드로어)에서 창 dim까지 겹쳐 배경이 두 번 어두워지는 것을 막는다.
 * Dialog 창 밖에서 호출하면 아무 일도 하지 않는다. 나갈 때는 원래 dim 플래그가 있었던 경우에만 되돌린다.
 */
@Composable
fun DisableDialogWindowDim() {
    val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
    DisposableEffect(dialogWindow) {
        val hadDimBehind = dialogWindow != null &&
            dialogWindow.attributes.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND != 0
        if (hadDimBehind) {
            dialogWindow?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        onDispose {
            if (hadDimBehind) {
                dialogWindow?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        }
    }
}
