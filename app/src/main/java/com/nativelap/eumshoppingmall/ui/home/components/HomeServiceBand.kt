package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 서비스 안내 띠. 무료배송 기준 금액은 문자열 리소스의 포맷 인자로 호출 측이 넘긴다.
// 칸 높이는 행 높이를 맞춰 통일한다.
@Composable
fun HomeServiceBand(
    freeShippingThresholdLabel: String,
    modifier: Modifier = Modifier,
) {
    val serviceLabels = listOf(
        stringResource(R.string.home_service_free_shipping, freeShippingThresholdLabel),
        stringResource(R.string.home_service_same_day),
        stringResource(R.string.home_service_tax_invoice),
    )
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(EumRadius.x8))
            .background(MaterialTheme.eumColors.surfaceSecondary)
            .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
    ) {
        val useStackedList = shouldStackHomeServiceBand(maxWidth, LocalDensity.current.fontScale)
        if (useStackedList) {
            // 좁은 폭·큰 글꼴에서는 한 줄에 둘씩 나누지 않고 세로 목록으로 둬 항목의 열 기준을 맞춘다.
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                serviceLabels.forEach { label ->
                    ServiceCell(label = label, modifier = Modifier.fillMaxWidth())
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
            ) {
                serviceLabels.forEach { label ->
                    ServiceCell(
                        label = label,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

internal fun shouldStackHomeServiceBand(maxWidth: androidx.compose.ui.unit.Dp, fontScale: Float): Boolean = maxWidth < 360.dp || fontScale >= 1.3f

@Composable
private fun ServiceCell(
    label: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .heightIn(min = 48.dp)
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
