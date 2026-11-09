package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoCard
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderProductThumbnail
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestDetailUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderRequestHistoryScreen(uiModel: OrderRequestHistoryUiModel, onEvent: (OrderRequestHistoryScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_history_list_title), { onEvent(OrderRequestHistoryScreenEvent.BackClicked) }, modifier) { padding ->
        if (uiModel.hasNoRequests) {
            RequestHistoryEmptyContent(
                onOrdersClick = { onEvent(OrderRequestHistoryScreenEvent.OrdersClicked) },
                modifier = Modifier.fillMaxSize().padding(padding),
            )
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(vertical = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                item {
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        OrderRequestHistoryFilter.entries.forEach { filter ->
                            HistoryFilterPill(historyFilterLabel(filter), uiModel.filter == filter) { onEvent(OrderRequestHistoryScreenEvent.FilterSelected(filter)) }
                        }
                    }
                }
                items(uiModel.items, key = { it.id }) { item ->
                    HistoryRequestCard(item, onEvent)
                }
                if (uiModel.items.isEmpty()) item { EmptyRequestState(stringResource(R.string.request_no_requests_title), stringResource(R.string.request_no_matching_requests), Modifier.fillMaxWidth().padding(EumSpacing.x32)) }
            }
        }
    }
}

@Composable
private fun RequestHistoryEmptyContent(onOrdersClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.padding(horizontal = EumSpacing.x20),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(shape = CircleShape, color = MaterialTheme.eumColors.surfaceSecondary, modifier = Modifier.size(64.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_message_square_text),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textSecondary,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
        Text(
            stringResource(R.string.request_no_requests_title),
            Modifier.padding(top = EumSpacing.x20),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
        )
        Text(
            stringResource(R.string.request_history_empty_description),
            Modifier.padding(top = EumSpacing.x8),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textSecondary,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onOrdersClick,
            modifier = Modifier.fillMaxWidth().padding(top = 64.dp).heightIn(min = 48.dp),
            shape = RoundedCornerShape(EumRadius.x8),
            colors = darkButtonColors(),
        ) { Text(stringResource(R.string.request_orders_title)) }
    }
}

@Composable
private fun HistoryFilterPill(label: String, isSelected: Boolean, onClick: () -> Unit) {
    // Figma 필은 높이 34dp이고, 터치 영역은 48dp를 유지하려고 바깥 Box에 둔다.
    Box(
        Modifier.heightIn(min = 48.dp).clickable(role = Role.RadioButton, onClick = onClick).semantics { selected = isSelected },
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
            border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
        ) {
            Box(Modifier.height(34.dp).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) {
                Text(label, style = MaterialTheme.typography.labelMedium, color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary)
            }
        }
    }
}

