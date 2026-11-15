package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 아이콘·라벨·값을 한 줄에 놓는 신청 정보 행(Figma 879:44641, 879:44666, 879:44682). 행 사이 간격 10dp는 부모 Column이 둔다. */
@Composable
internal fun RequestInfoRow(iconResId: Int, label: String, value: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = MaterialTheme.eumColors.textPrimary,
            modifier = Modifier.size(20.dp),
        )
        Text(
            label,
            Modifier.padding(start = EumSpacing.x8, end = EumSpacing.x8).widthIn(min = RequestInfoLabelMinWidth),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.eumColors.textSecondary,
        )
        Text(
            value,
            Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.eumColors.textPrimary,
        )
    }
}

// Figma 라벨 폭 72dp는 최소 폭으로 둔다(글꼴 확대 시 라벨이 늘어나도록). EumSpacing 토큰에 없어 이 행에서만 쓴다.
private val RequestInfoLabelMinWidth = 72.dp
