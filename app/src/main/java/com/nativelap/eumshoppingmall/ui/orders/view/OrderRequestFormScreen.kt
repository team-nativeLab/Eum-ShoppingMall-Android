package com.nativelap.eumshoppingmall.ui.orders.view

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
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderInfoCard
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestCollectionMethod
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderRequestFormScreen(uiModel: OrderRequestFormUiModel, onEvent: (OrderRequestFormScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    RequestScaffold(stringResource(R.string.request_request_type_format, requestTypeLabel(uiModel.type)), { onEvent(OrderRequestFormScreenEvent.BackClicked) }, modifier, bottomContent = {
        Button(onClick = { onEvent(OrderRequestFormScreenEvent.SubmitClicked) }, enabled = uiModel.canSubmit, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_request_type_format, requestTypeLabel(uiModel.type))) }
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) { OrderRequestType.entries.forEach { type -> FilterPill(requestTypeLabel(type), uiModel.type == type) { onEvent(OrderRequestFormScreenEvent.TypeSelected(type)) } } } }
            item { SectionTitle(stringResource(R.string.request_order_products)) }
            items(uiModel.products, key = { it.id }) { product -> SelectableRequestProduct(product, { onEvent(OrderRequestFormScreenEvent.ProductSelectionChanged(product.id, !product.isSelected)) }, { onEvent(OrderRequestFormScreenEvent.ProductQuantityChanged(product.id, it)) }) }
            item { SectionTitle(stringResource(R.string.request_reason_title)) }
            item { Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) { uiModel.reasons.chunked(3).forEach { group -> Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) { group.forEach { reason -> FilterPill(reasonLabel(reason), uiModel.selectedReason == reason) { onEvent(OrderRequestFormScreenEvent.ReasonSelected(reason)) } } } } } }
            item { TextField(value = uiModel.reasonDetails, onValueChange = { onEvent(OrderRequestFormScreenEvent.ReasonDetailsChanged(it.take(500))) }, modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp), placeholder = { Text(stringResource(R.string.request_form_reason_placeholder)) }, colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.eumColors.surface, focusedContainerColor = MaterialTheme.eumColors.surface)) }
            item { OutlinedButton(onClick = { onEvent(OrderRequestFormScreenEvent.PhotoAttachmentClicked) }, enabled = uiModel.photoCount < uiModel.maxPhotoCount, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(stringResource(R.string.request_photo_count, uiModel.photoCount, uiModel.maxPhotoCount)) } }
            if (uiModel.type != OrderRequestType.Cancel) item { SectionTitle(stringResource(R.string.request_collection_method)) }
            if (uiModel.type != OrderRequestType.Cancel) item { Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) { OrderRequestCollectionMethod.entries.forEach { method -> FilterPill(if (method == OrderRequestCollectionMethod.CourierPickup) stringResource(R.string.request_courier_pickup) else stringResource(R.string.request_self_shipment), uiModel.collectionMethod == method) { onEvent(OrderRequestFormScreenEvent.CollectionMethodSelected(method)) } } } }
            if (uiModel.type == OrderRequestType.Exchange) {
                item {
                    OrderInfoCard(Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.request_exchange_option), style = MaterialTheme.typography.labelMedium)
                        TextButton(onClick = { onEvent(OrderRequestFormScreenEvent.ExchangeOptionClicked) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), contentPadding = PaddingValues(0.dp)) { Text(if (uiModel.exchangeOptionLabel.isBlank()) stringResource(R.string.request_exchange_option_placeholder) else uiModel.exchangeOptionLabel, Modifier.weight(1f), color = MaterialTheme.eumColors.textPrimary) }
                    }
                }
            }
            if (uiModel.type != OrderRequestType.Cancel) {
                item {
                    OrderInfoCard(Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.request_collection_address), style = MaterialTheme.typography.labelMedium)
                        Text(uiModel.collectionAddressLabel, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall)
                        Text(uiModel.collectionContactLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        TextButton(onClick = { onEvent(OrderRequestFormScreenEvent.CollectionAddressChangeClicked) }, modifier = Modifier.heightIn(min = 48.dp)) { Text(stringResource(R.string.request_change_collection_address)) }
                    }
                }
            }
            item {
                OrderInfoCard(Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.request_refund_fee_guide), style = MaterialTheme.typography.labelMedium)
                    SummaryLine(stringResource(R.string.request_product_amount), uiModel.productAmountLabel)
                    SummaryLine(stringResource(R.string.request_shipping_fee), uiModel.feeLabel)
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                    SummaryLine(if (uiModel.type == OrderRequestType.Exchange) stringResource(R.string.request_exchange_shipping_fee) else stringResource(R.string.request_expected_refund), uiModel.expectedAmountLabel)
                    SummaryLine(stringResource(R.string.request_payment_method), uiModel.paymentMethodLabel)
                }
            }
        }
    }
}
