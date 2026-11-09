package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun InquiryHistoryItemCard(
    item: InquiryHistoryItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x10),
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
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
                InquiryStatusBadge(item.status)
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.eumColors.textPrimary,
            )
            Text(
                text = item.body,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textSecondary,
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
        Text(
            text = stringResource(if (isAnswered) R.string.inquiry_filter_answered else R.string.inquiry_filter_waiting),
            modifier = Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
            style = MaterialTheme.typography.labelSmall,
            color = if (isAnswered) MaterialTheme.eumColors.success else MaterialTheme.eumColors.warning,
        )
    }
}
