package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoCard
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrdersTopBar
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestHistoryFilter
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestReason
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestStatus
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun RequestScaffold(title: String, onBack: () -> Unit, modifier: Modifier = Modifier, bottomContent: (@Composable () -> Unit)? = null, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(modifier.fillMaxSize(), topBar = { OrdersTopBar(title, onBackClick = onBack, modifier = Modifier.statusBarsPadding().background(MaterialTheme.eumColors.background)) }, bottomBar = { if (bottomContent != null) Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface).navigationBarsPadding().imePadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12)) { bottomContent() } }, containerColor = MaterialTheme.eumColors.background, contentWindowInsets = WindowInsets(0, 0, 0, 0), content = content)
}

@Composable
internal fun HubActionCard(title: String, description: String, onClick: () -> Unit) {
    OrderInfoCard(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        Text(description, Modifier.padding(top = EumSpacing.x4), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text("›", Modifier.align(Alignment.End), style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
internal fun FilterPill(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(Modifier.heightIn(min = 48.dp).clickable(role = Role.RadioButton, onClick = onClick).semantics { selected = isSelected }, shape = RoundedCornerShape(50), color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface, border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong)) {
        Box(Modifier.padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8), contentAlignment = Alignment.Center) { Text(label, style = MaterialTheme.typography.labelSmall, color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary) }
    }
}

@Composable
internal fun SelectableRequestProduct(product: OrderRequestProductUiModel, onToggle: () -> Unit, onQuantity: (Int) -> Unit) {
    OrderInfoCard(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(48.dp).clickable(enabled = product.isSelectable, role = Role.Checkbox, onClick = onToggle).semantics { selected = product.isSelected }, shape = CircleShape, color = if (product.isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface, border = BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong)) { Box(contentAlignment = Alignment.Center) { if (product.isSelected) Text("✓", color = MaterialTheme.eumColors.inverse, style = MaterialTheme.typography.labelSmall) } }
            Column(Modifier.weight(1f).padding(start = EumSpacing.x12)) {
                Text(product.name, style = MaterialTheme.typography.bodyMedium)
                Text(product.optionLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                Text(product.priceAndQuantityLabel, style = MaterialTheme.typography.labelSmall)
            }
        }
        if (product.isSelected) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onQuantity(-1) }, modifier = Modifier.size(48.dp), enabled = product.isSelectable) { Text("−") }
                Text(product.quantityLabel, style = MaterialTheme.typography.labelMedium)
                TextButton(onClick = { onQuantity(1) }, modifier = Modifier.size(48.dp), enabled = product.isSelectable) { Text("+") }
            }
        }
    }
}

@Composable
internal fun RequestProductRow(product: OrderRequestProductUiModel) {
    Column(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x4), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
        Text(product.name, style = MaterialTheme.typography.bodyMedium)
        Text(product.optionLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(product.priceAndQuantityLabel, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
internal fun EmptyRequestState(title: String, description: String, modifier: Modifier = Modifier, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x12), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            Surface(shape = CircleShape, color = MaterialTheme.eumColors.surfaceSecondary, modifier = Modifier.size(64.dp)) { Box(contentAlignment = Alignment.Center) { Text("…", color = MaterialTheme.eumColors.textTertiary) } }
            Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary, textAlign = TextAlign.Center)
            if (actionLabel != null && onAction != null) Button(onClick = onAction, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), colors = darkButtonColors()) { Text(actionLabel) }
        }
    }
}

@Composable
internal fun SectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall)
}

@Composable
internal fun DetailCard(title: String, description: String) {
    OrderInfoCard(Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.labelMedium)
        Text(description, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
internal fun SummaryLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x4), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(value, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.End)
    }
}

@Composable
internal fun StatusLabel(status: OrderRequestStatus) {
    val label = when (status) {
        OrderRequestStatus.Requested -> stringResource(R.string.request_status_requested)
        OrderRequestStatus.Collecting -> stringResource(R.string.request_status_collecting)
        OrderRequestStatus.Processing -> stringResource(R.string.request_status_processing)
        OrderRequestStatus.Completed -> stringResource(R.string.request_status_completed)
        OrderRequestStatus.Rejected -> stringResource(R.string.request_status_rejected)
        OrderRequestStatus.Withdrawn -> stringResource(R.string.request_status_withdrawn)
    }
    Surface(shape = RoundedCornerShape(EumRadius.x8), color = MaterialTheme.eumColors.surfaceSecondary) { Text(label, Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4), style = MaterialTheme.typography.labelSmall) }
}

@Composable
internal fun darkButtonColors() = ButtonDefaults.buttonColors(containerColor = MaterialTheme.eumColors.textPrimary, contentColor = MaterialTheme.eumColors.inverse, disabledContainerColor = MaterialTheme.eumColors.surfaceSecondary, disabledContentColor = MaterialTheme.eumColors.textTertiary)

@Composable
internal fun orderFilterLabel(filter: OrderHistoryFilter): String = when (filter) {
    OrderHistoryFilter.All -> stringResource(R.string.request_period_all)
    OrderHistoryFilter.WaitingPayment -> stringResource(R.string.request_period_waiting)
    OrderHistoryFilter.PaymentComplete -> stringResource(R.string.request_period_paid)
    OrderHistoryFilter.Preparing -> stringResource(R.string.request_period_preparing)
    OrderHistoryFilter.Shipping -> stringResource(R.string.request_period_shipping)
    OrderHistoryFilter.Delivered -> stringResource(R.string.request_period_delivered)
}

@Composable
internal fun requestTypeLabel(type: OrderRequestType): String = when (type) {
    OrderRequestType.Cancel -> stringResource(R.string.request_history_filter_cancel)
    OrderRequestType.Return -> stringResource(R.string.request_history_filter_return)
    OrderRequestType.Exchange -> stringResource(R.string.request_history_filter_exchange)
}

@Composable
internal fun historyFilterLabel(filter: OrderRequestHistoryFilter): String = when (filter) {
    OrderRequestHistoryFilter.All -> stringResource(R.string.request_history_filter_all)
    OrderRequestHistoryFilter.Cancel -> stringResource(R.string.request_history_filter_cancel)
    OrderRequestHistoryFilter.Return -> stringResource(R.string.request_history_filter_return)
    OrderRequestHistoryFilter.Exchange -> stringResource(R.string.request_history_filter_exchange)
}

@Composable
internal fun reasonLabel(reason: OrderRequestReason): String = when (reason) {
    OrderRequestReason.ChangedMind -> stringResource(R.string.request_reason_changed_mind)
    OrderRequestReason.ProductDefective -> stringResource(R.string.request_reason_defective)
    OrderRequestReason.WrongProduct -> stringResource(R.string.request_reason_wrong)
    OrderRequestReason.DeliveryDelayed -> stringResource(R.string.request_reason_delayed)
    OrderRequestReason.Other -> stringResource(R.string.request_reason_other)
}
