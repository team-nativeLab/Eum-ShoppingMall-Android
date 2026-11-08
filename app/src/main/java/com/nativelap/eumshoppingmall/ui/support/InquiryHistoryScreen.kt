package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

enum class InquiryStatus { Waiting, Answered }
data class InquiryHistoryItemUiModel(val id: String, val title: String, val categoryLabel: String, val dateLabel: String, val status: InquiryStatus, val body: String, val answer: String? = null)

sealed interface InquiryHistoryEvent {
    data object BackClicked : InquiryHistoryEvent
    data class FilterSelected(val status: InquiryStatus?) : InquiryHistoryEvent
    data class ItemClicked(val id: String) : InquiryHistoryEvent
    data object WriteClicked : InquiryHistoryEvent
}

@Composable
fun InquiryHistoryScreen(items: List<InquiryHistoryItemUiModel>, selectedStatus: InquiryStatus?, onEvent: (InquiryHistoryEvent) -> Unit, modifier: Modifier = Modifier) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.inquiry_history_title),
        onBackClick = { onEvent(InquiryHistoryEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.inquiry_history_write),
        onBottomActionClick = { onEvent(InquiryHistoryEvent.WriteClicked) },
    ) { padding ->
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EumSpacing.x12, bottom = EumSpacing.x24)) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                InquiryFilterChip(stringResource(R.string.inquiry_filter_all), selectedStatus == null) { onEvent(InquiryHistoryEvent.FilterSelected(null)) }
                InquiryFilterChip(stringResource(R.string.inquiry_filter_waiting), selectedStatus == InquiryStatus.Waiting) { onEvent(InquiryHistoryEvent.FilterSelected(InquiryStatus.Waiting)) }
                InquiryFilterChip(stringResource(R.string.inquiry_filter_answered), selectedStatus == InquiryStatus.Answered) { onEvent(InquiryHistoryEvent.FilterSelected(InquiryStatus.Answered)) }
            }
            if (items.isEmpty()) {
                Text(stringResource(R.string.inquiry_history_empty), Modifier.fillMaxWidth().padding(vertical = EumSpacing.x40), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textSecondary)
            } else {
                items.forEach { item ->
                    Column(Modifier.fillMaxWidth().clickable { onEvent(InquiryHistoryEvent.ItemClicked(item.id)) }.padding(vertical = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                            Text(item.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                            Text(item.categoryLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                            InquiryStatusBadge(item.status)
                        }
                        Text(item.title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textPrimary)
                        Text(item.body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        item.answer?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary) }
                    }
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
            }
        }
    }
}

@Composable
private fun InquiryFilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(Modifier.heightIn(min = 48.dp).clickable(role = Role.RadioButton, onClick = onClick).semantics { selected = isSelected }, shape = RoundedCornerShape(50), color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface, border = if (isSelected) null else BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong)) {
        Text(label, Modifier.padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x12), style = MaterialTheme.typography.labelSmall, color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary)
    }
}

@Composable
private fun InquiryStatusBadge(status: InquiryStatus) {
    val isAnswered = status == InquiryStatus.Answered
    Surface(shape = RoundedCornerShape(EumRadius.x4), color = if (isAnswered) MaterialTheme.eumColors.successTint else MaterialTheme.eumColors.surfaceSecondary) {
        Text(stringResource(if (isAnswered) R.string.inquiry_filter_answered else R.string.inquiry_filter_waiting), Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4), style = MaterialTheme.typography.labelSmall, color = if (isAnswered) MaterialTheme.eumColors.success else MaterialTheme.eumColors.textSecondary)
    }
}
