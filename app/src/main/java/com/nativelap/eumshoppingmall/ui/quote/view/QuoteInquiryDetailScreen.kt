package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteBottomActions
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteCard
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteDetailItemRow
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteFileDownloadRow
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePageScaffold
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuotePrimaryButton
import com.nativelap.eumshoppingmall.ui.quote.view.components.QuoteProgressStepper
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
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val fontScale = LocalDensity.current.fontScale
                    if (maxWidth < 320.dp || fontScale >= 1.3f) {
                        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            QuoteSecondaryButton(
                                label = stringResource(R.string.quote_contact_manager),
                                onClick = { onEvent(QuoteInquiryDetailEvent.ContactManagerClicked) },
                            )
                            QuotePrimaryButton(
                                label = stringResource(R.string.quote_request_again),
                                onClick = { onEvent(QuoteInquiryDetailEvent.RequestAgainClicked) },
                            )
                        }
                    } else {
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
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x6)) {
                    QuoteStatusChip(uiModel.status)
                    Text(uiModel.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
                    Text(
                        "${uiModel.dateLabel} · ${stringResource(R.string.quote_inquiry_number, uiModel.inquiryNumber)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                }
            }
            if (uiModel.status == QuoteInquiryStatus.Cancelled) {
                item {
                    Text(
                        stringResource(R.string.quote_detail_cancelled_notice),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
            } else {
                item {
                    QuoteCard {
                        QuoteProgressStepper(uiModel.status)
                    }
                }
            }
            uiModel.managerReply?.let { reply ->
                item {
                    QuoteCard {
                        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            Text(stringResource(R.string.quote_reply), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
                            Text(reply, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
                            HorizontalDivider(Modifier.padding(vertical = EumSpacing.x4), color = MaterialTheme.eumColors.line)
                            uiModel.amountLabel?.let { amount ->
                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Text(stringResource(R.string.quote_amount), Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
                                    Text(
                                        amount,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.eumColors.textPrimary,
                                    )
                                    Text(
                                        stringResource(R.string.quote_vat_excluded),
                                        Modifier.padding(start = EumSpacing.x4),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.eumColors.textTertiary,
                                    )
                                }
                            }
                            uiModel.validityLabel?.let { QuoteSummaryRow(stringResource(R.string.quote_valid_until), it) }
                            if (uiModel.managerName != null || uiModel.managerPhone != null) {
                                QuoteSummaryRow(
                                    stringResource(R.string.quote_manager),
                                    listOfNotNull(uiModel.managerName, uiModel.managerPhone).joinToString(" · "),
                                )
                            }
                            uiModel.estimateFile?.let { attachment ->
                                QuoteFileDownloadRow(
                                    fileName = attachment.fileName,
                                    downloadDescription = stringResource(R.string.quote_estimate_download),
                                    onDownload = { onEvent(QuoteInquiryDetailEvent.EstimateDownloadClicked) },
                                )
                            }
                        }
                    }
                }
            }
            item {
                QuoteCard {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(stringResource(R.string.quote_requester_information), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
                        QuoteSummaryRow(stringResource(R.string.quote_company_name), uiModel.companyName)
                        QuoteSummaryRow(stringResource(R.string.quote_summary_contact), uiModel.contactName)
                        QuoteSummaryRow(stringResource(R.string.quote_phone_number), uiModel.contactPhone)
                        QuoteSummaryRow(stringResource(R.string.quote_email_address), uiModel.contactEmail)
                    }
                }
            }
            item {
                QuoteCard {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                        Text(stringResource(R.string.quote_requested_products), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
                        uiModel.items.forEachIndexed { index, item ->
                            if (index > 0) HorizontalDivider(color = MaterialTheme.eumColors.line)
                            QuoteDetailItemRow(item.name, item.quantityLabel)
                        }
                    }
                }
            }
            item {
                QuoteCard {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(stringResource(R.string.quote_request_details), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
                        uiModel.preferredDeliveryDate?.let { QuoteSummaryRow(stringResource(R.string.quote_preferred_delivery_date), it) }
                        uiModel.deliveryAddress?.let { QuoteSummaryRow(stringResource(R.string.quote_delivery_address), it) }
                        uiModel.requestDetails?.let { QuoteSummaryRow(stringResource(R.string.quote_detail_content_label), it) }
                        uiModel.requestAttachments.forEach { attachment ->
                            QuoteFileDownloadRow(
                                fileName = attachment.fileName,
                                downloadDescription = stringResource(R.string.quote_file_download),
                                onDownload = { onEvent(QuoteInquiryDetailEvent.RequestAttachmentClicked(attachment.id)) },
                            )
                        }
                    }
                }
            }
        }
    }
}
