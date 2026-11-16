package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// Figma A-08 결제 예정 금액: 56dp 한 줄 카드. 글자 서식은 명세에 없어 결제 요약 최종 행(labelL·priceL)과 맞춘다.
@Composable
fun CheckoutExpectedAmountCard(
    amountLabel: String,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(EumRadius.x12)
    // 큰 글꼴·좁은 폭에서 금액이 잘리지 않도록 자리가 모자라면 값을 다음 줄로 넘긴다.
    FlowRow(
        modifier = modifier.fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(MaterialTheme.eumColors.surface, cardShape)
            .border(1.dp, MaterialTheme.eumColors.line, cardShape)
            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center,
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.checkout_payment_amount),
            modifier = Modifier.padding(end = EumSpacing.x8),
            style = EumTypographyTokens.labelL,
            color = MaterialTheme.eumColors.textPrimary,
        )
        Text(
            text = amountLabel,
            style = EumTypographyTokens.priceL,
            color = MaterialTheme.eumColors.textPrimary,
        )
    }
}
