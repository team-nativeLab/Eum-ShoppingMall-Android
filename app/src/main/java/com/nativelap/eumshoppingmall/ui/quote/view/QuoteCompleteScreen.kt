package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteBottomActions
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
        title = stringResource(R.string.quote_received),
        onBack = { onEvent(QuoteCompleteEvent.CloseClicked) },
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
            contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            item {
                Surface(color = MaterialTheme.eumColors.successTint, shape = CircleShape) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = MaterialTheme.eumColors.success,
                        modifier = Modifier.padding(18.dp),
                    )
                }
                Text(stringResource(R.string.quote_received), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                Text(
                    uiModel.responseTimeLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textSecondary,
                    textAlign = TextAlign.Center,
                )
            }
            item {
                Column(
                    Modifier.fillMaxWidth().padding(top = EumSpacing.x8),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                ) {
                    QuoteSummaryRow(stringResource(R.string.quote_inquiry_number), uiModel.inquiryNumber)
                    QuoteSummaryRow(stringResource(R.string.quote_inquiry_type), uiModel.requestTypeLabel)
                    QuoteSummaryRow(stringResource(R.string.quote_company_name), uiModel.companyName)
                    QuoteSummaryRow(stringResource(R.string.quote_contact_name), uiModel.contactName)
                    QuoteSummaryRow(stringResource(R.string.quote_interested_products), uiModel.productsSummary)
                    QuoteSummaryRow(stringResource(R.string.quote_delivery_date), uiModel.preferredDeliveryDate)
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth().padding(top = EumSpacing.x8),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    listOf(
                        R.string.quote_progress_inquiry,
                        R.string.quote_progress_consultation,
                        R.string.quote_progress_estimate,
                        R.string.quote_progress_complete,
                    ).forEachIndexed { index, label ->
                        Text(
                            stringResource(label),
                            color = if (index == 0) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}
