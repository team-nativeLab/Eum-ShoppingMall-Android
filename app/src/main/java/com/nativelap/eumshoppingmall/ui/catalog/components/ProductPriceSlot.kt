package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceKind
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceSlot
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 가격 슬롯은 항상 두 줄이다. 첫 줄은 원가·할인(없으면 빈 줄), 둘째 줄은 판매가 또는 "금액 미표시"·"구매 불가" 문구.
// 품절 표시는 이 슬롯이 아니라 이미지 위 "품절" 배지가 맡는다.
@Composable
internal fun ProductPriceSlot(
    slot: CatalogPriceSlot,
    modifier: Modifier = Modifier,
) {
    val hasDiscount = slot.discountLabel != null
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.heightIn(min = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                slot.originalLabel.orEmpty(),
                style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.LineThrough),
                color = MaterialTheme.eumColors.textTertiary,
                minLines = 1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            slot.discountLabel?.let { discount ->
                Spacer(Modifier.width(EumSpacing.x4))

                val discountShape = RoundedCornerShape(EumRadius.x4)
                Box(
                    modifier = Modifier
                        .clip(discountShape)
                        .background(MaterialTheme.eumColors.dangerTint)
                        .padding(horizontal = EumSpacing.x4),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        discount,
                        style = EumTypographyTokens.labelS.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.eumColors.danger,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        val mainLabel = when (slot.kind) {
            CatalogPriceKind.Price -> slot.currentLabel.orEmpty()
            CatalogPriceKind.Undisclosed -> stringResource(R.string.product_price_undisclosed)
            CatalogPriceKind.Unavailable -> stringResource(R.string.product_purchase_unavailable)
        }
        val mainColor = when {
            slot.kind == CatalogPriceKind.Unavailable -> MaterialTheme.eumColors.textTertiary
            slot.kind == CatalogPriceKind.Price && hasDiscount -> MaterialTheme.eumColors.danger
            else -> MaterialTheme.eumColors.textPrimary
        }
        Text(
            mainLabel,
            style = EumTypographyTokens.priceM,
            color = mainColor,
            minLines = 1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
