package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogOptionUiModel
import com.nativelap.eumshoppingmall.ui.common.EUM_MAX_QUANTITY
import com.nativelap.eumshoppingmall.ui.common.EumQuantityStepper
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogSelectedOptionCard(
    option: CatalogOptionUiModel,
    isPriceUndisclosed: Boolean,
    onQuantityChanged: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val quantity = option.quantity.coerceAtLeast(1)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(EumSpacing.x16),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(option.label, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_x),
                        contentDescription = stringResource(R.string.catalog_option_remove, option.label),
                        tint = MaterialTheme.eumColors.textTertiary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                EumQuantityStepper(
                    quantity = quantity,
                    onDecrease = { onQuantityChanged(quantity - 1) },
                    onIncrease = { onQuantityChanged((quantity + 1).coerceAtMost(EUM_MAX_QUANTITY)) },
                    canDecrease = quantity > 1,
                    canIncrease = quantity < EUM_MAX_QUANTITY,
                )
                // 0원 옵션은 "₩0" 행을 만들지 않는다(U-04). 가격 문구가 없어도 같은 규칙으로 숨긴다.
                val priceLabel = option.priceLabel?.takeIf { it.isNotBlank() && option.unitPriceWon != 0L }
                if (!isPriceUndisclosed && priceLabel != null) {
                    Text(
                        priceLabel,
                        Modifier.weight(1f),
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}
