package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable

@Composable
fun NoticeListRoute(
    notices: List<NoticeItemUiModel>,
    onBackClick: () -> Unit,
) {
    NoticeListScreen(
        notices = notices,
        onEvent = { event ->
            when (event) {
                NoticeScreenEvent.BackClicked -> onBackClick()

                // 공지 상세(A-41)는 2단계 흐름이라 이번 범위가 아니다.
                is NoticeScreenEvent.NoticeClicked -> Unit
            }
        },
    )
}
