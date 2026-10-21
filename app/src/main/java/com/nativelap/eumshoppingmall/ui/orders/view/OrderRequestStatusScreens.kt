package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoCard
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestWithdrawalScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderRequestResultScreen(uiModel: OrderRequestResultUiModel, onEvent: (OrderRequestResultScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(uiModel.statusLabel, { onEvent(OrderRequestResultScreenEvent.BackClicked) }, modifier, bottomContent = { Button(onClick = { onEvent(OrderRequestResultScreenEvent.RequestHistoryClicked) }, Modifier.fillMaxWidth().heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_view_history)) } }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = EumSpacing.x24), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Surface(shape = CircleShape, color = MaterialTheme.eumColors.surfaceSecondary, modifier = Modifier.size(72.dp)) { Box(contentAlignment = Alignment.Center) { Text("✓", style = MaterialTheme.typography.headlineMedium) } }
            Text(uiModel.statusLabel, Modifier.padding(top = EumSpacing.x20), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text(uiModel.statusDescription, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textSecondary, textAlign = TextAlign.Center)
            OrderInfoCard(Modifier.fillMaxWidth().padding(top = EumSpacing.x24)) { SummaryLine(stringResource(R.string.request_number), uiModel.requestNumber) }
        }
    }
}

@Composable
fun OrderRequestWithdrawalConfirmScreen(requestNumber: String, statusLabel: String, onEvent: (OrderRequestWithdrawalScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_withdraw), { onEvent(OrderRequestWithdrawalScreenEvent.BackClicked) }, modifier, bottomContent = {
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            OutlinedButton(onClick = { onEvent(OrderRequestWithdrawalScreenEvent.WithdrawalCancelled) }, modifier = Modifier.weight(1f).heightIn(min = 52.dp)) { Text(stringResource(R.string.request_cancel_action)) }
            Button(onClick = { onEvent(OrderRequestWithdrawalScreenEvent.WithdrawalConfirmed) }, modifier = Modifier.weight(1f).heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_withdraw_action)) }
        }
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            Text(stringResource(R.string.request_withdraw_question), style = MaterialTheme.typography.titleLarge)
            Text(stringResource(R.string.request_withdraw_notice), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textSecondary)
            OrderInfoCard(Modifier.fillMaxWidth()) {
                SummaryLine(stringResource(R.string.request_status), statusLabel)
                SummaryLine(stringResource(R.string.request_number), requestNumber)
            }
        }
    }
}

@Composable
fun OrderRequestWithdrawalCompleteScreen(uiModel: OrderRequestResultUiModel, onEvent: (OrderRequestWithdrawalScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_withdraw), { onEvent(OrderRequestWithdrawalScreenEvent.BackClicked) }, modifier, bottomContent = { Button(onClick = { onEvent(OrderRequestWithdrawalScreenEvent.RequestHistoryClicked) }, Modifier.fillMaxWidth().heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_view_history)) } }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(EumSpacing.x24), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(stringResource(R.string.request_withdraw_complete), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text(uiModel.statusDescription, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.eumColors.textSecondary, textAlign = TextAlign.Center)
            OrderInfoCard(Modifier.fillMaxWidth().padding(top = EumSpacing.x24)) {
                SummaryLine(stringResource(R.string.request_status), uiModel.statusLabel)
                SummaryLine(stringResource(R.string.request_number), uiModel.requestNumber)
            }
        }
    }
}

@Composable
fun OrderRequestRejectionScreen(uiModel: OrderRequestRejectionUiModel, onEvent: (OrderRequestRejectionScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_rejected_screen_title), { onEvent(OrderRequestRejectionScreenEvent.BackClicked) }, modifier, bottomContent = { Button(onClick = { onEvent(OrderRequestRejectionScreenEvent.RequestHistoryClicked) }, Modifier.fillMaxWidth().heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_view_history)) } }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            item {
                OrderInfoCard(Modifier.fillMaxWidth()) {
                    StatusLabel(OrderRequestStatus.Rejected)
                    Text(uiModel.statusDescription, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
            item { DetailCard(stringResource(R.string.request_product_title), uiModel.productSummaryLabel) }
            item { DetailCard(stringResource(R.string.request_reason_title), uiModel.requestReasonLabel) }
            item { DetailCard(stringResource(R.string.request_rejected_screen_title), uiModel.rejectionReasonLabel) }
            item { OrderInfoCard(Modifier.fillMaxWidth()) { SummaryLine(stringResource(R.string.request_number), uiModel.requestNumber) } }
        }
    }
}
