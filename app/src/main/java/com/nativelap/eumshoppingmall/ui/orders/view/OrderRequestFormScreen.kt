package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderProductThumbnail
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestCollectionMethod
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormScreenEvent
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestFormUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestProductUiModel
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestReason
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderRequestType
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private const val REASON_DETAILS_MAX_LENGTH = 500

@Composable
fun OrderRequestFormScreen(uiModel: OrderRequestFormUiModel, onEvent: (OrderRequestFormScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    val requestTypeName = requestTypeLabel(uiModel.type)
    RequestScaffold(stringResource(R.string.request_request_type_format, requestTypeName), { onEvent(OrderRequestFormScreenEvent.BackClicked) }, modifier, bottomContent = {
        Button(onClick = { onEvent(OrderRequestFormScreenEvent.SubmitClicked) }, enabled = uiModel.canSubmit, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), colors = darkButtonColors()) { Text(stringResource(R.string.request_submit_format, requestTypeName)) }
    }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x24),
        ) {
            item { RequestTypeSegmentedControl(uiModel.type) { onEvent(OrderRequestFormScreenEvent.TypeSelected(it)) } }
            item { ProductSelectionSection(uiModel.products, onEvent) }
            item { ReasonSection(uiModel, onEvent) }
            item { PhotoSection(uiModel, onEvent) }
            if (uiModel.type == OrderRequestType.Exchange) {
                item { ExchangeOptionCard(uiModel.exchangeOptionLabel) { onEvent(OrderRequestFormScreenEvent.ExchangeOptionClicked) } }
            }
            if (uiModel.type != OrderRequestType.Cancel) {
                item { CollectionSection(uiModel, onEvent) }
            }
            item { CostSection(uiModel) }
        }
    }
}

@Composable
private fun RequestTypeSegmentedControl(selectedType: OrderRequestType, onTypeSelected: (OrderRequestType) -> Unit) {
    Row(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.textPrimary, RoundedCornerShape(EumRadius.x8)).padding(EumSpacing.x4)) {
        OrderRequestType.entries.forEach { type ->
            val isSelected = type == selectedType
            Box(
                Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
                    .clip(RoundedCornerShape(EumRadius.x6))
                    .background(if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface)
                    .clickable(role = Role.RadioButton, onClick = { onTypeSelected(type) })
                    .semantics { selected = isSelected },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    requestTypeLabel(type),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary,
                )
            }
        }
    }
}

@Composable
private fun ProductSelectionSection(products: List<OrderRequestProductUiModel>, onEvent: (OrderRequestFormScreenEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        FormSectionTitle(stringResource(R.string.request_select_products_title))
        products.forEachIndexed { index, product ->
            if (index > 0) HorizontalDivider(color = MaterialTheme.eumColors.line)
            FormProductRow(
                product = product,
                onToggle = { onEvent(OrderRequestFormScreenEvent.ProductSelectionChanged(product.id, !product.isSelected)) },
                onQuantityChange = { onEvent(OrderRequestFormScreenEvent.ProductQuantityChanged(product.id, it)) },
            )
        }
    }
}

