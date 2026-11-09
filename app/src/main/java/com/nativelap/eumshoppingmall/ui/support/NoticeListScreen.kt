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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class NoticeItemUiModel(val id: String, val title: String, val dateLabel: String, val isPinned: Boolean = false)

sealed interface NoticeScreenEvent {
    data object BackClicked : NoticeScreenEvent
    data class NoticeClicked(val id: String) : NoticeScreenEvent
}

@Composable
fun NoticeListScreen(notices: List<NoticeItemUiModel>, onEvent: (NoticeScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    UiAuditScreenScaffold(stringResource(R.string.notice_title), { onEvent(NoticeScreenEvent.BackClicked) }, modifier) { padding ->
        if (notices.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(padding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Box(
                    Modifier.padding(bottom = EumSpacing.x20).size(64.dp).clip(CircleShape).background(MaterialTheme.eumColors.surfaceSecondary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(painterResource(R.drawable.ic_megaphone), contentDescription = null, Modifier.size(24.dp), tint = MaterialTheme.eumColors.textSecondary)
                }
                Text(stringResource(R.string.notice_empty_title), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.notice_empty_description), Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
        } else {
            // 목록 바깥 여백은 Scaffold content가 이미 적용하므로 행마다 가로 여백을 주어 구분선이 화면 전체 폭을 쓰게 한다.
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                notices.forEach { notice ->
                    Column(Modifier.fillMaxWidth().clickable { onEvent(NoticeScreenEvent.NoticeClicked(notice.id)) }.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        if (notice.isPinned) {
                            Surface(shape = RoundedCornerShape(EumRadius.x4), color = MaterialTheme.eumColors.textPrimary) {
                                Text(stringResource(R.string.notice_pinned), Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.inverse)
                            }
                        }
                        Text(notice.title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.eumColors.textPrimary)
                        Text(notice.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                    }
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
            }
        }
    }
}
