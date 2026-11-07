package com.nativelap.eumshoppingmall.ui.quote.viewmodel

data class QuoteDetailItemUiModel(
    val id: String,
    val name: String,
    val quantityLabel: String,
)

data class QuoteInquiryDetailUiModel(
    val title: String,
    val dateLabel: String,
    val inquiryNumber: String,
    val status: QuoteInquiryStatus,
    val managerReply: String? = null,
    val amountLabel: String? = null,
    val validityLabel: String? = null,
    val managerName: String? = null,
    val managerPhone: String? = null,
    val estimateFile: QuoteAttachmentUiModel? = null,
    val requestAttachments: List<QuoteAttachmentUiModel> = emptyList(),
    val companyName: String,
    val contactName: String,
    val contactPhone: String,
    val contactEmail: String,
    val items: List<QuoteDetailItemUiModel> = emptyList(),
    val preferredDeliveryDate: String? = null,
    val deliveryAddress: String? = null,
    val requestDetails: String? = null,
)

sealed interface QuoteInquiryDetailEvent {
    data object BackClicked : QuoteInquiryDetailEvent
    data object ContactManagerClicked : QuoteInquiryDetailEvent
    data object RequestAgainClicked : QuoteInquiryDetailEvent
    data object EstimateDownloadClicked : QuoteInquiryDetailEvent
    data class RequestAttachmentClicked(val attachmentId: String) : QuoteInquiryDetailEvent
}
