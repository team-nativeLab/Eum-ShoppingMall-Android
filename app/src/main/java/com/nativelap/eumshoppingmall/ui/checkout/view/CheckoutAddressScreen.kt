package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutExpectedAmountCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutOrderProductsCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutOutlineActionButton
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutSectionCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutStepIndicator
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.shippingFeeText
import com.nativelap.eumshoppingmall.ui.common.EumPrimaryButton
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

internal val CHECKOUT_CONTENT_MAX_WIDTH = 520.dp

internal fun checkoutContentWidthForViewport(viewportWidth: androidx.compose.ui.unit.Dp) =
    minOf(viewportWidth, CHECKOUT_CONTENT_MAX_WIDTH)

@Composable
fun CheckoutAddressScreen(
    uiModel: CheckoutUiModel,
    onEvent: (CheckoutScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val checkoutContentWidth = checkoutContentWidthForViewport(maxWidth)
        Scaffold(
            modifier = Modifier.width(checkoutContentWidth).fillMaxSize().align(Alignment.TopCenter),
            topBar = {
                Column(Modifier.statusBarsPadding()) {
                    CatalogTopBar(title = stringResource(R.string.checkout_title), onBackClick = { onEvent(CheckoutScreenEvent.BackClicked) })
                }
            },
            bottomBar = {
                // 배경은 전폭, 버튼만 폭 컨테이너 안에 둔다. 구분선은 Figma 미확인이라 넣지 않는다.
                Box(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)) {
                    EumPrimaryButton(
                        onClick = { onEvent(CheckoutScreenEvent.NextClicked) },
                        modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                    ) {
                        Text(stringResource(R.string.checkout_next))
                    }
                }
            },
            containerColor = MaterialTheme.eumColors.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
                contentPadding = PaddingValues(bottom = EumSpacing.x20),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                // 스텝퍼는 본문 첫 항목(72dp)이고, 첫 섹션 카드와는 20dp 간격이다(Figma A-08).
                item { CheckoutStepIndicator(currentStep = 1, modifier = Modifier.padding(bottom = EumSpacing.x12)) }
                item { AddressSectionCard(uiModel, onEvent) }
                item { DeliveryRequestSectionCard(uiModel, onEvent) }
                item {
                    CheckoutSectionCard(R.string.checkout_delivery_method) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(uiModel.deliveryMethodLabel, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                            Text(
                                uiModel.paymentSummary.shippingFeeText(
                                    stringResource(R.string.checkout_delivery_free),
                                    stringResource(R.string.checkout_shipping_fee_unknown),
                                ),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
                item {
                    CheckoutSectionCard(R.string.checkout_order_products) {
                        CheckoutOrderProductsCard(
                            products = uiModel.products,
                            isExpanded = uiModel.productsExpanded,
                            onExpandedChanged = { onEvent(CheckoutScreenEvent.ProductsExpandedChanged(it)) },
                        )
                    }
                }
                item {
                    CheckoutExpectedAmountCard(
                        amountLabel = uiModel.paymentSummary.finalAmountLabel,
                        modifier = Modifier.padding(horizontal = EumSpacing.x20),
                    )
                }
            }
        }
    }
}

@Composable
private fun AddressSectionCard(
    uiModel: CheckoutUiModel,
    onEvent: (CheckoutScreenEvent) -> Unit,
) {
    CheckoutSectionCard(R.string.checkout_shipping_address) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalArrangement = Arrangement.Center, itemVerticalAlignment = Alignment.CenterVertically) {
                    Text(uiModel.address.recipientName, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.eumColors.textPrimary)
                    Text(uiModel.address.contactLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                }
                if (uiModel.address.isDefaultAddress) {
                    // 이름 행과 간격 6(spacedBy 4 + 위 2).
                    Box(
                        modifier = Modifier.padding(top = EumSpacing.x2)
                            .heightIn(min = 20.dp)
                            .widthIn(min = 66.dp)
                            .background(MaterialTheme.eumColors.textPrimary, RoundedCornerShape(EumRadius.x4))
                            .padding(horizontal = EumSpacing.x6),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            stringResource(R.string.checkout_default_address),
                            style = EumTypographyTokens.labelXs,
                            color = MaterialTheme.eumColors.inverse,
                        )
                    }
                }
                Text(uiModel.address.addressLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
            }
            CheckoutOutlineActionButton(
                label = stringResource(R.string.checkout_address_change),
                onClick = { onEvent(CheckoutScreenEvent.AddressChangeClicked) },
            )
        }
    }
}

// 선택 메뉴(DropdownMenu)는 유지하고 입력 필드 모양(48dp, r8)만 맞춘다.
@Composable
private fun DeliveryRequestSectionCard(
    uiModel: CheckoutUiModel,
    onEvent: (CheckoutScreenEvent) -> Unit,
) {
    CheckoutSectionCard(R.string.checkout_shipping_request) {
        Box {
            val fieldShape = RoundedCornerShape(EumRadius.x8)
            val requestLabel = uiModel.deliveryRequestLabel
            Row(
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .background(MaterialTheme.eumColors.surface, fieldShape)
                    .border(1.dp, MaterialTheme.eumColors.lineNeutral, fieldShape)
                    .clip(fieldShape)
                    .clickable(role = Role.Button) { onEvent(CheckoutScreenEvent.DeliveryRequestPickerToggled) }
                    .padding(horizontal = 14.dp, vertical = EumSpacing.x12),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = requestLabel ?: stringResource(R.string.checkout_delivery_request_prompt),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (requestLabel == null) MaterialTheme.eumColors.textDisabled else MaterialTheme.eumColors.textPrimary,
                )
                Icon(
                    painterResource(R.drawable.ic_chevron_down),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textSecondary,
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
