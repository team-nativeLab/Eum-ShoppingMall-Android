package com.nativelap.eumshoppingmall.ui.audit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/**
 * Figma 라디오 20dp 원. 선택은 textPrimary 단색 채움, 미선택은 흰 채움에 1.5dp 링이다.
 * 표시 전용이다. 시맨틱(selected·RadioButton 역할)은 행 전체의 selectable(onClick 있는 쪽)이 맡는다.
 */
@Composable
internal fun UiAuditRadioIndicator(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.eumColors
    Box(
        modifier = modifier.size(RadioIndicatorSize).then(
            if (selected) {
                Modifier.background(colors.textPrimary, CircleShape)
            } else {
                // Figma 링 #A0A0A0은 addressPlaceholder 토큰 값과 같다. 링 대비는 2.61이라 선택 여부는 채움 유무로 구분한다.
                Modifier.background(colors.surface, CircleShape)
                    .border(RadioIndicatorRingWidth, colors.addressPlaceholder, CircleShape)
            },
        ),
    )
}

private val RadioIndicatorSize = 20.dp
private val RadioIndicatorRingWidth = 1.5.dp
