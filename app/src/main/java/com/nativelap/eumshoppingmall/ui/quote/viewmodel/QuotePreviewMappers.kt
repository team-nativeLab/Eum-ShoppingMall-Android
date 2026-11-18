package com.nativelap.eumshoppingmall.ui.quote.viewmodel

fun unavailableQuoteCompletePreview(message: String): QuoteCompleteUiModel = QuoteCompleteUiModel(
    inquiryNumber = "",
    responseTimeLabel = "",
    requestTypeLabel = "",
    companyName = "",
    contactName = "",
    productsSummary = message,
    preferredDeliveryDate = "",
    isPreviewUnavailable = true,
)

fun unavailableQuoteInquiryDetailPreview(message: String): QuoteInquiryDetailUiModel = QuoteInquiryDetailUiModel(
    title = message,
    dateLabel = "",
    inquiryNumber = "",
    status = QuoteInquiryStatus.Preview,
    companyName = "",
    contactName = "",
    contactPhone = "",
    contactEmail = "",
    isPreviewUnavailable = true,
)

fun QuoteRequestUiModel.toQuoteCompletePreview(
    requestTypeLabel: String,
    noProductsLabel: String = "선택 상품 없음",
): QuoteCompleteUiModel = QuoteCompleteUiModel(
    inquiryNumber = "",
    responseTimeLabel = "",
    requestTypeLabel = requestTypeLabel,
    companyName = companyName,
    contactName = contactName,
    productsSummary = products.joinToString(" · ") { product -> product.name }.ifBlank { noProductsLabel },
    preferredDeliveryDate = preferredDeliveryDate,
)

fun QuoteRequestUiModel.toQuoteHistoryPreview(
    id: String,
    previewSessionLabel: String,
    previewBadgeLabel: String,
    noProductsLabel: String,
    dueDateLabel: String,
): QuoteInquiryListItemUiModel = QuoteInquiryListItemUiModel(
    id = id,
    dateLabel = previewSessionLabel,
    inquiryNumber = previewBadgeLabel,
    title = listOf(companyName, contactName).filter(String::isNotBlank).joinToString(" · "),
    productsSummary = listOf(
        products.joinToString(" · ") { product -> product.name }.ifBlank { noProductsLabel },
        "$dueDateLabel: $preferredDeliveryDate",
    ).joinToString(" · "),
    status = QuoteInquiryStatus.Preview,
)
