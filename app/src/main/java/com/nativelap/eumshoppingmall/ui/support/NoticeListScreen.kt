package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val EMPTY_TOP_PADDING = 120.dp

data class NoticeItemUiModel(val id: String, val title: String, val dateLabel: String, val isPinned: Boolean = false)

sealed interface NoticeScreenEvent {
    data object BackClicked : NoticeScreenEvent
    data class NoticeClicked(val id: String) : NoticeScreenEvent
}

@Composable
fun NoticeListScreen(notices: List<NoticeItemUiModel>, onEvent: (NoticeScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    UiAuditScreenScaffold(stringResource(R.string.notice_title), { onEvent(NoticeScreenEvent.BackClicked) }, modifier) { padding ->
        if (notices.isEmpty()) {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EMPTY_TOP_PADDING, bottom = EumSpacing.x32),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier.padding(bottom = EumSpacing.x20).size(64.dp).clip(CircleShape).background(MaterialTheme.eumColors.surface),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(painterResource(R.drawable.ic_search_x), contentDescription = null, Modifier.size(28.dp), tint = MaterialTheme.eumColors.textSecondary)
                }
                Text(
                    stringResource(R.string.notice_empty_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.eumColors.textPrimary,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            // 목록 바깥 여백은 Scaffold content가 이미 적용하므로 행마다 가로 여백을 주어 구분선이 화면 전체 폭을 쓰게 한다.
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                notices.forEach { notice ->
                    // 고정 공지는 Figma bg-subtle(backgroundSubtle), 일반 공지는 surface 배경이다.
                    val rowBackground = if (notice.isPinned) MaterialTheme.eumColors.backgroundSubtle else MaterialTheme.eumColors.surface
                    Column(
                        Modifier.fillMaxWidth()
                            .background(rowBackground)
                            .clickable { onEvent(NoticeScreenEvent.NoticeClicked(notice.id)) }
                            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        if (notice.isPinned) NoticePinnedBadge()
                        Text(
                            notice.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.eumColors.textPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(notice.dateLabel, style = EumTypographyTokens.caption, color = MaterialTheme.eumColors.textTertiary)
                    }
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
            }
        }
    }
}
