package com.nativelap.eumshoppingmall.ui.orders.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrdersTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
) {
    // 제목을 가운데에 두려면 좌우 슬롯 폭이 같아야 한다. 액션 라벨이 있으면 왼쪽 슬롯도 함께 넓힌다.
    val sideSlotWidth = if (actionLabel != null) 96.dp else 48.dp
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.width(sideSlotWidth), contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = onBackClick, modifier = Modifier.size(48.dp)) {
                Icon(
                    painterResource(R.drawable.ic_chevron_left),
                    contentDescription = stringResource(R.string.common_back),
                    tint = MaterialTheme.eumColors.textPrimary,
                )
            }
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.eumColors.textPrimary,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
        Box(Modifier.width(sideSlotWidth), contentAlignment = Alignment.CenterEnd) {
            if (actionLabel != null) {
                TextButton(onClick = onActionClick, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(actionLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textPrimary, maxLines = 1)
                }
            }
        }
    }
}
