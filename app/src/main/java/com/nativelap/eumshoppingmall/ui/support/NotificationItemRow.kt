package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun NotificationItemRow(
    item: NotificationItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = EumSpacing.x16),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            Modifier
                .size(ICON_CIRCLE_SIZE)
                .border(EumSpacing.x1, MaterialTheme.eumColors.line, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = null,
                modifier = Modifier.size(EumSpacing.x20),
                tint = MaterialTheme.eumColors.textSecondary,
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                    Text(item.categoryLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                    Text(item.timeLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                }
                if (item.isUnread) {
                    val unreadDescription = stringResource(R.string.notification_unread_description)
                    Box(
                        Modifier
                            .size(EumSpacing.x6)
                            .background(MaterialTheme.eumColors.success, CircleShape)
                            .semantics { contentDescription = unreadDescription },
                    )
                }
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (item.isUnread) FontWeight.Bold else FontWeight.Normal),
                color = MaterialTheme.eumColors.textPrimary,
            )
            Text(item.body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
    }
}

private val ICON_CIRCLE_SIZE = 40.dp
