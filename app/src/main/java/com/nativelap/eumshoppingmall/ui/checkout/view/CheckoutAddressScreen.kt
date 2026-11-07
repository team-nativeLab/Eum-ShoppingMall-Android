package com.nativelap.eumshoppingmall.ui.checkout.view

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutOrderProductsCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutPriceSummaryCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutSectionHeader
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutStepIndicator
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutAddressScreen(
    uiModel: CheckoutUiModel,
    onEvent: (CheckoutScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(Modifier.statusBarsPadding()) {
                CatalogTopBar(title = stringResource(R.string.checkout_title), onBackClick = { onEvent(CheckoutScreenEvent.BackClicked) })
                CheckoutStepIndicator(currentStep = 1, modifier = Modifier.padding(horizontal = EumSpacing.x40, vertical = EumSpacing.x12))
            }
        },
        bottomBar = {
            Button(
                onClick = { onEvent(CheckoutScreenEvent.NextClicked) },
                modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12).heightIn(min = 56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.eumColors.textPrimary,
                    contentColor = MaterialTheme.eumColors.inverse,
                ),
            ) {
                Text(stringResource(R.string.checkout_next))
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            item { CheckoutSectionHeader(R.string.checkout_shipping_address) }
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                            Text(uiModel.address.recipientName, style = MaterialTheme.typography.labelLarge)
                            Text(uiModel.address.contactLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                        }
                        if (uiModel.address.isDefaultAddress) {
                            Text(
                                stringResource(R.string.checkout_default_address),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.eumColors.inverse,
                                modifier = Modifier.background(MaterialTheme.eumColors.textPrimary, MaterialTheme.shapes.extraSmall).padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
                            )
                        }
                        Text(uiModel.address.addressLabel, style = MaterialTheme.typography.bodySmall)
                    }
                    TextButton(onClick = { onEvent(CheckoutScreenEvent.AddressChangeClicked) }) {
                        Text(stringResource(R.string.checkout_address_change))
                    }
                }
            }
            item { CheckoutSectionHeader(R.string.checkout_shipping_request) }
            item {
                Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12)) {
                    androidx.compose.foundation.layout.Box {
                        androidx.compose.material3.OutlinedButton(
                            onClick = { onEvent(CheckoutScreenEvent.DeliveryRequestPickerToggled) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        ) {
                            Text(uiModel.deliveryRequestLabel ?: stringResource(R.string.checkout_delivery_request_prompt), Modifier.weight(1f))
                            androidx.compose.material3.Icon(
                                androidx.compose.ui.res.painterResource(R.drawable.ic_chevron_down),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        DropdownMenu(
                            expanded = uiModel.isDeliveryRequestPickerVisible,
                            onDismissRequest = { onEvent(CheckoutScreenEvent.DeliveryRequestPickerToggled) },
                        ) {
                            uiModel.deliveryRequestOptions.forEach { request ->
                                DropdownMenuItem(
                                    text = { Text(request) },
                                    onClick = { onEvent(CheckoutScreenEvent.DeliveryRequestSelected(request)) },
                                )
                            }
                        }
                    }
                }
            }
            item { CheckoutSectionHeader(R.string.checkout_delivery_method) }
            item {
                Row(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                    Text(uiModel.deliveryMethodLabel, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                    Text(stringResource(R.string.checkout_delivery_free), style = MaterialTheme.typography.labelMedium)
                }
            }
            item { CheckoutSectionHeader(R.string.checkout_order_products) }
            item {
                CheckoutOrderProductsCard(
                    products = uiModel.products,
                    isExpanded = uiModel.productsExpanded,
                    onExpandedChanged = { onEvent(CheckoutScreenEvent.ProductsExpandedChanged(it)) },
                    modifier = Modifier.padding(vertical = EumSpacing.x8),
                )
            }
            item {
                CheckoutPriceSummaryCard(
                    summary = uiModel.paymentSummary,
                    showDiscount = false,
                    modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                )
            }
        }
    }
}
