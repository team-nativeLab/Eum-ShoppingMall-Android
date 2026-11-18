package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/** 공지 상세(A-41). */
@Composable
fun NoticeDetailRoute(
    uiModel: NoticeDetailUiModel,
    onBackClick: () -> Unit,
) {
    NoticeDetailScreen(uiModel = uiModel, onBack = onBackClick)
}

/** 1:1 문의 상세. 첨부 파일은 지원 안내를 표시한다. */
@Composable
fun InquiryDetailRoute(
    uiModel: InquiryDetailUiModel,
    onBackClick: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    InquiryDetailScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                InquiryDetailEvent.BackClicked -> onBackClick()
                is InquiryDetailEvent.AttachmentClicked -> onUnavailableAction()
            }
        },
    )
}

/**
 * 1:1 문의 작성. 서버 연동 전이라 제출은 입력이 채워졌을 때 [onSubmitted]만 부르고 내용은 저장하지 않는다.
 * 문의 유형 선택·첨부는 지원 안내를 표시한다.
 */
@Composable
fun InquiryWriteRoute(
    uiModel: OneToOneInquiryUiModel,
    onBackClick: () -> Unit,
    onSubmitted: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    var title by rememberSaveable { mutableStateOf(uiModel.title) }
    var content by rememberSaveable { mutableStateOf(uiModel.content) }
    OneToOneInquiryFormScreen(
        uiModel = uiModel.copy(title = title, content = content),
        onEvent = { event ->
            when (event) {
                OneToOneInquiryFormEvent.BackClicked -> onBackClick()

                is OneToOneInquiryFormEvent.TitleChanged -> title = event.title

                is OneToOneInquiryFormEvent.ContentChanged -> content = event.content

                OneToOneInquiryFormEvent.SubmitClicked -> if (title.isNotBlank() && content.isNotBlank()) onSubmitted()

                OneToOneInquiryFormEvent.InquiryTypeClicked,
                OneToOneInquiryFormEvent.AttachmentClicked,
                -> onUnavailableAction()
            }
        },
    )
}
