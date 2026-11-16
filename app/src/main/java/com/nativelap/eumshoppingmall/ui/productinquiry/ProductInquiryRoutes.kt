package com.nativelap.eumshoppingmall.ui.productinquiry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * 상품 문의 작성(A-44). 서버 연동 전이라 제출은 제목과 내용이 채워졌을 때 [onSubmitted]만 부르고 내용은 저장하지 않는다.
 */
@Composable
fun ProductInquiryRoute(
    onBackClick: () -> Unit,
    onSubmitted: () -> Unit,
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
                ProductInquiryScreenEvent.SubmitClicked -> if (title.isNotBlank() && content.isNotBlank()) onSubmitted()
            }
        },
    )
}

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
