package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryAction
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel

@Composable
fun OrderHistoryRoute(
    uiModel: OrderHistoryUiModel,
    onBackClick: () -> Unit,
    onBrowseProductsClick: () -> Unit,
    onOrderDetailsClick: (orderId: String) -> Unit = {},
    onTrackDeliveryClick: (orderId: String) -> Unit = {},
    onCancelClick: (orderId: String) -> Unit = {},
    onCancelReturnClick: () -> Unit = {},
    onUnavailableAction: () -> Unit,
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

                OrderHistoryScreenEvent.CancelReturnClicked -> onCancelReturnClick()

                is OrderHistoryScreenEvent.OrderDetailsClicked -> onOrderDetailsClick(event.orderId)

                is OrderHistoryScreenEvent.ActionClicked -> when (event.action) {
                    OrderHistoryAction.TrackDelivery -> onTrackDeliveryClick(event.orderId)
                    OrderHistoryAction.Cancel -> onCancelClick(event.orderId)
                    OrderHistoryAction.BuyAgain -> onUnavailableAction()
                }
            }
        },
    )
}
