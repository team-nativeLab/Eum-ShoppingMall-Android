package com.nativelap.eumshoppingmall.ui.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.LocalDate

/** 이벤트 목록(A-54). 탭 선택은 화면 회전에도 남기고, 기준 날짜는 화면을 만들 때의 오늘이다. */
@Composable
fun EventListRoute(
    events: List<EventUiModel>,
    onBackClick: () -> Unit,
    onEventClick: (eventId: String) -> Unit = {},
) {
    var selectedTab by rememberSaveable { mutableStateOf(EventListTab.All) }
    val today = remember { LocalDate.now() }
    EventListScreen(
        events = events,
        today = today,
        selectedTab = selectedTab,
        onEvent = { event ->
            when (event) {
                EventListScreenEvent.BackClicked -> onBackClick()
                is EventListScreenEvent.EventClicked -> onEventClick(event.eventId)
                is EventListScreenEvent.TabSelected -> selectedTab = event.tab
            }
        },
    )
}
