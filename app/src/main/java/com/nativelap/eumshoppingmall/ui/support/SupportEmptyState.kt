package com.nativelap.eumshoppingmall.ui.support

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 빈 상태의 간격과 원 크기는 Figma 빈 상태 프레임의 측정값을 따른다. */
@Composable
internal fun SupportEmptyState(
    @DrawableRes iconRes: Int,
    title: String,
    body: String,
    topPadding: Dp = EumSpacing.x64,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = EumSpacing.x40),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Box(Modifier.padding(bottom = EumSpacing.x12)) {
            Box(
                Modifier
                    .size(EMPTY_ICON_CIRCLE_SIZE)
                    .background(MaterialTheme.eumColors.surfaceSecondary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(EumSpacing.x24),
                    tint = MaterialTheme.eumColors.textSecondary,
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.eumColors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

private val EMPTY_ICON_CIRCLE_SIZE = 64.dp
