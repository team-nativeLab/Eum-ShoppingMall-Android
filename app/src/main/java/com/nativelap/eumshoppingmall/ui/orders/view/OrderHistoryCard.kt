package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderProductThumbnail
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderStatusBadge
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryAction
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
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
                Text(
                    order.orderDateLabel,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.eumColors.textPrimary,
                )
                Text(
                    text = stringResource(R.string.orders_order_number_short, order.orderNumber),
                    modifier = Modifier.weight(1f).padding(start = EumSpacing.x8),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                TextButton(
                    onClick = onDetailsClicked,
                    modifier = Modifier.heightIn(min = 48.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
                ) {
                    Text(stringResource(R.string.orders_detail), maxLines = 1)
                    Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onDetailsClicked),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                verticalAlignment = Alignment.Top,
            ) {
                OrderProductThumbnail(order.product.imageResId, order.product.name)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                    OrderStatusBadge(order.status)
                    Text(order.product.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(
                        order.product.priceAndQuantityLabel,
                        style = EumTypographyTokens.priceM,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                    order.product.productCountLabel?.let { countLabel ->
                        Text(countLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                    }
                    order.paymentDeadlineLabel?.let { deadline ->
                        Text(deadline, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                    }
                }
            }
            if (order.actions.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    order.actions.forEach { action ->
                        OrderHistoryActionButton(
                            label = stringResource(action.labelResource()),
                            onClick = { onActionClicked(action) },
                            modifier = Modifier.weight(1f),
                        )
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

// M3 Button은 내부 Row에 최소 높이(ButtonDefaults.MinHeight, 40dp)를 강제해 36dp를 만들 수 없다.
// 그래서 Surface로 36dp 시각 박스를 직접 그리고, 바깥에서 터치 영역을 48dp로 확보한다.
@Composable
private fun OrderHistoryActionButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.minimumInteractiveComponentSize(), contentAlignment = Alignment.Center) {
        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().heightIn(min = 36.dp).semantics { role = Role.Button },
            shape = RoundedCornerShape(EumRadius.x8),
            color = Color.Transparent,
            contentColor = MaterialTheme.eumColors.textPrimary,
            border = BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
        ) {
            Box(Modifier.padding(horizontal = EumSpacing.x12), contentAlignment = Alignment.Center) {
                Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
