package com.nativelap.eumshoppingmall.ui.events

import androidx.compose.runtime.Composable

@Composable
fun EventListRoute(
    events: List<EventUiModel>,
    onBackClick: () -> Unit,
    onEventClick: (eventId: String) -> Unit = {},
) {
    EventListScreen(
        events = events,
        onEvent = { event ->
            when (event) {
                EventListScreenEvent.BackClicked -> onBackClick()
                is EventListScreenEvent.EventClicked -> onEventClick(event.eventId)
            }
        },
    )
}
