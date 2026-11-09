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

                // 1:1 문의 상세와 작성(A-31 하위)은 이번 범위가 아니다.
                is InquiryHistoryEvent.ItemClicked,
                InquiryHistoryEvent.WriteClicked,
                -> Unit
            }
        },
    )
}
