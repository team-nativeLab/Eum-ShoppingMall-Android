package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoCard
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoRow
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderPaymentBreakdownCard
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrdersTopBar
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderDetailScreen(
    uiModel: OrderDetailUiModel,
    onEvent: (OrderDetailScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            OrdersTopBar(
                title = stringResource(R.string.orders_detail),
                onBackClick = { onEvent(OrderDetailScreenEvent.BackClicked) },
                modifier = Modifier.statusBarsPadding().background(MaterialTheme.eumColors.background),
            )
        },
        bottomBar = {
            Row(
                Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface).navigationBarsPadding()
                    .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                Button(
                    onClick = { onEvent(OrderDetailScreenEvent.DeliveryTrackingClicked) },
                    enabled = uiModel.canTrackDelivery,
                    modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.eumColors.textPrimary,
                        contentColor = MaterialTheme.eumColors.inverse,
                        disabledContainerColor = MaterialTheme.eumColors.surfaceSecondary,
                        disabledContentColor = MaterialTheme.eumColors.textTertiary,
                    ),
                ) {
                    Text(stringResource(R.string.orders_track_delivery))
                }
                OutlinedButton(
                    onClick = { onEvent(OrderDetailScreenEvent.ExchangeReturnRequestClicked) },
                    enabled = uiModel.canRequestExchangeOrReturn,
                    modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                ) {
                    Text(stringResource(R.string.orders_exchange_return_request))
                }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                    Text(stringResource(R.string.orders_order_date_title, uiModel.orderDateLabel), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.orders_order_number_short, uiModel.orderNumber), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
            }
            item {
                OrderInfoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.orders_products_title), modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
                        TextButton(onClick = { onEvent(OrderDetailScreenEvent.ProductsExpandedChanged(!uiModel.isProductsExpanded)) }) {
                            Text(stringResource(if (uiModel.isProductsExpanded) R.string.common_fold else R.string.common_expand))
                            Icon(
                                painterResource(if (uiModel.isProductsExpanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                                contentDescription = null,
                                tint = MaterialTheme.eumColors.textPrimary,
                            )
                        }
                    }
                    if (uiModel.isProductsExpanded) {
                        uiModel.products.forEachIndexed { index, product ->
                            if (index > 0) HorizontalDivider(color = MaterialTheme.eumColors.line)
                            OrderDetailProductCard(product)
                        }
                    }
                }
            }
            item {
                OrderInfoCard {
                    Text(stringResource(R.string.checkout_delivery_address), style = MaterialTheme.typography.labelMedium)
                    OrderInfoRow(stringResource(R.string.checkout_recipient), uiModel.address.recipientName, Modifier.padding(top = EumSpacing.x12))
                    OrderInfoRow(stringResource(R.string.checkout_contact), uiModel.address.contactLabel, Modifier.padding(top = EumSpacing.x8))
                    OrderInfoRow(stringResource(R.string.orders_address), uiModel.address.addressLabel, Modifier.padding(top = EumSpacing.x8))
                    OrderInfoRow(stringResource(R.string.orders_delivery_request), uiModel.address.requestLabel, Modifier.padding(top = EumSpacing.x8))
                }
            }
            item { OrderPaymentBreakdownCard(uiModel.payment) }
            item {
                OrderInfoCard {
                    OrderInfoRow(stringResource(R.string.checkout_expected_points), uiModel.expectedPointsLabel)
                }
            }
            item {
                OrderInfoCard {
                    TextButton(
                        onClick = { onEvent(OrderDetailScreenEvent.StatementDownloadClicked) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    ) {
                        Icon(painterResource(R.drawable.ic_download), contentDescription = null)
                        Text(stringResource(R.string.orders_download_statement), modifier = Modifier.weight(1f).padding(start = EumSpacing.x8), color = MaterialTheme.eumColors.textPrimary)
                        Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, tint = MaterialTheme.eumColors.textPrimary)
                    }
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                    TextButton(
                        onClick = { onEvent(OrderDetailScreenEvent.ReceiptViewClicked) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    ) {
                        Icon(painterResource(R.drawable.ic_receipt), contentDescription = null)
                        Text(stringResource(R.string.orders_view_receipt), modifier = Modifier.weight(1f).padding(start = EumSpacing.x8), color = MaterialTheme.eumColors.textPrimary)
                        Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, tint = MaterialTheme.eumColors.textPrimary)
                    }
                }
            }
        }
    }
}
