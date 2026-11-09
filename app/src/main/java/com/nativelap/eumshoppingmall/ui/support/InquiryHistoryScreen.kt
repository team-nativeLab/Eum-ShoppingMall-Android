package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing

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
        showBottomDivider = true,
        bottomActionLabel = stringResource(R.string.inquiry_history_write),
        onBottomActionClick = { onEvent(InquiryHistoryEvent.WriteClicked) },
    ) { padding ->
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(top = EumSpacing.x12, bottom = EumSpacing.x24),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                SupportFilterChip(stringResource(R.string.inquiry_filter_all), selectedStatus == null) { onEvent(InquiryHistoryEvent.FilterSelected(null)) }
                SupportFilterChip(stringResource(R.string.inquiry_filter_waiting), selectedStatus == InquiryStatus.Waiting) { onEvent(InquiryHistoryEvent.FilterSelected(InquiryStatus.Waiting)) }
                SupportFilterChip(stringResource(R.string.inquiry_filter_answered), selectedStatus == InquiryStatus.Answered) { onEvent(InquiryHistoryEvent.FilterSelected(InquiryStatus.Answered)) }
            }
            if (items.isEmpty()) {
                SupportEmptyState(
                    iconRes = R.drawable.ic_message_square_text,
                    title = stringResource(R.string.inquiry_history_empty_title),
                    body = stringResource(R.string.inquiry_history_empty_body),
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                    items.forEach { item ->
                        InquiryHistoryItemCard(item, onClick = { onEvent(InquiryHistoryEvent.ItemClicked(item.id)) })
                    }
                }
            }
        }
    }
}
