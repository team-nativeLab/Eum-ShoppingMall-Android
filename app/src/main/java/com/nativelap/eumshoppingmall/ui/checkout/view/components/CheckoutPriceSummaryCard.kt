package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutPriceSummaryCard(
    summary: CheckoutPaymentSummaryUiModel,
    showDiscount: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.eumColors.surfaceSecondary, RoundedCornerShape(EumRadius.x12))
            .padding(EumSpacing.x16),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        SummaryRow(stringResource(R.string.checkout_product_amount_label), summary.productAmountLabel)
        SummaryRow(stringResource(R.string.cart_shipping_fee), summary.shippingFeeLabel)
        if (showDiscount) SummaryRow(stringResource(R.string.checkout_discount), summary.discountLabel)
        HorizontalDivider(color = MaterialTheme.eumColors.line)
        SummaryRow(
            label = stringResource(if (showDiscount) R.string.checkout_final_payment_amount else R.string.checkout_payment_amount),
            value = summary.finalAmountLabel,
            emphasized = true,
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    emphasized: Boolean = false,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            modifier = Modifier.weight(1f),
            style = if (emphasized) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
        Text(
            value,
            style = if (emphasized) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
    }
}
