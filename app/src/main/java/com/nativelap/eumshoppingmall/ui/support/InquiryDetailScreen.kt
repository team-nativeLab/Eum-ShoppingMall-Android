package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class InquiryAttachmentUiModel(val id: String, val fileName: String)
data class InquiryDetailUiModel(
    val title: String,
    val statusLabel: String,
    val dateLabel: String,
    val typeLabel: String,
    val orderNumber: String,
    val content: String,
    val attachments: List<InquiryAttachmentUiModel> = emptyList(),
    val answerDateLabel: String? = null,
    val answer: String? = null,
)

sealed interface InquiryDetailEvent {
    data object BackClicked : InquiryDetailEvent
    data class AttachmentClicked(val id: String) : InquiryDetailEvent
}

@Composable
fun InquiryDetailScreen(uiModel: InquiryDetailUiModel, onEvent: (InquiryDetailEvent) -> Unit, modifier: Modifier = Modifier) {
    UiAuditScreenScaffold(stringResource(R.string.inquiry_detail_title), { onEvent(InquiryDetailEvent.BackClicked) }, modifier) { padding ->
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)
                .padding(top = EumSpacing.x20, bottom = EumSpacing.x40),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Surface(shape = RoundedCornerShape(EumRadius.x4), color = MaterialTheme.eumColors.successTint) {
                Text(uiModel.statusLabel, Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.success)
            }
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(uiModel.title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.eumColors.textPrimary)
                Text(stringResource(R.string.inquiry_detail_meta, uiModel.dateLabel, uiModel.typeLabel), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
            }
            Surface(shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surface, border = androidx.compose.foundation.BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line)) {
                Column(Modifier.fillMaxWidth().padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                    InquiryDetailInfoRow(stringResource(R.string.inquiry_detail_type), uiModel.typeLabel)
                    InquiryDetailInfoRow(stringResource(R.string.inquiry_detail_order), uiModel.orderNumber)
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                    Text(stringResource(R.string.inquiry_detail_content), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                    Text(uiModel.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                    uiModel.attachments.forEach { attachment ->
                        Row(
                            Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable(role = Role.Button) { onEvent(InquiryDetailEvent.AttachmentClicked(attachment.id)) }
                                .padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8),
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(painterResource(R.drawable.ic_paperclip), contentDescription = null)
                            Text(attachment.fileName, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
                            Icon(painterResource(R.drawable.ic_download), contentDescription = stringResource(R.string.inquiry_attachment_download))
                        }
                    }
                }
            }
            if (uiModel.answer != null || uiModel.answerDateLabel != null) {
                Surface(shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surface, border = androidx.compose.foundation.BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong)) {
                    Column(Modifier.fillMaxWidth().padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.inquiry_detail_answer), Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
                            uiModel.answerDateLabel?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary) }
                        }
                        Text(uiModel.answer.orEmpty(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun InquiryDetailInfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12), verticalAlignment = Alignment.Top) {
        Text(label, Modifier.weight(0.25f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(value, Modifier.weight(0.75f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}
