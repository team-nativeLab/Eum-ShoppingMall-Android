package com.nativelap.eumshoppingmall.ui.events

import androidx.compose.runtime.Composable

@Composable
fun EventListRoute(
    events: List<EventUiModel>,
    onBackClick: () -> Unit,
) {
    EventListScreen(
        events = events,
        onEvent = { event ->
            when (event) {
                EventListScreenEvent.BackClicked -> onBackClick()

                // 이벤트 상세(A-55)는 2단계 흐름이라 이번 범위가 아니다.
                is EventListScreenEvent.EventClicked -> Unit
            }
        },
    )
}
