package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductBadge
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 이미지 좌상단 배지. 순위는 굵게, 라벨(견적 등)은 보통 굵기로 그린다.
@Composable
internal fun ProductCardBadge(
    badge: CatalogProductBadge,
    modifier: Modifier = Modifier,
) {
    val isRank = badge is CatalogProductBadge.BestRank
    val label = when (badge) {
        is CatalogProductBadge.BestRank -> stringResource(R.string.product_best_rank_badge, badge.rank)
        is CatalogProductBadge.Label -> badge.text
    }
    val badgeShape = RoundedCornerShape(EumRadius.x4)
    Box(
        modifier = modifier
            .heightIn(min = 20.dp)
            .clip(badgeShape)
            .background(MaterialTheme.eumColors.surface)
            .border(1.dp, MaterialTheme.eumColors.lineStrong, badgeShape)
            .padding(horizontal = EumSpacing.x6),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = if (isRank) EumTypographyTokens.labelXs.copy(fontWeight = FontWeight.Bold) else EumTypographyTokens.labelXs,
            color = if (isRank) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textSecondary,
            maxLines = 1,
        )
    }
}
