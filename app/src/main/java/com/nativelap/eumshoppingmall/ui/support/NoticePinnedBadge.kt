package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val PINNED_BADGE_HEIGHT = 20.dp

/** 공지 목록과 상세가 함께 쓰는 고정 공지 배지(높이 20·좌우 6·모서리 4·11/14 Medium). */
@Composable
internal fun NoticePinnedBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(EumRadius.x4),
        color = MaterialTheme.eumColors.textPrimary,
    ) {
        Box(
            Modifier.heightIn(min = PINNED_BADGE_HEIGHT).padding(horizontal = EumSpacing.x6),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.notice_pinned),
                style = EumTypographyTokens.labelXs,
                color = MaterialTheme.eumColors.inverse,
            )
        }
    }
}