@Composable
private fun FormProductRow(product: OrderRequestProductUiModel, onToggle: () -> Unit, onQuantityChange: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
        ProductCheckbox(isChecked = product.isSelected, isEnabled = product.isSelectable, onToggle = onToggle)
        OrderProductThumbnail(imageResId = product.imageResId, contentDescription = product.name, size = 56.dp)
        Column(Modifier.weight(1f).padding(start = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(product.name, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(product.optionLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            if (product.isSelected) {
                QuantityStepper(
                    quantityLabel = product.quantityLabel,
                    isEnabled = product.isSelectable,
                    onQuantityChange = onQuantityChange,
                    modifier = Modifier.align(Alignment.End),
                )
            }
        }
    }
}

@Composable
private fun ProductCheckbox(isChecked: Boolean, isEnabled: Boolean, onToggle: () -> Unit) {
    Box(
        Modifier.size(48.dp).toggleable(value = isChecked, enabled = isEnabled, role = Role.Checkbox, onValueChange = { onToggle() }),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            Modifier.size(20.dp),
            shape = RoundedCornerShape(EumRadius.x4),
            color = if (isChecked) MaterialTheme.eumColors.checkboxChecked else MaterialTheme.eumColors.surface,
            border = if (isChecked) null else BorderStroke(1.dp, MaterialTheme.eumColors.lineStrong),
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isChecked) Text("✓", color = MaterialTheme.eumColors.inverse, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun QuantityStepper(quantityLabel: String, isEnabled: Boolean, onQuantityChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.border(1.dp, MaterialTheme.eumColors.lineStrong, RoundedCornerShape(EumRadius.x8)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val decreaseLabel = stringResource(R.string.catalog_quantity_decrease)
        val increaseLabel = stringResource(R.string.catalog_quantity_increase)
        TextButton(
            onClick = { onQuantityChange(-1) },
            enabled = isEnabled,
            modifier = Modifier.size(48.dp).semantics { contentDescription = decreaseLabel },
            contentPadding = PaddingValues(0.dp),
            colors = monochromeTextButtonColors(),
        ) { Text("−") }
        Text(quantityLabel, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = EumSpacing.x4))
        TextButton(
            onClick = { onQuantityChange(1) },
            enabled = isEnabled,
            modifier = Modifier.size(48.dp).semantics { contentDescription = increaseLabel },
            contentPadding = PaddingValues(0.dp),
            colors = monochromeTextButtonColors(),
        ) { Text("+") }
    }
}

@Composable
private fun ReasonSection(uiModel: OrderRequestFormUiModel, onEvent: (OrderRequestFormScreenEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        FormSectionTitle(stringResource(R.string.request_select_reason_title))
        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            uiModel.reasons.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    pair.forEach { reason ->
                        ReasonChoiceChip(
                            label = formReasonLabel(reason),
                            isSelected = uiModel.selectedReason == reason,
                            modifier = Modifier.weight(1f),
                            onClick = { onEvent(OrderRequestFormScreenEvent.ReasonSelected(reason)) },
                        )
                    }

                    if (pair.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
        OutlinedTextField(
            value = uiModel.reasonDetails,
            onValueChange = { onEvent(OrderRequestFormScreenEvent.ReasonDetailsChanged(it.take(REASON_DETAILS_MAX_LENGTH))) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp),
            placeholder = { Text(stringResource(R.string.request_reason_detail_placeholder)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.eumColors.surface,
                unfocusedContainerColor = MaterialTheme.eumColors.surface,
                focusedBorderColor = MaterialTheme.eumColors.textPrimary,
                unfocusedBorderColor = MaterialTheme.eumColors.lineStrong,
                cursorColor = MaterialTheme.eumColors.textPrimary,
            ),
        )
        Text(
            stringResource(R.string.request_count_fraction, uiModel.reasonDetails.length, REASON_DETAILS_MAX_LENGTH),
            Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textSecondary,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun formReasonLabel(reason: OrderRequestReason): String = when (reason) {
    OrderRequestReason.ProductDefective -> stringResource(R.string.request_reason_defective_damaged)
    else -> reasonLabel(reason)
}

@Composable
private fun ReasonChoiceChip(label: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val chipShape = RoundedCornerShape(EumRadius.x8)
    Box(
        modifier
            .heightIn(min = 48.dp)
            .clip(chipShape)
            .background(MaterialTheme.eumColors.surface)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.lineStrong,
                shape = chipShape,
            )
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { selected = isSelected }
            .padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.eumColors.textPrimary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PhotoSection(uiModel: OrderRequestFormUiModel, onEvent: (OrderRequestFormScreenEvent) -> Unit) {
    val dashColor = MaterialTheme.eumColors.lineStrong
    val density = LocalDensity.current
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        FormSectionTitle(stringResource(R.string.return_attach_photo_optional))
        Box(
            Modifier
                .size(56.dp)
                .drawBehind {
                    val strokeWidth = with(density) { 1.dp.toPx() }
                    drawRoundRect(
                        color = dashColor,
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth),
                        cornerRadius = CornerRadius(with(density) { 8.dp.toPx() }),
                        style = Stroke(width = strokeWidth, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))),
                    )
                }
                .clickable(enabled = uiModel.photoCount < uiModel.maxPhotoCount, role = Role.Button, onClick = { onEvent(OrderRequestFormScreenEvent.PhotoAttachmentClicked) }),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("+", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textSecondary)
                Text(
                    stringResource(R.string.request_count_fraction, uiModel.photoCount, uiModel.maxPhotoCount),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
            }
        }
        Text(stringResource(R.string.request_photo_guidance), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
    }
}

@Composable
private fun ExchangeOptionCard(optionLabel: String, onClick: () -> Unit) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.backgroundSubtle) {
        Column(Modifier.padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            Text(stringResource(R.string.request_exchange_option_title), style = MaterialTheme.typography.titleSmall)
            TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), contentPadding = PaddingValues(0.dp), colors = monochromeTextButtonColors()) {
                Text(stringResource(R.string.request_exchange_option_label), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)

                Spacer(Modifier.weight(1f))

                Text(
                    if (optionLabel.isBlank()) stringResource(R.string.request_exchange_option_pending) else optionLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textPrimary,
                )
            }
            HorizontalDivider(color = MaterialTheme.eumColors.line)
            Row(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x4), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                Text(stringResource(R.string.request_exchange_stock_label), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)

                Spacer(Modifier.weight(1f))

                Text(stringResource(R.string.request_exchange_stock_value), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.End)
            }
        }
    }
}

@Composable
private fun CollectionSection(uiModel: OrderRequestFormUiModel, onEvent: (OrderRequestFormScreenEvent) -> Unit) {
    val isExchange = uiModel.type == OrderRequestType.Exchange
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        FormSectionTitle(stringResource(if (isExchange) R.string.request_exchange_collection_title else R.string.return_collection_method))
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
            OrderRequestCollectionMethod.entries.forEach { method ->
                CollectionMethodRadio(
                    label = if (method == OrderRequestCollectionMethod.CourierPickup) stringResource(R.string.return_courier_collection) else stringResource(R.string.return_self_shipping),
                    isSelected = uiModel.collectionMethod == method,
                    onClick = { onEvent(OrderRequestFormScreenEvent.CollectionMethodSelected(method)) },
                )
            }
        }
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surface, border = BorderStroke(1.dp, MaterialTheme.eumColors.line)) {
            Row(Modifier.padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(uiModel.collectionContactLabel, style = MaterialTheme.typography.titleSmall)
                    Text(uiModel.collectionAddressLabel, Modifier.padding(top = EumSpacing.x4), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
                TextButton(onClick = { onEvent(OrderRequestFormScreenEvent.CollectionAddressChangeClicked) }, modifier = Modifier.heightIn(min = 48.dp), colors = monochromeTextButtonColors()) {
                    Text(stringResource(R.string.request_change_action))
                }
            }
        }
    }
}

