package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class NotificationGroupUiModel(val title: String, val items: List<NotificationItemUiModel>)
data class NotificationItemUiModel(val id: String, val categoryLabel: String, val title: String, val body: String, val timeLabel: String, val isUnread: Boolean = false)

sealed interface NotificationScreenEvent {
    data object BackClicked : NotificationScreenEvent
    data object MarkAllReadClicked : NotificationScreenEvent
    data class NotificationClicked(val id: String) : NotificationScreenEvent
}

@Composable
fun NotificationScreen(groups: List<NotificationGroupUiModel>, unreadCount: Int, onEvent: (NotificationScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    UiAuditScreenScaffold(stringResource(R.string.notification_title), { onEvent(NotificationScreenEvent.BackClicked) }, modifier) { padding ->
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EumSpacing.x12, bottom = EumSpacing.x24)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.notification_unread_count, unreadCount), Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)
                Box(
                    Modifier.heightIn(min = 48.dp).clickable { onEvent(NotificationScreenEvent.MarkAllReadClicked) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(stringResource(R.string.notification_mark_all_read), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textPrimary)
                }
            }
            if (groups.isEmpty() || groups.all { it.items.isEmpty() }) {
                Text(stringResource(R.string.notification_empty), Modifier.fillMaxWidth().padding(vertical = EumSpacing.x40), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textSecondary)
            } else {
                groups.forEach { group ->
                    if (group.items.isNotEmpty()) {
                        Text(group.title, Modifier.padding(top = EumSpacing.x20, bottom = EumSpacing.x8), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                        group.items.forEach { item ->
                            Row(Modifier.fillMaxWidth().clickable { onEvent(NotificationScreenEvent.NotificationClicked(item.id)) }.padding(vertical = EumSpacing.x16), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.Top) {
                                Icon(painterResource(R.drawable.ic_bell), null, Modifier.size(EumSpacing.x20), tint = MaterialTheme.eumColors.textSecondary)
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                                        Text(item.categoryLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                                        Text(item.timeLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                                    }
                                    Text(item.title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                                    Text(item.body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.eumColors.line)
                        }
                    }
                }
            }
            Text(stringResource(R.string.notification_retention_notice), Modifier.padding(top = EumSpacing.x20), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
        }
    }
}
