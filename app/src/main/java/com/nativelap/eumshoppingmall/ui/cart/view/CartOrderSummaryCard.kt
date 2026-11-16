package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CartOrderSummaryCard(
    uiModel: CartUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.x12))
            .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
            .padding(EumSpacing.x20),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        SummaryRow(stringResource(R.string.cart_product_amount), uiModel.productAmountLabel.ifBlank { stringResource(R.string.cart_price_check_needed) })
        SummaryRow(stringResource(R.string.cart_shipping_fee), uiModel.shippingFeeLabel.ifBlank { stringResource(R.string.cart_price_check_needed) })
        HorizontalDivider(color = MaterialTheme.eumColors.line)
        // 큰 글꼴·좁은 폭에서 금액이 잘리지 않도록 자리가 모자라면 값을 다음 줄로 넘긴다.
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.Center,
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.cart_expected_payment_amount),
                modifier = Modifier.padding(end = EumSpacing.x8),
                style = EumTypographyTokens.labelL,
                color = MaterialTheme.eumColors.textPrimary,
            )
            // 금액을 모를 때는 안내 문구가 들어오므로 priceL(24sp)로 키우지 않고 기존 16sp 서식을 쓴다.
            Text(
                uiModel.expectedPaymentLabel.ifBlank { stringResource(R.string.cart_price_check_needed) },
                style = if (uiModel.expectedPaymentWon != null) EumTypographyTokens.priceL else MaterialTheme.typography.titleLarge,
                color = MaterialTheme.eumColors.textPrimary,
            )
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
