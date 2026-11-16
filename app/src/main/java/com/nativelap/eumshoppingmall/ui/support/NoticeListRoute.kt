package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable

@Composable
fun NoticeListRoute(
    notices: List<NoticeItemUiModel>,
    onBackClick: () -> Unit,
    onNoticeClick: (noticeId: String) -> Unit = {},
) {
    NoticeListScreen(
        notices = notices,
        onEvent = { event ->
            when (event) {
                NoticeScreenEvent.BackClicked -> onBackClick()
                is NoticeScreenEvent.NoticeClicked -> onNoticeClick(event.id)
            }
        },
    )
}
