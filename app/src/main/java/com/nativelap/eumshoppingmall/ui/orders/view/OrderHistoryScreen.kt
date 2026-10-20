package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrdersTopBar
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryEmptyState
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderHistoryScreen(
    uiModel: OrderHistoryUiModel,
    onEvent: (OrderHistoryScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            OrdersTopBar(
                title = stringResource(R.string.orders_title),
                actionLabel = stringResource(R.string.orders_cancel_return),
                onBackClick = { onEvent(OrderHistoryScreenEvent.BackClicked) },
                onActionClick = { onEvent(OrderHistoryScreenEvent.CancelReturnClicked) },
                modifier = Modifier.statusBarsPadding().background(MaterialTheme.eumColors.background),
            )
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        if (uiModel.orders.isEmpty() && uiModel.emptyState != OrderHistoryEmptyState.NoMatchingOrders) {
            EmptyOrderHistory(
                emptyState = uiModel.emptyState ?: OrderHistoryEmptyState.NoOrders,
                onBrowseProducts = { onEvent(OrderHistoryScreenEvent.BrowseProductsClicked) },
                modifier = Modifier.fillMaxSize().padding(contentPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
            ) {
                item {
                    androidx.compose.foundation.layout.Row(
                        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        OrderHistoryFilter.entries.forEach { filter ->
                            OrderHistoryFilterChip(
                                filter = filter,
                                selected = filter == uiModel.filter,
                                onClick = { onEvent(OrderHistoryScreenEvent.FilterSelected(filter)) },
                            )
                        }
                    }
                }
                item {
                    Box(Modifier.padding(horizontal = EumSpacing.x20)) {
                        TextButton(
                            onClick = { onEvent(OrderHistoryScreenEvent.PeriodPickerToggled) },
                            modifier = Modifier.heightIn(min = 48.dp),
                        ) {
                            Text(uiModel.periodLabel, color = MaterialTheme.eumColors.textPrimary)
                            Icon(painterResource(R.drawable.ic_chevron_down), contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                        DropdownMenu(
                            expanded = uiModel.isPeriodPickerVisible,
                            onDismissRequest = { onEvent(OrderHistoryScreenEvent.PeriodPickerToggled) },
                        ) {
                            uiModel.periods.forEach { period ->
                                DropdownMenuItem(
                                    text = { Text(period) },
                                    onClick = { onEvent(OrderHistoryScreenEvent.PeriodSelected(period)) },
                                )
                            }
                        }
                    }
                }
                if (uiModel.orders.isEmpty()) {
                    item {
                        NoMatchingOrderResults(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x32),
                            onResetFilter = { onEvent(OrderHistoryScreenEvent.FiltersReset) },
                        )
                    }
                } else {
                    items(uiModel.orders, key = { it.id }) { order ->
                        OrderHistoryCard(
                            order = order,
                            onDetailsClicked = { onEvent(OrderHistoryScreenEvent.OrderDetailsClicked(order.id)) },
                            onActionClicked = { onEvent(OrderHistoryScreenEvent.ActionClicked(order.id, it)) },
                            modifier = Modifier.padding(horizontal = EumSpacing.x20),
                        )
                    }
                }
                item { androidx.compose.foundation.layout.Spacer(Modifier.size(EumSpacing.x12)) }
            }
        }
    }
}

@Composable
private fun OrderHistoryFilterChip(filter: OrderHistoryFilter, selected: Boolean, onClick: () -> Unit) {
    val labelResId = when (filter) {
        OrderHistoryFilter.All -> R.string.orders_filter_all
        OrderHistoryFilter.WaitingPayment -> R.string.orders_status_waiting_payment
        OrderHistoryFilter.PaymentComplete -> R.string.orders_status_paid
        OrderHistoryFilter.Preparing -> R.string.orders_status_preparing
        OrderHistoryFilter.Shipping -> R.string.orders_status_in_transit
        OrderHistoryFilter.Delivered -> R.string.orders_status_delivered
    }
    Surface(
        modifier = Modifier.heightIn(min = 48.dp).clickable(role = Role.RadioButton, onClick = onClick).semantics { this.selected = selected },
        shape = RoundedCornerShape(50),
        color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
        border = if (selected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
    ) {
        Text(
            stringResource(labelResId),
            modifier = Modifier.padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8),
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary,
        )
    }
}

@Composable
private fun NoMatchingOrderResults(modifier: Modifier = Modifier, onResetFilter: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Text(stringResource(R.string.orders_no_results_title), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Text(
            stringResource(R.string.orders_no_results_description),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textSecondary,
            textAlign = TextAlign.Center,
        )
        TextButton(onClick = onResetFilter, modifier = Modifier.heightIn(min = 48.dp)) {
            Text(stringResource(R.string.orders_clear_filter))
        }
    }
}

@Composable
private fun EmptyOrderHistory(
    emptyState: OrderHistoryEmptyState,
    onBrowseProducts: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(horizontal = EumSpacing.x32),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.eumColors.surface, modifier = Modifier.size(64.dp)) {
                Icon(
                    painterResource(R.drawable.ic_message_square_text),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textTertiary,
                    modifier = Modifier.padding(EumSpacing.x20),
                )
            }
            Text(
                stringResource(if (emptyState == OrderHistoryEmptyState.NoOrders) R.string.orders_empty_title else R.string.orders_no_results_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                stringResource(if (emptyState == OrderHistoryEmptyState.NoOrders) R.string.orders_empty_description else R.string.orders_no_results_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
        Button(
            onClick = onBrowseProducts,
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = EumSpacing.x20).padding(bottom = 112.dp).navigationBarsPadding().heightIn(min = 52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.eumColors.textPrimary, contentColor = MaterialTheme.eumColors.inverse),
            shape = RoundedCornerShape(EumRadius.x8),
        ) {
            Text(stringResource(R.string.orders_browse_products))
        }
    }
}