@Composable
private fun HistoryRequestCard(item: OrderRequestHistoryItemUiModel, onEvent: (OrderRequestHistoryScreenEvent) -> Unit) {
    OrderInfoCard(Modifier.padding(horizontal = EumSpacing.x20)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(item.requestDateLabel, style = MaterialTheme.typography.titleSmall)
            Text(
                stringResource(R.string.request_history_meta_format, requestTypeLabel(item.requestType), item.orderNumber),
                Modifier.weight(1f).padding(start = EumSpacing.x8),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                Modifier.heightIn(min = 48.dp).clickable(role = Role.Button) { onEvent(OrderRequestHistoryScreenEvent.RequestDetailsClicked(item.id)) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.request_detail_screen_title), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textPrimary)
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        Row(Modifier.fillMaxWidth().padding(top = EumSpacing.x12), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            OrderProductThumbnail(imageResId = item.imageResId, contentDescription = item.productName, size = 64.dp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                HistoryListStatusChip(item.requestType, item.status)
                Text(item.productName, style = MaterialTheme.typography.bodyMedium)
                Text(item.priceAndQuantityLabel, style = MaterialTheme.typography.labelLarge)
            }
        }
        item.expectedAmountLabel?.let { amount ->
            Row(Modifier.fillMaxWidth().padding(top = EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.request_refund_expected_label), Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                Text(amount, style = MaterialTheme.typography.labelMedium)
            }
        }
        if (item.hasRejectionReason) {
            OutlinedButton(
                onClick = { onEvent(OrderRequestHistoryScreenEvent.RejectionReasonClicked(item.id)) },
                modifier = Modifier.fillMaxWidth().padding(top = EumSpacing.x12).heightIn(min = 48.dp),
                shape = RoundedCornerShape(EumRadius.x8),
                border = BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
            ) { Text(stringResource(R.string.request_reject_reason_view_action), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary) }
        }
        if (item.canWithdraw) {
            TextButton(
                onClick = { onEvent(OrderRequestHistoryScreenEvent.WithdrawalClicked(item.id)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) { Text(stringResource(R.string.request_withdraw), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary) }
        }
    }
}

@Composable
private fun HistoryListStatusChip(type: OrderRequestType, status: OrderRequestStatus) {
    val isOutlined = status == OrderRequestStatus.Collecting || status == OrderRequestStatus.Withdrawn
    val (labelResId, background, foreground) = when (status) {
        OrderRequestStatus.Withdrawn -> Triple(R.string.request_status_withdrawn_completed, MaterialTheme.eumColors.surface, MaterialTheme.eumColors.textPrimary)

        OrderRequestStatus.Collecting -> Triple(R.string.request_status_collecting_short, MaterialTheme.eumColors.surface, MaterialTheme.eumColors.textPrimary)

        OrderRequestStatus.Completed -> Triple(
            if (type == OrderRequestType.Exchange) R.string.request_status_exchange_completed else R.string.request_status_completed,
            MaterialTheme.eumColors.successTint,
            MaterialTheme.eumColors.success,
        )

        OrderRequestStatus.Rejected -> Triple(R.string.request_status_rejected, MaterialTheme.eumColors.dangerTint, MaterialTheme.eumColors.danger)

        OrderRequestStatus.Requested -> if (type == OrderRequestType.Return) {
            Triple(R.string.request_status_return_requested, MaterialTheme.eumColors.dangerTint, MaterialTheme.eumColors.danger)
        } else {
            Triple(R.string.request_status_requested, MaterialTheme.eumColors.surfaceSecondary, MaterialTheme.eumColors.textSecondary)
        }

        OrderRequestStatus.Processing -> Triple(R.string.request_status_processing, MaterialTheme.eumColors.surfaceSecondary, MaterialTheme.eumColors.textSecondary)
    }
    Surface(
        shape = RoundedCornerShape(EumRadius.x4),
        color = background,
        border = if (isOutlined) BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong) else null,
    ) {
        Text(
            stringResource(labelResId),
            Modifier.padding(horizontal = EumSpacing.x6, vertical = EumSpacing.x2),
            style = MaterialTheme.typography.labelSmall,
            color = foreground,
        )
    }
}

@Composable
fun OrderRequestDetailScreen(uiModel: OrderRequestDetailUiModel, onEvent: (OrderRequestDetailScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_detail_screen_title), { onEvent(OrderRequestDetailScreenEvent.BackClicked) }, modifier, bottomContent = {
        if (uiModel.canWithdraw && uiModel.rejectionReasonLabel != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                OutlinedButton(onClick = { onEvent(OrderRequestDetailScreenEvent.WithdrawalClicked) }, modifier = Modifier.weight(1f).heightIn(min = 52.dp), shape = RoundedCornerShape(EumRadius.x8)) { Text(stringResource(R.string.request_withdraw)) }
                Button(onClick = { onEvent(OrderRequestDetailScreenEvent.RejectionReasonClicked) }, modifier = Modifier.weight(1f).heightIn(min = 52.dp), shape = RoundedCornerShape(EumRadius.x8), colors = darkButtonColors()) { Text(stringResource(R.string.request_rejected_screen_title)) }
            }
        } else if (uiModel.canWithdraw) {
            Button(onClick = { onEvent(OrderRequestDetailScreenEvent.WithdrawalClicked) }, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp), shape = RoundedCornerShape(EumRadius.x8), colors = darkButtonColors()) { Text(stringResource(R.string.request_withdraw)) }
        } else if (uiModel.rejectionReasonLabel != null) {
            Button(onClick = { onEvent(OrderRequestDetailScreenEvent.RejectionReasonClicked) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(EumRadius.x8), colors = darkButtonColors()) { Text(stringResource(R.string.request_rejected_screen_title)) }
        } else {
            Button(onClick = { onEvent(OrderRequestDetailScreenEvent.RequestHistoryClicked) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(EumRadius.x8), colors = darkButtonColors()) { Text(stringResource(R.string.request_view_history)) }
        }
    }) { padding ->
        if (uiModel.canWithdraw) {
            ActiveRequestDetailContent(uiModel, Modifier.fillMaxSize().padding(padding))
        } else {
            ClosedRequestDetailContent(uiModel, Modifier.fillMaxSize().padding(padding))
        }
    }
}

