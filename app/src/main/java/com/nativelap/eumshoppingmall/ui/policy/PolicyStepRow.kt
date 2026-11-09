package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 번호가 붙은 절차 한 단계(번호 원형, 제목, 설명)를 보여준다. */
@Composable
internal fun PolicyStepRow(number: Int, step: PolicyDocumentStepUiModel) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(PolicyStepBadgeSize).background(MaterialTheme.eumColors.textPrimary, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(number.toString(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.surface)
        }
        Column(Modifier.padding(start = EumSpacing.x12)) {
            Text(step.title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.eumColors.textPrimary)
            Text(step.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
    }
}

// Figma 단계 번호 원형 크기는 EumSpacing 토큰에 없어 이 행 전용 값으로 둔다.
private val PolicyStepBadgeSize: Dp = 20.dp
