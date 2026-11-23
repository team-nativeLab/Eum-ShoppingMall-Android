package com.nativelap.eumshoppingmall.ui.policy

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * 원문이 아직 없는 정책 문서 화면(이용약관·개인정보처리방침).
 * 자리표시자 본문 대신 "준비 중" 안내만 보여 주며, 날짜·조항 목록은 만들지 않는다.
 */
@Composable
fun PolicyUnavailableRoute(
    @StringRes titleRes: Int,
    @StringRes noticeRes: Int,
    onBackClick: () -> Unit,
) {
    PolicyDocumentScreen(
        uiModel = PolicyDocumentUiModel(
            title = stringResource(titleRes),
            dateLabel = null,
            isEffectiveDate = false,
            sections = emptyList(),
            noticeLabel = stringResource(noticeRes),
        ),
        onEvent = { event ->
            if (event == PolicyDocumentEvent.BackClicked) onBackClick()
        },
    )
}
