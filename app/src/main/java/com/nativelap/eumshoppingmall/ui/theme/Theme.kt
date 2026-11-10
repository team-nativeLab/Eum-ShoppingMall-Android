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
        // 비활성 미체크 박스는 기본 회색 대신 연한 라인 토큰을 쓴다(m3). 값은 기존 토큰이라 미확인 항목이 아니다.
        disabledUncheckedBoxColor = LocalEumColorTokens.current.line,
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
