package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.shippingFeeText
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutPriceSummaryCard(
    summary: CheckoutPaymentSummaryUiModel,
    showDiscount: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.x12))
            .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
            .padding(EumSpacing.x20),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        // 상품금액·배송비·할인 묶음은 행 간격 8, 묶음과 구분선·최종행 사이는 12(Figma A-09).
        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            SummaryRow(stringResource(R.string.checkout_product_amount_label), summary.productAmountLabel)
            SummaryRow(
                stringResource(R.string.cart_shipping_fee),
                summary.shippingFeeText(
                    stringResource(R.string.checkout_delivery_free),
                    stringResource(R.string.checkout_shipping_fee_unknown),
                ),
            )
            if (showDiscount) SummaryRow(stringResource(R.string.checkout_discount), summary.discountLabel)
        }
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
    // 큰 글꼴·좁은 폭에서 금액이 잘리지 않도록 자리가 모자라면 값을 다음 줄로 넘긴다.
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center,
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            modifier = Modifier.padding(end = EumSpacing.x8),
            style = if (emphasized) EumTypographyTokens.labelL else MaterialTheme.typography.bodySmall,
            color = if (emphasized) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textSecondary,
        )
        Text(
            value,
            style = if (emphasized) EumTypographyTokens.priceL else MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
    }
}
