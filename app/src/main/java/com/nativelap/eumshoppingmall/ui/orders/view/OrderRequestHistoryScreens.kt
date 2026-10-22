package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoCard
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderRequestHistoryScreen(uiModel: OrderRequestHistoryUiModel, onEvent: (OrderRequestHistoryScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_history_screen_title), { onEvent(OrderRequestHistoryScreenEvent.BackClicked) }, modifier) { padding ->
        if (uiModel.hasNoRequests) {
            EmptyRequestState(stringResource(R.string.request_no_requests_title), stringResource(R.string.request_no_requests_description), Modifier.fillMaxSize().padding(padding).padding(EumSpacing.x32), stringResource(R.string.request_orders_title)) { onEvent(OrderRequestHistoryScreenEvent.OrdersClicked) }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(vertical = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                item { Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) { OrderRequestHistoryFilter.entries.forEach { filter -> FilterPill(historyFilterLabel(filter), uiModel.filter == filter) { onEvent(OrderRequestHistoryScreenEvent.FilterSelected(filter)) } } } }
                items(uiModel.items, key = { it.id }) { item ->
                    OrderInfoCard(Modifier.padding(horizontal = EumSpacing.x20)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(stringResource(R.string.request_request_type_format, requestTypeLabel(item.requestType)), style = MaterialTheme.typography.titleSmall)
                                Text(item.requestDateLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                            }
                            StatusLabel(item.status)
                        }
                        Text(stringResource(R.string.request_order_number_format, item.orderNumber), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        Text(item.productName, style = MaterialTheme.typography.bodyMedium)
                        Text(item.priceAndQuantityLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        item.expectedAmountLabel?.let { Text(stringResource(R.string.request_expected_amount_format, it), style = MaterialTheme.typography.labelMedium) }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            OutlinedButton(onClick = { onEvent(OrderRequestHistoryScreenEvent.RequestDetailsClicked(item.id)) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text(stringResource(R.string.request_detail_screen_title)) }
                            if (item.canWithdraw) OutlinedButton(onClick = { onEvent(OrderRequestHistoryScreenEvent.WithdrawalClicked(item.id)) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text(stringResource(R.string.request_withdraw)) }
                            if (item.hasRejectionReason) OutlinedButton(onClick = { onEvent(OrderRequestHistoryScreenEvent.RejectionReasonClicked(item.id)) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text(stringResource(R.string.request_rejected_screen_title)) }
                        }
                    }
                }
                if (uiModel.items.isEmpty()) item { EmptyRequestState(stringResource(R.string.request_no_requests_title), stringResource(R.string.request_no_matching_requests), Modifier.fillMaxWidth().padding(EumSpacing.x32)) }
            }
        }
    }
}

@Composable
fun OrderRequestDetailScreen(uiModel: OrderRequestDetailUiModel, onEvent: (OrderRequestDetailScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_detail_screen_title), { onEvent(OrderRequestDetailScreenEvent.BackClicked) }, modifier, bottomContent = {
        if (uiModel.canWithdraw && uiModel.rejectionReasonLabel != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                OutlinedButton(onClick = { onEvent(OrderRequestDetailScreenEvent.WithdrawalClicked) }, modifier = Modifier.weight(1f).heightIn(min = 52.dp)) { Text(stringResource(R.string.request_withdraw)) }
                Button(onClick = { onEvent(OrderRequestDetailScreenEvent.RejectionReasonClicked) }, modifier = Modifier.weight(1f).heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_rejected_screen_title)) }
            }
        } else if (uiModel.canWithdraw) {
            OutlinedButton(onClick = { onEvent(OrderRequestDetailScreenEvent.WithdrawalClicked) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text(stringResource(R.string.request_withdraw)) }
        } else if (uiModel.rejectionReasonLabel != null) {
            Button(onClick = { onEvent(OrderRequestDetailScreenEvent.RejectionReasonClicked) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_rejected_screen_title)) }
        } else {
            Button(onClick = { onEvent(OrderRequestDetailScreenEvent.RequestHistoryClicked) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_view_history)) }
        }
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            item {
                OrderInfoCard(Modifier.fillMaxWidth()) {
                    StatusLabel(uiModel.status)
                    Text(uiModel.statusDescription, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
            item { DetailCard(stringResource(R.string.request_product_title), uiModel.productSummaryLabel) }
            item { DetailCard(stringResource(R.string.request_reason_title), uiModel.reasonSummaryLabel) }
            item { DetailCard(stringResource(R.string.request_collection_address), uiModel.collectionAddressLabel) }
            item {
                OrderInfoCard(Modifier.fillMaxWidth()) {
                    SummaryLine(stringResource(R.string.request_number), uiModel.requestNumber)
                    SummaryLine(stringResource(R.string.request_date), uiModel.requestDateLabel)
                }
            }
        }
    }
}
