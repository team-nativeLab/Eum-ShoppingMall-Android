package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val STATUS_BADGE_HEIGHT = 20.dp

@Composable
internal fun InquiryHistoryItemCard(
    item: InquiryHistoryItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(EumSpacing.x16),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.inquiry_detail_meta, item.dateLabel, item.categoryLabel),
                    modifier = Modifier.weight(1f),
                    style = EumTypographyTokens.caption,
                    color = MaterialTheme.eumColors.textTertiary,
                )
                InquiryStatusBadge(item.status)
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.eumColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.body,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            item.answer?.let { answer ->
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textPrimary,
                )
            }
        }
    }
}

@Composable
private fun InquiryStatusBadge(status: InquiryStatus) {
    val isAnswered = status == InquiryStatus.Answered
    Surface(
        shape = RoundedCornerShape(EumRadius.x4),
        color = if (isAnswered) MaterialTheme.eumColors.successTint else MaterialTheme.eumColors.warningTint,
    ) {
        Box(
            Modifier.heightIn(min = STATUS_BADGE_HEIGHT).padding(horizontal = EumSpacing.x6),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(if (isAnswered) R.string.inquiry_filter_answered else R.string.inquiry_filter_waiting),
                style = MaterialTheme.typography.labelSmall,
                color = if (isAnswered) MaterialTheme.eumColors.success else MaterialTheme.eumColors.warning,
            )
        }
    }
}
