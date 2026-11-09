package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteBottomActions
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteCard
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePageScaffold
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePrimaryButton
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteSecondaryButton
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteSummaryRow
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteCompleteEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteCompleteUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun QuoteCompleteScreen(
    uiModel: QuoteCompleteUiModel,
    onEvent: (QuoteCompleteEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    QuotePageScaffold(
        title = stringResource(R.string.quote_page_title),
        onBack = { onEvent(QuoteCompleteEvent.CloseClicked) },
        isCloseAction = true,
        bottomBar = {
            QuoteBottomActions {
                QuotePrimaryButton(
                    label = stringResource(R.string.quote_continue_shopping),
                    onClick = { onEvent(QuoteCompleteEvent.ContinueShoppingClicked) },
                )
                QuoteSecondaryButton(
                    label = stringResource(R.string.quote_view_history),
                    onClick = { onEvent(QuoteCompleteEvent.ViewInquiriesClicked) },
                )
            }
        },
    ) { contentModifier ->
        LazyColumn(
            modifier = contentModifier.fillMaxSize(),
            contentPadding = PaddingValues(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x40, bottom = EumSpacing.x24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Surface(modifier = Modifier.size(64.dp), shape = CircleShape, color = MaterialTheme.eumColors.textPrimary) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                tint = MaterialTheme.eumColors.inverse,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }
                    Text(
                        stringResource(R.string.quote_received),
                        modifier = Modifier.padding(top = EumSpacing.x8),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.eumColors.textPrimary,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        stringResource(R.string.quote_inquiry_number, uiModel.inquiryNumber),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.eumColors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        uiModel.responseTimeLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            item {
                QuoteCard {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                        QuoteSummaryRow(stringResource(R.string.quote_inquiry_type), uiModel.requestTypeLabel)
                        QuoteSummaryRow(stringResource(R.string.quote_company_name), uiModel.companyName)
                        QuoteSummaryRow(stringResource(R.string.quote_summary_contact), uiModel.contactName)
                        QuoteSummaryRow(stringResource(R.string.quote_inquiry_item), uiModel.productsSummary)
                        QuoteSummaryRow(stringResource(R.string.quote_preferred_due_date), uiModel.preferredDeliveryDate)
                    }
                }
            }
            item {
                val stepLabels = listOf(
                    R.string.quote_progress_inquiry,
                    R.string.quote_progress_consultation,
                    R.string.quote_progress_estimate,
                    R.string.quote_progress_complete,
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    stepLabels.forEachIndexed { index, labelRes ->
                        if (index > 0) {
                            Text(
                                "→",
                                Modifier.padding(horizontal = EumSpacing.x6),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.eumColors.textTertiary,
                            )
                        }
                        val isCurrentStep = index == 0
                        Text(
                            stringResource(labelRes),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isCurrentStep) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary,
                            fontWeight = if (isCurrentStep) FontWeight.Bold else FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}
