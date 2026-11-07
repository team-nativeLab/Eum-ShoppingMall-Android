package com.nativelap.eumshoppingmall.ui.quote.viewmodel

data class QuoteCompleteUiModel(
    val inquiryNumber: String,
    val responseTimeLabel: String,
    val requestTypeLabel: String,
    val companyName: String,
    val contactName: String,
    val productsSummary: String,
    val preferredDeliveryDate: String,
)

sealed interface QuoteCompleteEvent {
    data object CloseClicked : QuoteCompleteEvent
    data object ContinueShoppingClicked : QuoteCompleteEvent
    data object ViewInquiriesClicked : QuoteCompleteEvent
}
