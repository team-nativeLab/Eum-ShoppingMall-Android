package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoCard
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryEmptyState
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHubScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestOrderSelectionScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestOrderSelectionUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderRequestHubScreen(onEvent: (OrderRequestHubScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_hub_title), { onEvent(OrderRequestHubScreenEvent.BackClicked) }, modifier) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
            HubActionCard(stringResource(R.string.request_orders_title), stringResource(R.string.request_hub_orders_description)) { onEvent(OrderRequestHubScreenEvent.OrdersClicked) }
            HubActionCard(stringResource(R.string.request_history_title), stringResource(R.string.request_hub_history_description)) { onEvent(OrderRequestHubScreenEvent.RequestHistoryClicked) }
            HubActionCard(stringResource(R.string.request_start_title), stringResource(R.string.request_hub_start_description)) { onEvent(OrderRequestHubScreenEvent.StartRequestClicked) }
        }
    }
}

@Composable
fun OrderRequestOrderSelectionScreen(uiModel: OrderRequestOrderSelectionUiModel, onEvent: (OrderRequestOrderSelectionScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_order_selection_screen_title), { onEvent(OrderRequestOrderSelectionScreenEvent.BackClicked) }, modifier) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(vertical = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            item {
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    OrderHistoryFilter.entries.forEach { f -> FilterPill(orderFilterLabel(f), uiModel.filter == f) { onEvent(OrderRequestOrderSelectionScreenEvent.FilterSelected(f)) } }
                }
            }
            item {
                Surface(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20), shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.backgroundSubtle) {
                    Text(stringResource(R.string.request_selection_notice), Modifier.padding(EumSpacing.x16), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
            item {
                Box(Modifier.padding(horizontal = EumSpacing.x20)) {
                    TextButton(onClick = { onEvent(OrderRequestOrderSelectionScreenEvent.PeriodPickerToggled) }, modifier = Modifier.heightIn(min = 48.dp), colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textPrimary)) {
                        Text(uiModel.periodLabel, style = MaterialTheme.typography.bodyMedium)
                        Text(" ⌄", style = MaterialTheme.typography.bodyMedium)
                    }
                    DropdownMenu(expanded = uiModel.isPeriodPickerVisible, onDismissRequest = { onEvent(OrderRequestOrderSelectionScreenEvent.PeriodPickerToggled) }) {
                        uiModel.periods.forEach { period -> DropdownMenuItem(text = { Text(period) }, onClick = { onEvent(OrderRequestOrderSelectionScreenEvent.PeriodSelected(period)) }) }
                    }
                }
            }
            if (uiModel.orders.isEmpty()) {
                item {
                    val hasNoMatchingOrders = uiModel.emptyState == OrderHistoryEmptyState.NoMatchingOrders
                    EmptyRequestState(
                        title = stringResource(if (hasNoMatchingOrders) R.string.request_no_matching_orders_title else R.string.request_no_orders_title),
                        description = stringResource(if (hasNoMatchingOrders) R.string.request_no_matching_orders_description else R.string.request_no_orders_description),
                        modifier = Modifier.fillMaxWidth().padding(EumSpacing.x32),
                        actionLabel = if (hasNoMatchingOrders) stringResource(R.string.request_reset_filters) else null,
                        onAction = if (hasNoMatchingOrders) ({ onEvent(OrderRequestOrderSelectionScreenEvent.FiltersReset) }) else null,
                    )
                }
            }
            items(uiModel.orders, key = { it.id }) { order ->
                OrderInfoCard(Modifier.padding(horizontal = EumSpacing.x20)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(order.orderDateLabel, style = MaterialTheme.typography.titleSmall)
                        Text(stringResource(R.string.request_order_number_format, order.orderNumber), Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                        TextButton(onClick = { onEvent(OrderRequestOrderSelectionScreenEvent.OrderDetailsClicked(order.id)) }, modifier = Modifier.heightIn(min = 48.dp), colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textPrimary)) {
                            Text(stringResource(R.string.orders_detail), style = MaterialTheme.typography.labelMedium)
                            Text(" ›", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    order.products.forEach { RequestProductRow(it, order.status) }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        OutlinedButton(onClick = { onEvent(OrderRequestOrderSelectionScreenEvent.OrderDeliveryClicked(order.id)) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text(stringResource(R.string.request_delivery_action)) }
                        OutlinedButton(onClick = { onEvent(OrderRequestOrderSelectionScreenEvent.OrderRequestSelected(order.id)) }, enabled = order.canSelectForRequest, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text(stringResource(R.string.request_order_select_action)) }
                    }
                }
            }
        }
    }
}