/** 신청 접수 상태(철회 가능)의 상세. Figma 879:44641. */
@Composable
private fun ActiveRequestDetailContent(uiModel: OrderRequestDetailUiModel, modifier: Modifier) {
    val statusTint = if (uiModel.status == OrderRequestStatus.Requested) MaterialTheme.eumColors.success else MaterialTheme.eumColors.textPrimary
    LazyColumn(modifier, contentPadding = PaddingValues(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        item {
            Text(
                stringResource(R.string.request_request_type_format, requestTypeLabel(uiModel.requestType)),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
        }
        item {
            OrderInfoCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_circle_check), contentDescription = null, tint = statusTint, modifier = Modifier.size(20.dp))
                    Text(stringResource(requestStatusTitleRes(uiModel.status)), Modifier.padding(start = EumSpacing.x8), style = MaterialTheme.typography.titleSmall, color = statusTint)
                }
                Text(uiModel.statusDescription, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            }
        }
        item {
            OrderInfoCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_package), contentDescription = null, tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
                    Column(Modifier.padding(start = EumSpacing.x12)) {
                        Text(stringResource(R.string.request_product_title), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        Text(uiModel.productSummaryLabel, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
        item {
            OrderInfoCard(Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.request_info_section_title), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Column(Modifier.padding(top = EumSpacing.x8)) {
                    RequestInfoRow(R.drawable.ic_calendar, stringResource(R.string.request_date_label), uiModel.requestDateLabel)
                    RequestInfoRow(R.drawable.ic_file_text, stringResource(R.string.request_reason_title), uiModel.reasonSummaryLabel)
                    RequestInfoRow(R.drawable.ic_map_pin, stringResource(R.string.request_collection_address_label), uiModel.collectionAddressLabel)
                    RequestInfoRow(R.drawable.ic_receipt, stringResource(R.string.request_number), uiModel.requestNumber)
                }
            }
        }
    }
}

/** 철회·종료된 신청의 상세. Figma 879:45279. */
@Composable
private fun ClosedRequestDetailContent(uiModel: OrderRequestDetailUiModel, modifier: Modifier) {
    LazyColumn(modifier, contentPadding = PaddingValues(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        item {
            Text(
                stringResource(
                    R.string.request_detail_summary_format,
                    uiModel.requestDateLabel,
                    stringResource(R.string.request_request_type_format, requestTypeLabel(uiModel.requestType)),
                    stringResource(requestStatusTitleRes(uiModel.status)),
                ),
                Modifier.padding(bottom = EumSpacing.x24),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textSecondary,
            )
        }
        item { RequestIconInfoCard(R.drawable.ic_circle_check, stringResource(R.string.request_progress_status_label), uiModel.statusDescription) }
        item { RequestIconInfoCard(R.drawable.ic_package, stringResource(R.string.request_product_title), uiModel.productSummaryLabel) }
        item { RequestIconInfoCard(R.drawable.ic_message_square_text, stringResource(R.string.request_reason_title), uiModel.reasonSummaryLabel) }
        item { RequestIconInfoCard(R.drawable.ic_map_pin, stringResource(R.string.request_collection_address_label), uiModel.collectionAddressLabel) }
        item { RequestIconInfoCard(R.drawable.ic_file_text, stringResource(R.string.request_number), uiModel.requestNumber) }
    }
}

/** 상세 화면 상단 상태 문구. 목록 칩 문구와 달리 Figma 상세 표기를 따른다. */
internal fun requestStatusTitleRes(status: OrderRequestStatus): Int = when (status) {
    OrderRequestStatus.Requested -> R.string.request_status_requested
    OrderRequestStatus.Collecting -> R.string.request_status_collecting
    OrderRequestStatus.Processing -> R.string.request_status_processing
    OrderRequestStatus.Completed -> R.string.request_status_completed
    OrderRequestStatus.Rejected -> R.string.request_status_rejected
    OrderRequestStatus.Withdrawn -> R.string.request_status_withdrawn_done
}
