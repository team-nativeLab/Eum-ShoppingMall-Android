package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class NoticeDetailUiModel(val title: String, val dateLabel: String, val content: String, val isPinned: Boolean = false)

@Composable
fun NoticeDetailScreen(uiModel: NoticeDetailUiModel, onBack: () -> Unit, modifier: Modifier = Modifier) {
    UiAuditScreenScaffold(stringResource(R.string.notice_title), onBack, modifier) { padding ->
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EumSpacing.x24, bottom = EumSpacing.x40), verticalArrangement = Arrangement.spacedBy(EumSpacing.x20)) {
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                if (uiModel.isPinned) {
                    Surface(shape = RoundedCornerShape(EumRadius.x4), color = MaterialTheme.eumColors.textPrimary) {
                        Text(stringResource(R.string.notice_pinned), Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.inverse)
                    }
                }
                Text(uiModel.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
                Text(uiModel.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
            }
            HorizontalDivider(color = MaterialTheme.eumColors.line)
            Text(uiModel.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
        }
    }
}
