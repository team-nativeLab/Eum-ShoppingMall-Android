package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailUiModel

@Composable
fun QuoteInquiryDetailRoute(
    uiModel: QuoteInquiryDetailUiModel,
    onBackClick: () -> Unit,
    onRequestAgainClick: () -> Unit,
) {
    QuoteInquiryDetailScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                QuoteInquiryDetailEvent.BackClicked -> onBackClick()

                QuoteInquiryDetailEvent.RequestAgainClicked -> onRequestAgainClick()

                // 담당자 연락·파일 다운로드는 시스템 앱 연동이 필요해 이번 범위가 아니다.
                QuoteInquiryDetailEvent.ContactManagerClicked,
                QuoteInquiryDetailEvent.EstimateDownloadClicked,
                is QuoteInquiryDetailEvent.RequestAttachmentClicked,
                -> Unit
            }
        },
    )
}
