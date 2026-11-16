package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun InquiryHistoryRoute(
    items: List<InquiryHistoryItemUiModel>,
    onBackClick: () -> Unit,
    onItemClick: (inquiryId: String) -> Unit = {},
    onWriteClick: () -> Unit = {},
) {
    var selectedStatus by rememberSaveable { mutableStateOf<InquiryStatus?>(null) }
    val visibleItems = remember(items, selectedStatus) {
        val status = selectedStatus
        if (status == null) items else items.filter { item -> item.status == status }
    }
    InquiryHistoryScreen(
        items = visibleItems,
        selectedStatus = selectedStatus,
        onEvent = { event ->
            when (event) {
                InquiryHistoryEvent.BackClicked -> onBackClick()
                is InquiryHistoryEvent.FilterSelected -> selectedStatus = event.status
                is InquiryHistoryEvent.ItemClicked -> onItemClick(event.id)
                InquiryHistoryEvent.WriteClicked -> onWriteClick()
            }
        },
    )
}
