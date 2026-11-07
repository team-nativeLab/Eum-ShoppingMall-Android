package com.nativelap.eumshoppingmall.ui.orders.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderPaymentBreakdownUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderPaymentBreakdownCard(payment: OrderPaymentBreakdownUiModel, modifier: Modifier = Modifier) {
    OrderInfoCard(modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            Text(stringResource(R.string.checkout_payment_information), style = MaterialTheme.typography.labelMedium)
            PaymentRow(stringResource(R.string.cart_product_amount), payment.productAmountLabel)
            PaymentRow(stringResource(R.string.cart_shipping_fee), payment.shippingFeeLabel)
            PaymentRow(stringResource(R.string.checkout_coupon_discount), payment.couponDiscountLabel)
            PaymentRow(stringResource(R.string.checkout_points_used), payment.pointsUsedLabel)
            HorizontalDivider(color = MaterialTheme.eumColors.line)
            PaymentRow(stringResource(R.string.checkout_final_payment_amount), payment.totalAmountLabel, emphasize = true)
            PaymentRow(stringResource(R.string.checkout_payment_method_label), payment.paymentMethodLabel)
        }
    }
}

@Composable
private fun PaymentRow(label: String, value: String, emphasize: Boolean = false) {
    Row(Modifier.fillMaxWidth()) {
        Text(
            label,
            modifier = Modifier.weight(1f).padding(end = EumSpacing.x8),
            style = if (emphasize) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodySmall,
            color = if (emphasize) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textSecondary,
        )
        Text(
            value,
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
    }
}
