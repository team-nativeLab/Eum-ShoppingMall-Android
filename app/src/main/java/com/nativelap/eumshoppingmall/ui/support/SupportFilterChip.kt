package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val CHIP_VISUAL_HEIGHT = 34.dp

/** 시각 높이(34dp)와 터치 영역(최소 48dp)을 분리해, 칩 모양은 Figma에 맞추고 터치 기준은 유지한다. */
@Composable
internal fun SupportFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        Modifier
            .minimumInteractiveComponentSize()
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { selected = isSelected },
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
            border = if (isSelected) null else BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong),
        ) {
            Box(
                Modifier
                    .height(CHIP_VISUAL_HEIGHT)
                    .padding(horizontal = EumSpacing.x12),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary,
                )
            }
        }
    }
}
