package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 라벨과 값이 짝을 이루는 정책 항목 표(제3자 제공, 위탁, 보호책임자 등)를 보여준다. */
@Composable
internal fun PolicyKeyValueCard(rows: List<Pair<String, String>>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x8),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
    ) {
        Column(Modifier.fillMaxWidth().padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            rows.forEach { (label, value) ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Text(label, Modifier.width(PolicyKeyValueLabelWidth), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                    Text(value, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
                }
            }
        }
    }
}

// Figma 표 라벨 열 폭은 EumSpacing 토큰에 없어 이 카드 전용 값으로 둔다.
private val PolicyKeyValueLabelWidth: Dp = 96.dp
