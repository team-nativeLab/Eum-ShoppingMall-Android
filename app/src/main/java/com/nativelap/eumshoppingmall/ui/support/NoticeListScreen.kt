package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
                Text(stringResource(R.string.notice_empty_title), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.notice_empty_description), Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
        } else {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) {
                notices.forEach { notice ->
                    Column(Modifier.fillMaxWidth().clickable { onEvent(NoticeScreenEvent.NoticeClicked(notice.id)) }.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        if (notice.isPinned) {
                            Surface(shape = RoundedCornerShape(EumRadius.x4), color = MaterialTheme.eumColors.textPrimary) {
                                Text(stringResource(R.string.notice_pinned), Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.inverse)
                            }
                        }
                        Text(notice.title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                        Text(notice.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                    }
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
            }
        }
    }
}
