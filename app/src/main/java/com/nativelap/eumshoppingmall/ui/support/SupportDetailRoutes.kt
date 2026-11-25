package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R

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
 * 1:1 문의 작성. 서버 연동 전이라 제출은 저장하지 않고 준비 중 안내([onUnavailableAction])만 보여 주며 화면을 닫지 않는다.
 * 유형·주문 선택과 내용은 화면 상태일 뿐 저장되지 않는다. 첨부도 준비 중 안내를 표시한다. 내용은 최대 글자 수에서 자른다.
 */
@Composable
fun InquiryWriteRoute(
    uiModel: OneToOneInquiryUiModel,
    onBackClick: () -> Unit,
    onUnavailableAction: () -> Unit,
    onCustomerSupportClick: () -> Unit,
    // 가격 미표시 상품에서 들어왔을 때 문의 제목에 상품명을 미리 채운다. 빈 값이면 채우지 않는다.
    prefilledProductName: String = "",
) {
    val prefilledTitle = if (prefilledProductName.isBlank()) {
        uiModel.title
    } else {
        stringResource(R.string.inquiry_price_prefill_title, prefilledProductName)
    }
    // 가격 미표시 상품에서 들어오면 문의 유형 기본값을 "상품"으로 둔다.
    var selectedType by rememberSaveable {
        mutableStateOf(if (prefilledProductName.isBlank()) uiModel.selectedType else OneToOneInquiryType.Product)
    }
    var selectedOrderId by rememberSaveable { mutableStateOf(uiModel.selectedOrderId) }
    var title by rememberSaveable { mutableStateOf(prefilledTitle) }
    var content by rememberSaveable { mutableStateOf(uiModel.content) }
    // 펼침 상태는 Route가 보관한다. 화면 회전에도 남도록 rememberSaveable을 쓰고, 항목을 고르면 닫는다.
    var isTypeListExpanded by rememberSaveable { mutableStateOf(uiModel.isTypeListExpanded) }
    var isOrderListExpanded by rememberSaveable { mutableStateOf(uiModel.isOrderListExpanded) }
    OneToOneInquiryFormScreen(
        isSubmissionAvailable = false,
        uiModel = uiModel.copy(
            selectedType = selectedType,
            selectedOrderId = selectedOrderId,
            title = title,
            content = content,
            isTypeListExpanded = isTypeListExpanded,
            isOrderListExpanded = isOrderListExpanded,
        ),
        onEvent = { event ->
            when (event) {
                OneToOneInquiryFormEvent.BackClicked -> onBackClick()

                OneToOneInquiryFormEvent.TypeFieldClicked -> isTypeListExpanded = !isTypeListExpanded

                is OneToOneInquiryFormEvent.TypeSelected -> {
                    selectedType = event.type
                    isTypeListExpanded = false
                }

                OneToOneInquiryFormEvent.OrderFieldClicked -> isOrderListExpanded = !isOrderListExpanded

                is OneToOneInquiryFormEvent.OrderSelected -> {
                    selectedOrderId = event.orderId
                    isOrderListExpanded = false
                }

                is OneToOneInquiryFormEvent.TitleChanged -> title = event.title

                is OneToOneInquiryFormEvent.ContentChanged -> content = limitOneToOneInquiryContent(event.content)

                // 서버 연동 전이라 제출은 저장하지 않고 준비 중 안내만 낸다(화면 쪽에서 버튼도 비활성이다).
                OneToOneInquiryFormEvent.SubmitClicked -> onUnavailableAction()

                OneToOneInquiryFormEvent.CustomerSupportClicked -> onCustomerSupportClick()

                OneToOneInquiryFormEvent.AttachmentClicked -> onUnavailableAction()
            }
        },
    )
}
