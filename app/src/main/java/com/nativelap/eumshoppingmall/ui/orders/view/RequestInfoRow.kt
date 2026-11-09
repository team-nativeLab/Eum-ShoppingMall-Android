package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

/** 아이콘·라벨·값을 한 줄에 놓는 신청 정보 행(Figma 879:44641, 879:44666, 879:44682). */
@Composable
internal fun RequestInfoRow(iconResId: Int, label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x6), verticalAlignment = Alignment.Top) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = MaterialTheme.eumColors.textPrimary,
            modifier = Modifier.size(18.dp),
        )
        Text(
            label,
            Modifier.padding(start = EumSpacing.x8).width(80.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textSecondary,
        )
        Text(value, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
    }
}
