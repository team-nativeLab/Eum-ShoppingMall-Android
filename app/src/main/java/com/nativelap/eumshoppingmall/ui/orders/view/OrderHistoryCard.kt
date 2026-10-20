package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderProductThumbnail
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderStatusBadge
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryAction
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderHistoryCard(
    order: OrderHistoryItemUiModel,
    onDetailsClicked: () -> Unit,
    onActionClicked: (OrderHistoryAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        Column(
            modifier = Modifier.padding(EumSpacing.x16),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(order.orderDateLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                Text(
                    text = stringResource(R.string.orders_order_number_short, order.orderNumber),
                    modifier = Modifier.weight(1f).padding(start = EumSpacing.x8),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                TextButton(onClick = onDetailsClicked, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(stringResource(R.string.orders_detail), maxLines = 1)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onDetailsClicked),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                verticalAlignment = Alignment.Top,
            ) {
                OrderProductThumbnail(order.product.imageResId, order.product.name)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OrderStatusBadge(order.status)
                        order.paymentDeadlineLabel?.let { deadline ->
                            Text(
                                deadline,
                                modifier = Modifier.padding(start = EumSpacing.x6),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.eumColors.warning,
                            )
                        }
                    }
                    Text(order.product.name, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(order.product.priceAndQuantityLabel, style = MaterialTheme.typography.titleSmall)
                    order.product.productCountLabel?.let { countLabel ->
                        Text(countLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                    }
                }
            }
            if (order.actions.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    order.actions.forEach { action ->
                        OutlinedButton(
                            onClick = { onActionClicked(action) },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                        ) {
                            Text(stringResource(action.labelResource()), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

@StringRes
private fun OrderHistoryAction.labelResource(): Int = when (this) {
    OrderHistoryAction.Cancel -> R.string.orders_cancel_order
    OrderHistoryAction.TrackDelivery -> R.string.orders_track_delivery
    OrderHistoryAction.BuyAgain -> R.string.orders_buy_again
}
