package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryUiModel

@Composable
fun QuoteHistoryRoute(
    uiModel: QuoteHistoryUiModel,
    onBackClick: () -> Unit,
    onInquiryClick: (inquiryId: String) -> Unit,
    onNewInquiryClick: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    var selectedFilter by rememberSaveable { mutableStateOf(uiModel.selectedFilter) }
    QuoteHistoryScreen(
        uiModel = uiModel.copy(selectedFilter = selectedFilter),
        onEvent = { event ->
            when (event) {
                QuoteHistoryEvent.BackClicked -> onBackClick()
                is QuoteHistoryEvent.FilterSelected -> selectedFilter = event.filter
                is QuoteHistoryEvent.InquiryClicked -> onInquiryClick(event.inquiryId)
                QuoteHistoryEvent.NewInquiryClicked -> onNewInquiryClick()
                is QuoteHistoryEvent.EstimateClicked -> onUnavailableAction()
            }
        },
    )
}
