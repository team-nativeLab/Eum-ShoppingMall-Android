package com.nativelap.eumshoppingmall.ui.orders.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderStatus
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderStatusBadge(status: OrderStatus, modifier: Modifier = Modifier) {
    val (labelResId, background, foreground) = when (status) {
        OrderStatus.WaitingPayment -> Triple(R.string.orders_status_waiting_payment, MaterialTheme.eumColors.warningTint, MaterialTheme.eumColors.warning)
        OrderStatus.PaymentComplete -> Triple(R.string.orders_status_paid, MaterialTheme.eumColors.warningTint, MaterialTheme.eumColors.warning)
        OrderStatus.Preparing -> Triple(R.string.orders_status_preparing, MaterialTheme.eumColors.surfaceSecondary, MaterialTheme.eumColors.textSecondary)
        OrderStatus.Shipping -> Triple(R.string.orders_status_in_transit, MaterialTheme.eumColors.surfaceSecondary, MaterialTheme.eumColors.textSecondary)
        OrderStatus.Delivered -> Triple(R.string.orders_status_delivered, MaterialTheme.eumColors.successTint, MaterialTheme.eumColors.success)
    }
    val isOutlined = status == OrderStatus.Preparing || status == OrderStatus.Shipping
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(EumRadius.x4),
        color = if (isOutlined) MaterialTheme.eumColors.surface else background,
        border = if (isOutlined) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong) else null,
    ) {
        Text(
            text = stringResource(labelResId),
            modifier = Modifier.padding(horizontal = EumSpacing.x6, vertical = EumSpacing.x2),
            style = MaterialTheme.typography.labelSmall,
            color = foreground,
        )
    }
}
