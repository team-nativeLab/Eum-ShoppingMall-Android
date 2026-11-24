package com.nativelap.eumshoppingmall.ui.support

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val MARK_ALL_READ_VISUAL_HEIGHT = 36.dp

data class NotificationGroupUiModel(val title: String, val items: List<NotificationItemUiModel>)
data class NotificationItemUiModel(
    val id: String,
    val categoryLabel: String,
    val title: String,
    val body: String,
    val timeLabel: String,
    val isUnread: Boolean = false,
    @DrawableRes val iconRes: Int = R.drawable.ic_bell,
)

sealed interface NotificationScreenEvent {
    data object BackClicked : NotificationScreenEvent
    data object MarkAllReadClicked : NotificationScreenEvent
    data class NotificationClicked(val id: String) : NotificationScreenEvent
}

@Composable
fun NotificationScreen(groups: List<NotificationGroupUiModel>, unreadCount: Int, onEvent: (NotificationScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    val hasNotifications = groups.any { it.items.isNotEmpty() }
    UiAuditScreenScaffold(stringResource(R.string.notification_title), { onEvent(NotificationScreenEvent.BackClicked) }, modifier) { padding ->
        // 알림 행은 화면 폭을 채우고 행 안쪽 좌우 20dp를 직접 갖는다. 나머지 요소는 scaffold 좌우 여백을 쓴다.
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            // 알림이 없으면 읽지 않은 개수와 모두 읽음 버튼은 의미가 없어 빈 상태 안내만 보인다.
            if (hasNotifications) {
                Row(
                    Modifier.fillMaxWidth().padding(padding).padding(vertical = EumSpacing.x8),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        stringResource(R.string.notification_unread_count, unreadCount),
                        Modifier.weight(1f),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                    Box(
                        Modifier.heightIn(min = 48.dp).clickable(role = Role.Button) { onEvent(NotificationScreenEvent.MarkAllReadClicked) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            Modifier.heightIn(min = MARK_ALL_READ_VISUAL_HEIGHT).padding(horizontal = EumSpacing.x12),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                stringResource(R.string.notification_mark_all_read),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.eumColors.textPrimary,
                            )
                        }
                    }
                }
            }
            if (!hasNotifications) {
                SupportEmptyState(
                    iconRes = R.drawable.ic_bell,
                    title = stringResource(R.string.notification_empty_title),
                    // 제목이 이미 "알림이 없다"는 사실을 전하므로 같은 뜻의 본문은 두지 않는다.
                    body = null,
                )
            } else {
                groups.forEach { group ->
                    if (group.items.isNotEmpty()) {
                        Text(
                            group.title,
                            Modifier.fillMaxWidth().padding(padding).padding(top = EumSpacing.x16, bottom = EumSpacing.x8),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                        group.items.forEach { item ->
                            NotificationItemRow(item, onClick = { onEvent(NotificationScreenEvent.NotificationClicked(item.id)) })
                        }
                    }
                }
                Text(
                    stringResource(R.string.notification_retention_notice),
                    Modifier.fillMaxWidth().padding(padding).padding(vertical = EumSpacing.x24),
                    style = EumTypographyTokens.caption,
                    color = MaterialTheme.eumColors.textTertiary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
