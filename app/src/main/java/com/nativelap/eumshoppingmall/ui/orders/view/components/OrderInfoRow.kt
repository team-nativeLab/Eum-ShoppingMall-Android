package com.nativelap.eumshoppingmall.ui.orders.view.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// Figma 주문 상세 배송지 행: 라벨 열을 100dp로 고정하고 값은 그 옆에 왼쪽 정렬한다.
private val OrderInfoLabelWidth = 100.dp

@Composable
fun OrderInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            label,
            modifier = Modifier.width(OrderInfoLabelWidth),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textSecondary,
        )
        Text(
            value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
    }
}
