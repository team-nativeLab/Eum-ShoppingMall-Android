package com.nativelap.eumshoppingmall.ui.productinquiry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * 상품 문의 작성(A-44). 서버 연동 전이라 제출은 저장하지 않고 준비 중 안내([onUnavailableAction])만 보여 주며 화면을 닫지 않는다.
 * 제목·내용·비공개 설정은 화면 상태일 뿐 저장되지 않는다.
 */
@Composable
fun ProductInquiryRoute(
    onBackClick: () -> Unit,
    onUnavailableAction: () -> Unit,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var isPrivate by rememberSaveable { mutableStateOf(false) }
    ProductInquiryScreen(
        title = title,
        content = content,
        isPrivate = isPrivate,
        onEvent = { event ->
            when (event) {
                ProductInquiryScreenEvent.BackClicked -> onBackClick()

                is ProductInquiryScreenEvent.TitleChanged -> title = event.title

                is ProductInquiryScreenEvent.ContentChanged -> content = event.content

                is ProductInquiryScreenEvent.PrivateModeChanged -> isPrivate = event.isPrivate

                ProductInquiryScreenEvent.SubmitClicked -> when (resolveProductInquirySubmit()) {
                    ProductInquirySubmitOutcome.UnavailableNotice -> onUnavailableAction()
                }
            }
        },
    )
}

/** 제출의 결과. 서버 연동 전이라 저장 없이 준비 중 안내만 낸다. 완료 화면으로 이동하지 않는다. */
enum class ProductInquirySubmitOutcome { UnavailableNotice, }

/** 제출 동작을 정한다. 저장하지 않으므로 어떤 입력이든 준비 중 안내로 끝난다. */
fun resolveProductInquirySubmit(): ProductInquirySubmitOutcome = ProductInquirySubmitOutcome.UnavailableNotice

/** 상품 문의 완료(A-45). */
@Composable
fun ProductInquiryCompleteRoute(
    onBackClick: () -> Unit,
    onInquiriesClick: () -> Unit,
) {
    ProductInquiryCompleteScreen(
        onEvent = { event ->
            when (event) {
                ProductInquiryCompleteEvent.BackClicked -> onBackClick()
                ProductInquiryCompleteEvent.InquiriesClicked -> onInquiriesClick()
            }
        },
    )
}
