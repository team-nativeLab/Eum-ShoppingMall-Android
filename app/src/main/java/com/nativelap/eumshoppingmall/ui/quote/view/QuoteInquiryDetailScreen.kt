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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteAttachmentLabel
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteBottomActions
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteCard
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePageScaffold
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePrimaryButton
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteSecondaryButton
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteStatusChip
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteSummaryRow
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryStatus
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun QuoteInquiryDetailScreen(
    uiModel: QuoteInquiryDetailUiModel,
    onEvent: (QuoteInquiryDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    QuotePageScaffold(
        title = stringResource(R.string.quote_detail_title),
        onBack = { onEvent(QuoteInquiryDetailEvent.BackClicked) },
        bottomBar = {
            QuoteBottomActions {
                Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    QuoteSecondaryButton(
                        label = stringResource(R.string.quote_contact_manager),
                        onClick = { onEvent(QuoteInquiryDetailEvent.ContactManagerClicked) },
                        modifier = Modifier.weight(1f),
                    )
                    QuotePrimaryButton(
                        label = stringResource(R.string.quote_request_again),
                        onClick = { onEvent(QuoteInquiryDetailEvent.RequestAgainClicked) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        },
    ) { contentModifier ->
        LazyColumn(
            modifier = contentModifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            item {
                QuoteCard {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        QuoteStatusChip(uiModel.status)
                        Text(uiModel.title, style = MaterialTheme.typography.titleMedium)
                        Text(uiModel.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                        QuoteSummaryRow(stringResource(R.string.quote_inquiry_number_label), uiModel.inquiryNumber)
                    }
                }
            }
            uiModel.managerReply?.let { reply ->
                item {
                    QuoteCard {
                        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            Text(stringResource(R.string.quote_reply), style = MaterialTheme.typography.titleSmall)
                            Text(reply, style = MaterialTheme.typography.bodySmall)
                            uiModel.amountLabel?.let { QuoteSummaryRow(stringResource(R.string.quote_amount), it) }
                            uiModel.validityLabel?.let { QuoteSummaryRow(stringResource(R.string.quote_valid_until), it) }
                            uiModel.managerName?.let { QuoteSummaryRow(stringResource(R.string.quote_manager), it) }
                            uiModel.managerPhone?.let { QuoteSummaryRow(stringResource(R.string.quote_phone_number), it) }
                            uiModel.estimateFile?.let { attachment ->
                                QuoteAttachmentLabel(attachment.fileName, attachment.sizeLabel)
                                androidx.compose.material3.TextButton(onClick = { onEvent(QuoteInquiryDetailEvent.EstimateDownloadClicked) }) {
                                    Text(stringResource(R.string.quote_estimate_download))
                                }
                            }
                        }
                    }
                }
            }
            item {
                QuoteCard {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(stringResource(R.string.quote_requester_information), style = MaterialTheme.typography.titleSmall)
                        QuoteSummaryRow(stringResource(R.string.quote_company_name), uiModel.companyName)
                        QuoteSummaryRow(stringResource(R.string.quote_contact_name), uiModel.contactName)
                        QuoteSummaryRow(stringResource(R.string.quote_phone_number), uiModel.contactPhone)
                        QuoteSummaryRow(stringResource(R.string.quote_email_address), uiModel.contactEmail)
                    }
                }
            }
            item {
                QuoteCard {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        if (uiModel.status == QuoteInquiryStatus.Cancelled) {
                            Text(
                                stringResource(R.string.quote_detail_cancelled_notice),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.eumColors.textSecondary,
                            )
                        }
                        Text(stringResource(R.string.quote_interested_products), style = MaterialTheme.typography.titleSmall)
                        uiModel.items.forEach { item ->
                            QuoteSummaryRow(item.name, item.quantityLabel)
                        }
                        uiModel.requestAttachments.forEach { attachment ->
                            QuoteAttachmentLabel(attachment.fileName, attachment.sizeLabel)
                            androidx.compose.material3.TextButton(
                                onClick = { onEvent(QuoteInquiryDetailEvent.RequestAttachmentClicked(attachment.id)) },
                            ) {
                                Text(stringResource(R.string.quote_file_download))
                            }
                        }
                        uiModel.preferredDeliveryDate?.let { QuoteSummaryRow(stringResource(R.string.quote_delivery_date), it) }
                        uiModel.deliveryAddress?.let { QuoteSummaryRow(stringResource(R.string.quote_delivery_address), it) }
                        uiModel.requestDetails?.let {
                            Spacer(Modifier.height(EumSpacing.x4))
                            Text(stringResource(R.string.quote_request_details), style = MaterialTheme.typography.labelMedium)
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
