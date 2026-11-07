package com.nativelap.eumshoppingmall.ui.productfilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditTextField
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class ProductFilterUiModel(
    val priceRange: String,
    val brand: String,
    val shippingType: String,
    val stock: String,
)

sealed interface ProductFilterScreenEvent {
    data object BackClicked : ProductFilterScreenEvent
    data class FilterFieldClicked(val filter: ProductFilterType) : ProductFilterScreenEvent
    data object ApplyClicked : ProductFilterScreenEvent
}

enum class ProductFilterType {
    PriceRange,
    Brand,
    ShippingType,
    Stock,
}

@Composable
fun ProductFilterScreen(
    uiModel: ProductFilterUiModel,
    onEvent: (ProductFilterScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_filter_title),
        onBackClick = { onEvent(ProductFilterScreenEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_apply_filter),
        onBottomActionClick = { onEvent(ProductFilterScreenEvent.ApplyClicked) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_filter_helper),
                modifier = Modifier.padding(bottom = EumSpacing.x32),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            ProductFilterRow(
                label = stringResource(R.string.ui_audit_price_range),
                value = uiModel.priceRange,
                onClick = { onEvent(ProductFilterScreenEvent.FilterFieldClicked(ProductFilterType.PriceRange)) },
            )
            ProductFilterRow(
                label = stringResource(R.string.ui_audit_brand),
                value = uiModel.brand,
                onClick = { onEvent(ProductFilterScreenEvent.FilterFieldClicked(ProductFilterType.Brand)) },
            )
            ProductFilterRow(
                label = stringResource(R.string.ui_audit_shipping_type),
                value = uiModel.shippingType,
                onClick = { onEvent(ProductFilterScreenEvent.FilterFieldClicked(ProductFilterType.ShippingType)) },
            )
            ProductFilterRow(
                label = stringResource(R.string.ui_audit_stock),
                value = uiModel.stock,
                onClick = { onEvent(ProductFilterScreenEvent.FilterFieldClicked(ProductFilterType.Stock)) },
            )
        }
    }
}

@Composable
private fun ProductFilterRow(
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary)
        UiAuditTextField(
            label = label,
            value = value,
            placeholder = "",
            onValueChange = {},
            readOnly = true,
            onClick = onClick,
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProductFilterScreenPreview() {
    EumShoppingMallTheme {
        val allLabel = stringResource(R.string.ui_audit_all)
        ProductFilterScreen(
            uiModel = ProductFilterUiModel(allLabel, allLabel, allLabel, allLabel),
            onEvent = {},
        )
    }
}
