package com.nativelap.eumshoppingmall.ui.productreview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/** 상품 전체 리뷰(A-49). 리뷰 작성은 로그인이 필요해 호출부가 게이트를 거친다. */
@Composable
fun ProductReviewRoute(
    uiModel: ProductReviewUiModel,
    onBackClick: () -> Unit,
    onReviewWriteClick: () -> Unit,
) {
    ProductReviewScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                ProductReviewScreenEvent.BackClicked -> onBackClick()
                ProductReviewScreenEvent.ReviewWriteClicked -> onReviewWriteClick()
            }
        },
    )
}

/**
 * 리뷰 작성. 서버 연동 전이라 제출은 별점과 내용이 채워졌을 때 [onSubmitted]만 부르고 내용은 저장하지 않는다.
 * 사진 첨부는 선택기 연동 전이라 연결하지 않는다.
 */
@Composable
fun ProductReviewWriteRoute(
    uiModel: ProductReviewWriteUiModel,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSubmitted: () -> Unit,
) {
    var rating by rememberSaveable { mutableStateOf(uiModel.rating) }
    var content by rememberSaveable { mutableStateOf(uiModel.content) }
    val canSubmit = rating > 0 && content.isNotBlank()
    ProductReviewWriteScreen(
        uiModel = uiModel.copy(rating = rating, content = content, canSubmit = canSubmit),
        onEvent = { event ->
            when (event) {
                ProductReviewWriteEvent.BackClicked -> onBackClick()
                ProductReviewWriteEvent.SearchClicked -> onSearchClick()
                is ProductReviewWriteEvent.RatingSelected -> rating = event.value
                is ProductReviewWriteEvent.ContentChanged -> content = event.value
                ProductReviewWriteEvent.SubmitClicked -> if (canSubmit) onSubmitted()
                ProductReviewWriteEvent.PhotoClicked -> Unit
            }
        },
    )
}
