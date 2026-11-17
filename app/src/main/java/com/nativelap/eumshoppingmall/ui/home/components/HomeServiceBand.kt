package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 서비스 안내 띠. 무료배송 기준 금액은 문자열 리소스의 포맷 인자로 호출 측이 넘긴다.
// 칸 높이는 행 높이를 맞춰 통일하고, 대량·맞춤 견적 칸은 견적 문의 화면으로 연결한다.
@Composable
fun HomeServiceBand(
    freeShippingThresholdLabel: String,
    onQuoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clip(RoundedCornerShape(EumRadius.x8))
            .background(MaterialTheme.eumColors.surfaceSecondary)
            .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
    ) {
        ServiceCell(
            label = stringResource(R.string.home_service_free_shipping, freeShippingThresholdLabel),
            onClick = null,
            modifier = Modifier.weight(1f),
        )
        ServiceCell(
            label = stringResource(R.string.home_service_same_day),
            onClick = null,
            modifier = Modifier.weight(1f),
        )
        ServiceCell(
            label = stringResource(R.string.home_service_tax_invoice),
            onClick = null,
            modifier = Modifier.weight(1f),
        )
        ServiceCell(
            label = stringResource(R.string.home_service_custom_quote),
            onClick = onQuoteClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ServiceCell(
    label: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(role = Role.Button, onClick = onClick)
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .fillMaxHeight()
            .heightIn(min = 48.dp)
            .then(clickModifier)
            .padding(horizontal = EumSpacing.x4),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.eumColors.textSecondary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
