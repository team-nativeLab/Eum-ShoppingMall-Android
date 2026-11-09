package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel

@Composable
fun OrderHistoryRoute(
    uiModel: OrderHistoryUiModel,
    onBackClick: () -> Unit,
    onBrowseProductsClick: () -> Unit,
) {
    var selectedFilter by rememberSaveable { mutableStateOf(uiModel.filter) }
    var periodLabel by rememberSaveable { mutableStateOf(uiModel.periodLabel) }
    var isPeriodPickerVisible by rememberSaveable { mutableStateOf(uiModel.isPeriodPickerVisible) }
    OrderHistoryScreen(
        uiModel = uiModel.copy(
            filter = selectedFilter,
            periodLabel = periodLabel,
            isPeriodPickerVisible = isPeriodPickerVisible,
        ),
        onEvent = { event ->
            when (event) {
                OrderHistoryScreenEvent.BackClicked -> onBackClick()

                is OrderHistoryScreenEvent.FilterSelected -> selectedFilter = event.filter

                OrderHistoryScreenEvent.FiltersReset -> selectedFilter = OrderHistoryFilter.All

                OrderHistoryScreenEvent.PeriodPickerToggled -> isPeriodPickerVisible = !isPeriodPickerVisible

                is OrderHistoryScreenEvent.PeriodSelected -> {
                    periodLabel = event.period
                    isPeriodPickerVisible = false
                }

                OrderHistoryScreenEvent.BrowseProductsClicked -> onBrowseProductsClick()

                // 주문 상세(A-17)·배송 조회(A-18)·취소·반품(A-19)은 2단계 흐름이라 이번 범위가 아니다.
                OrderHistoryScreenEvent.CancelReturnClicked,
                is OrderHistoryScreenEvent.OrderDetailsClicked,
                is OrderHistoryScreenEvent.ActionClicked,
                -> Unit
            }
        },
    )
}