@Composable
private fun CollectionMethodRadio(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .heightIn(min = 48.dp)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { selected = isSelected }
            .padding(end = EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.eumColors.textPrimary, unselectedColor = MaterialTheme.eumColors.lineStrong),
        )
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun CostSection(uiModel: OrderRequestFormUiModel) {
    val isExchange = uiModel.type == OrderRequestType.Exchange
    val feeLabelRes = when (uiModel.type) {
        OrderRequestType.Return -> R.string.request_return_fee_label
        OrderRequestType.Cancel -> R.string.request_cancel_fee_label
        OrderRequestType.Exchange -> R.string.request_exchange_shipping_fee
    }
    val expectedLabelRes = if (isExchange) R.string.request_exchange_additional_fee_label else R.string.return_expected_refund
    val lastLabelRes = if (isExchange) R.string.request_payment_method else R.string.return_refund_method
    val noticeRes = when (uiModel.type) {
        OrderRequestType.Return -> R.string.request_return_cost_notice
        OrderRequestType.Cancel -> R.string.request_cancel_refund_notice
        OrderRequestType.Exchange -> R.string.request_exchange_cost_notice
    }
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        FormSectionTitle(stringResource(if (isExchange) R.string.request_exchange_cost_section else R.string.return_expected_refund))
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(EumRadius.x12), color = MaterialTheme.eumColors.surface, border = BorderStroke(1.dp, MaterialTheme.eumColors.line)) {
            Column(Modifier.padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x12)) {
                FormCostLine(stringResource(R.string.request_product_amount), uiModel.productAmountLabel)
                FormCostLine(stringResource(feeLabelRes), uiModel.feeLabel)
                HorizontalDivider(Modifier.padding(vertical = EumSpacing.x8), color = MaterialTheme.eumColors.line)
                FormCostLine(stringResource(expectedLabelRes), uiModel.expectedAmountLabel, isEmphasized = true)
                FormCostLine(stringResource(lastLabelRes), uiModel.paymentMethodLabel)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.Top) {
            Box(Modifier.size(20.dp).border(1.dp, MaterialTheme.eumColors.textSecondary, CircleShape), contentAlignment = Alignment.Center) {
                Text("i", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
            }
            Text(stringResource(noticeRes), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
    }
}

@Composable
private fun FormCostLine(label: String, value: String, isEmphasized: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x4), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
        Text(
            label,
            Modifier.weight(1f),
            style = if (isEmphasized) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodySmall,
            color = if (isEmphasized) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textSecondary,
        )
        Text(value, style = if (isEmphasized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodySmall, textAlign = TextAlign.End)
    }
}

@Composable
private fun FormSectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
}

@Composable
private fun monochromeTextButtonColors() = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textPrimary)
