package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailUiModel

@Composable
fun QuoteInquiryDetailRoute(
    uiModel: QuoteInquiryDetailUiModel,
    onBackClick: () -> Unit,
    onRequestAgainClick: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    QuoteInquiryDetailScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                QuoteInquiryDetailEvent.BackClicked -> onBackClick()

                QuoteInquiryDetailEvent.RequestAgainClicked -> onRequestAgainClick()

                QuoteInquiryDetailEvent.ContactManagerClicked,
                QuoteInquiryDetailEvent.EstimateDownloadClicked,
                is QuoteInquiryDetailEvent.RequestAttachmentClicked,
                -> onUnavailableAction()
            }
        },
    )
}
