package com.nativelap.eumshoppingmall.ui.quote.viewmodel

enum class QuoteHistoryFilter {
    All,
    Received,
    Consulting,
    EstimateSent,
    Complete,
    Cancelled,
}

enum class QuoteInquiryStatus {
    Received,
    Consulting,
    EstimateSent,
    Complete,
    Cancelled,
}

data class QuoteInquiryListItemUiModel(
    val id: String,
    val dateLabel: String,
    val inquiryNumber: String,
    val title: String,
    val productsSummary: String,
    val status: QuoteInquiryStatus,
    val estimateAvailable: Boolean = false,
)

data class QuoteHistoryUiModel(
    val selectedFilter: QuoteHistoryFilter = QuoteHistoryFilter.All,
    val inquiries: List<QuoteInquiryListItemUiModel> = emptyList(),
)

sealed interface QuoteHistoryEvent {
    data object BackClicked : QuoteHistoryEvent
    data class FilterSelected(val filter: QuoteHistoryFilter) : QuoteHistoryEvent
    data class InquiryClicked(val inquiryId: String) : QuoteHistoryEvent
    data class EstimateClicked(val inquiryId: String) : QuoteHistoryEvent
    data object NewInquiryClicked : QuoteHistoryEvent
}
