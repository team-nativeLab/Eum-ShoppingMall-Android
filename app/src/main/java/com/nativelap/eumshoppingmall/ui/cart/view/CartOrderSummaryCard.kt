package com.nativelap.eumshoppingmall.ui.cart.view

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
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CartOrderSummaryCard(
    uiModel: CartUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.eumColors.surfaceSecondary, RoundedCornerShape(EumRadius.x12))
            .padding(EumSpacing.x20),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        SummaryRow(stringResource(R.string.cart_product_amount), uiModel.productAmountLabel.ifBlank { stringResource(R.string.common_zero_currency) })
        SummaryRow(stringResource(R.string.cart_shipping_fee), uiModel.shippingFeeLabel.ifBlank { stringResource(R.string.common_zero_currency) })
        HorizontalDivider(color = MaterialTheme.eumColors.line)
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(R.string.cart_expected_payment_amount),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.eumColors.textPrimary,
            )
            Text(uiModel.expectedPaymentLabel.ifBlank { stringResource(R.string.common_zero_currency) }, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.eumColors.textPrimary)
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}
