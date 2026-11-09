package com.nativelap.eumshoppingmall.ui.policy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/**
 * 개인정보 수집·이용 동의서의 항목 카드(라벨 위, 값 아래).
 * 공용 UiAuditSectionCard는 값이 회색이고 라벨 간격이 달라 Figma와 맞지 않아 이 카드를 따로 둔다.
 */
@Composable
internal fun ConsentItemCard(title: String, description: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x8),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
        }
    }
}
