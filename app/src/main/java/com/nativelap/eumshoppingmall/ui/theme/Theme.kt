package com.nativelap.eumshoppingmall.ui.theme

import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * 체크박스 체크 상태를 Figma 흑백 톤으로 맞춘다.
 * 기본값에서 시작해 체크 관련 색만 바꾼다. checkedColor 단일 인자로 만들면 미체크 테두리와 체크 표시가 투명해진다.
 * 전역 primary(파란색)는 바꾸지 않아 다른 컴포넌트에 영향이 없다.
 */
@Composable
fun eumCheckboxColors(): CheckboxColors {
    val checkedColor = LocalEumColorTokens.current.checkboxChecked
    val checkmarkColor = LocalEumColorTokens.current.inverse
    return CheckboxDefaults.colors().copy(
        checkedBoxColor = checkedColor,
        checkedBorderColor = checkedColor,
        checkedCheckmarkColor = checkmarkColor,
    )
}

/** The Figma file defines Light values only; system and dynamic palettes are intentionally disabled. */
@Composable
fun EumShoppingMallTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalEumColorTokens provides LightEumColorTokens) {
        MaterialTheme(
            colorScheme = EumLightColorScheme,
            typography = Typography,
            shapes = EumShapes,
            content = content,
        )
    }
}
