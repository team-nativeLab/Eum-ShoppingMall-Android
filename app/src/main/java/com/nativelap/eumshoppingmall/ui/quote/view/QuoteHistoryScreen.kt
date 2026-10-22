package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteBottomActions
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteCard
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePageScaffold
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePrimaryButton
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteStatusChip
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryFilter
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryListItemUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun QuoteHistoryScreen(
    uiModel: QuoteHistoryUiModel,
    onEvent: (QuoteHistoryEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    QuotePageScaffold(
        title = stringResource(R.string.quote_history_title),
        onBack = { onEvent(QuoteHistoryEvent.BackClicked) },
        bottomBar = {
            QuoteBottomActions {
                QuotePrimaryButton(
                    label = stringResource(R.string.quote_new_inquiry),
                    onClick = { onEvent(QuoteHistoryEvent.NewInquiryClicked) },
                )
            }
        },
    ) { contentModifier ->
        Column(contentModifier.fillMaxSize()) {
            QuoteHistoryFilters(uiModel.selectedFilter, onEvent)
            if (uiModel.inquiries.isEmpty()) {
                Box(Modifier.fillMaxWidth().weight(1f).padding(horizontal = EumSpacing.x24), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            stringResource(
                                if (uiModel.selectedFilter == QuoteHistoryFilter.All) {
                                    R.string.quote_history_empty_title
                                } else {
                                    R.string.quote_filter_empty_title
                                },
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(EumSpacing.x8))
                        Text(
                            stringResource(R.string.quote_history_empty_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.eumColors.textSecondary,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    items(uiModel.inquiries, key = { it.id }) { inquiry ->
                        QuoteInquiryCard(inquiry, onEvent)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuoteHistoryFilters(selected: QuoteHistoryFilter, onEvent: (QuoteHistoryEvent) -> Unit) {
    androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        items(QuoteHistoryFilter.entries) { filter ->
            val selectedFilter = filter == selected
            Surface(
                onClick = { onEvent(QuoteHistoryEvent.FilterSelected(filter)) },
                shape = RoundedCornerShape(20.dp),
                color = if (selectedFilter) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
                contentColor = if (selectedFilter) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
            ) {
                Text(
                    stringResource(filter.labelRes),
                    Modifier.heightIn(min = 48.dp).padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun QuoteInquiryCard(inquiry: QuoteInquiryListItemUiModel, onEvent: (QuoteHistoryEvent) -> Unit) {
    QuoteCard {
        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(inquiry.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                QuoteStatusChip(inquiry.status)
            }
            Text(inquiry.title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
            Text(inquiry.productsSummary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            Text(stringResource(R.string.quote_inquiry_number, inquiry.inquiryNumber), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
            if (inquiry.estimateAvailable) {
                Surface(
                    onClick = { onEvent(QuoteHistoryEvent.EstimateClicked(inquiry.id)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.eumColors.surfaceSecondary,
                ) {
                    Text(
                        stringResource(R.string.quote_estimate_view),
                        Modifier.heightIn(min = 48.dp).padding(EumSpacing.x12),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                }
            }
            Surface(onClick = { onEvent(QuoteHistoryEvent.InquiryClicked(inquiry.id)) }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.quote_detail_view),
                    Modifier.heightIn(min = 48.dp).padding(vertical = EumSpacing.x12),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.eumColors.textPrimary,
                )
            }
        }
    }
}

private val QuoteHistoryFilter.labelRes: Int
    get() = when (this) {
        QuoteHistoryFilter.All -> R.string.quote_filter_all
        QuoteHistoryFilter.Received -> R.string.quote_status_received
        QuoteHistoryFilter.Consulting -> R.string.quote_status_consulting
        QuoteHistoryFilter.EstimateSent -> R.string.quote_status_sent
        QuoteHistoryFilter.Complete -> R.string.quote_status_complete
        QuoteHistoryFilter.Cancelled -> R.string.quote_status_cancelled
    }
