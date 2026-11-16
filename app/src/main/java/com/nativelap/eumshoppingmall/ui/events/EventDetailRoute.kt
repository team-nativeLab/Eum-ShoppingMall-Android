package com.nativelap.eumshoppingmall.ui.events

import androidx.compose.runtime.Composable

/** 이벤트 상세(A-55). */
@Composable
fun EventDetailRoute(
    uiModel: EventDetailUiModel,
    onBackClick: () -> Unit,
    onBrowseProductsClick: () -> Unit,
) {
    EventDetailScreen(
        uiModel = uiModel,
        onEvent = { event ->
            when (event) {
                EventDetailScreenEvent.BackClicked -> onBackClick()
                EventDetailScreenEvent.BrowseProductsClicked -> onBrowseProductsClick()
            }
        },
    )
}
