package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * 자식이 소비하지 않은 탭이 들어오면 포커스를 해제하고 키보드를 닫는다.
 * 버튼·입력칸처럼 탭을 소비하는 자식에는 영향이 없다. Dialog 창은 별도 창이라 이 modifier가 닿지 않으므로 필요하면 창 안에 따로 단다.
 */
fun Modifier.clearFocusOnTap(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    pointerInput(focusManager, keyboardController) {
        detectTapGestures(
            onTap = {
                focusManager.clearFocus()
                keyboardController?.hide()
            },
        )
    }
}
