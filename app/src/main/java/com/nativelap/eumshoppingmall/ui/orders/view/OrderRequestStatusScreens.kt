package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoCard
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestRejectionUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestResultUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestWithdrawalScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderRequestResultScreen(uiModel: OrderRequestResultUiModel, onEvent: (OrderRequestResultScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(uiModel.statusLabel, { onEvent(OrderRequestResultScreenEvent.BackClicked) }, modifier, bottomContent = {
        Button(onClick = { onEvent(OrderRequestResultScreenEvent.RequestHistoryClicked) }, Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(EumRadius.x8), colors = darkButtonColors()) { Text(stringResource(R.string.request_view_history)) }
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x24)) {
            Text(stringResource(R.string.request_result_description), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            RequestLabeledField(stringResource(R.string.request_result_number_label), uiModel.requestNumber)
            RequestLabeledField(stringResource(R.string.request_result_notice_label), uiModel.statusDescription)
        }
    }
}

/** 라벨 아래에 읽기 전용 입력칸 모양의 값 상자를 두는 행(Figma 879:8904). */
@Composable
private fun RequestLabeledField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(label, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        Surface(
            modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp),
            shape = RoundedCornerShape(EumRadius.x8),
            color = MaterialTheme.eumColors.surface,
            border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
        ) {
            Box(Modifier.fillMaxWidth().heightIn(min = 50.dp).padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12), contentAlignment = Alignment.CenterStart) {
                Text(value, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun OrderRequestWithdrawalConfirmScreen(requestNumber: String, onEvent: (OrderRequestWithdrawalScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_withdraw_confirm_title), { onEvent(OrderRequestWithdrawalScreenEvent.BackClicked) }, modifier, bottomContent = {
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            OutlinedButton(onClick = { onEvent(OrderRequestWithdrawalScreenEvent.WithdrawalCancelled) }, modifier = Modifier.weight(1f).heightIn(min = 56.dp), shape = RoundedCornerShape(EumRadius.x8), border = BorderStroke(1.dp, MaterialTheme.eumColors.textPrimary)) { Text(stringResource(R.string.request_cancel_action), color = MaterialTheme.eumColors.textPrimary) }
            Button(onClick = { onEvent(OrderRequestWithdrawalScreenEvent.WithdrawalConfirmed) }, modifier = Modifier.weight(1f).heightIn(min = 56.dp), shape = RoundedCornerShape(EumRadius.x8), colors = darkButtonColors()) { Text(stringResource(R.string.request_withdraw_action)) }
        }
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            Surface(shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.dangerTint, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(EumSpacing.x16)) {
                    Text(stringResource(R.string.request_withdraw_question), style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.eumColors.danger)
                    Text(stringResource(R.string.request_withdraw_warning_body), Modifier.padding(top = EumSpacing.x4), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
                }
            }
            OrderInfoCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_package), contentDescription = null, tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
                    Text(stringResource(R.string.request_withdraw_target_title), Modifier.padding(start = EumSpacing.x8), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                Column(Modifier.padding(top = EumSpacing.x8)) {
                    RequestInfoRow(R.drawable.ic_receipt, stringResource(R.string.request_number), requestNumber)
                }
            }
        }
    }
}

@Composable
fun OrderRequestWithdrawalCompleteScreen(uiModel: OrderRequestResultUiModel, onEvent: (OrderRequestWithdrawalScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_withdraw_complete_title), { onEvent(OrderRequestWithdrawalScreenEvent.BackClicked) }, modifier, bottomContent = {
        Button(onClick = { onEvent(OrderRequestWithdrawalScreenEvent.RequestHistoryClicked) }, Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(EumRadius.x8), colors = darkButtonColors()) { Text(stringResource(R.string.request_view_history)) }
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            Box(Modifier.fillMaxWidth().padding(top = EumSpacing.x24), contentAlignment = Alignment.Center) {
                Surface(shape = CircleShape, color = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(64.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(painterResource(R.drawable.ic_check), contentDescription = null, tint = MaterialTheme.eumColors.inverse, modifier = Modifier.size(28.dp))
                    }
                }
            }
            Text(stringResource(R.string.request_withdraw_done_headline), Modifier.padding(top = EumSpacing.x12), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text(uiModel.statusDescription, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            OrderInfoCard(Modifier.fillMaxWidth().padding(top = EumSpacing.x12)) {
                Text(stringResource(R.string.request_withdrawn_target_title), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Column(Modifier.padding(top = EumSpacing.x8)) {
                    RequestInfoRow(R.drawable.ic_receipt, stringResource(R.string.request_number), uiModel.requestNumber)
                }
            }
        }
    }
}

@Composable
fun OrderRequestRejectionScreen(uiModel: OrderRequestRejectionUiModel, onEvent: (OrderRequestRejectionScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_rejected_screen_title), { onEvent(OrderRequestRejectionScreenEvent.BackClicked) }, modifier, bottomContent = {
        Button(onClick = { onEvent(OrderRequestRejectionScreenEvent.RequestHistoryClicked) }, Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(EumRadius.x8), colors = darkButtonColors()) { Text(stringResource(R.string.request_view_history)) }
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            item {
                Text(stringResource(R.string.request_rejection_result_subtitle), Modifier.padding(bottom = EumSpacing.x24), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
            item { RequestIconInfoCard(R.drawable.ic_circle_alert, stringResource(R.string.request_progress_status_label), uiModel.statusDescription) }
            item { RequestIconInfoCard(R.drawable.ic_package, stringResource(R.string.request_product_title), uiModel.productSummaryLabel) }
            item { RequestIconInfoCard(R.drawable.ic_message_square_text, stringResource(R.string.request_rejected_screen_title), uiModel.rejectionReasonLabel) }
            item { RequestIconInfoCard(R.drawable.ic_file_text, stringResource(R.string.request_number), uiModel.requestNumber) }
        }
    }
}
