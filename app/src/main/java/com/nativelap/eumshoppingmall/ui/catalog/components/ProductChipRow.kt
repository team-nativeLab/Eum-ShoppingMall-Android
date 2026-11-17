package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.QuickCartBlockReason
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 배송·혜택 칩은 한 줄에 최대 2개만 그린다. 칩이 없어도 줄 높이(20dp 이상)는 유지된다.
@Composable
internal fun ProductChipRow(
    shippingLabel: String?,
    benefitLabels: List<String>,
    modifier: Modifier = Modifier,
    // 담을 수 없는 사유(품절, 옵션 필요)는 배송·혜택 칩보다 앞에 둔다. 칩 개수 상한은 같다.
    reasonLabel: String? = null,
) {
    val labels = listOfNotNull(reasonLabel, shippingLabel?.takeIf { it.isNotBlank() }) +
        benefitLabels.filter { it.isNotBlank() }
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
    ) {
        labels.take(2).forEach { label ->
            ProductInfoChip(label, Modifier.weight(1f, fill = false))
        }
    }
}

@Composable
private fun ProductInfoChip(
    label: String,
    modifier: Modifier = Modifier,
) {
    val chipShape = RoundedCornerShape(EumRadius.x4)
    Box(
        modifier = modifier
            .heightIn(min = 20.dp)
            .clip(chipShape)
            .border(1.dp, MaterialTheme.eumColors.lineNeutral, chipShape)
            .padding(horizontal = EumSpacing.x6),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = EumTypographyTokens.labelXs,
            color = MaterialTheme.eumColors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// 사유 칩 문구. 리소스 문구이므로 Composable에서 만든다.
@Composable
internal fun quickCartBlockLabel(reason: QuickCartBlockReason?): String? = when (reason) {
    QuickCartBlockReason.SoldOut -> stringResource(R.string.product_sold_out)
    QuickCartBlockReason.OptionRequired -> stringResource(R.string.product_option_required)
    null -> null
}
