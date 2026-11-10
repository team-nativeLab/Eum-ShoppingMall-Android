package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumCheckboxColors
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CartProductRow(
    item: CartItemUiModel,
    onSelectionChanged: (Boolean) -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onOptionChangeClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier,
    /** 수량 저장이 끝날 때까지 true. 연타로 여러 변경이 겹치지 않게 +/−를 막는다(L1). */
    isQuantityLocked: Boolean = false,
) {
    val isOrderable = item.type == CartItemType.Orderable && !item.isSoldOut
    val canEditQuantity = (item.type == CartItemType.QuoteRequired || isOrderable) && !isQuantityLocked
    val textColor = if (item.isSoldOut) MaterialTheme.eumColors.textTertiary else MaterialTheme.eumColors.textPrimary
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 120.dp).padding(vertical = EumSpacing.x12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Checkbox(
            colors = eumCheckboxColors(),
            checked = item.isSelected,
            onCheckedChange = onSelectionChanged,
            enabled = isOrderable,
            modifier = Modifier.size(48.dp),
        )
        Box(
            Modifier.size(80.dp).clip(RoundedCornerShape(EumRadius.x8)).background(MaterialTheme.eumColors.surfaceSecondary),
            contentAlignment = Alignment.Center,
        ) {
            item.imageResId?.let { imageResId ->
                Image(painterResource(imageResId), item.name, Modifier.size(80.dp))
            } ?: Icon(
                painterResource(R.drawable.ic_image),
                contentDescription = stringResource(R.string.catalog_product_image_placeholder),
                tint = MaterialTheme.eumColors.placeholderGlyph,
                modifier = Modifier.size(28.dp),
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x2)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.type == CartItemType.QuoteRequired) {
                    Text(
                        stringResource(R.string.cart_quote_product_badge),
                        modifier = Modifier
                            .padding(end = EumSpacing.x4)
                            .border(1.dp, MaterialTheme.eumColors.lineControl, RoundedCornerShape(4.dp))
                            .padding(horizontal = EumSpacing.x4, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
                Text(
                    text = item.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(onClick = onRemoveClicked, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painterResource(R.drawable.ic_x),
                        contentDescription = stringResource(R.string.cart_remove_product, item.name),
                        tint = MaterialTheme.eumColors.textTertiary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            if (item.isSoldOut) {
                // 품절은 빨간 글자 대신 위험 배경 배지로 표시한다(m2). 글자 크기가 같아 본문 줄 높이는 바뀌지 않는다.
                Text(
                    stringResource(R.string.product_sold_out),
                    modifier = Modifier
                        .clip(RoundedCornerShape(EumRadius.x4))
                        .background(MaterialTheme.eumColors.dangerTint)
                        .padding(horizontal = EumSpacing.x6),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.danger,
                )
            }
            Text(item.optionLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
            TextButton(
                onClick = onOptionChangeClicked,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            ) {
                Text(
                    stringResource(R.string.cart_change_option),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textSecondary,
                    textDecoration = TextDecoration.Underline,
                )
            }
            // 단가(수량 포함)는 작은 글씨로 두고, 줄 합계는 아래 가격 칸에 보인다. 원가 정보가 없으므로 취소선은 긋지 않는다(M2).
            if (item.type == CartItemType.Orderable) {
                Text(
                    item.unitPriceLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
            }
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val fontScale = LocalDensity.current.fontScale
                val priceLabel = if (item.type == CartItemType.QuoteRequired) {
                    stringResource(R.string.cart_price_inquiry)
                } else {
                    item.totalPriceLabel
                }
                if (maxWidth < 180.dp || fontScale >= 1.3f) {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                        Text(
                            text = priceLabel,
                            style = MaterialTheme.typography.titleSmall,
                            color = textColor,
                            maxLines = 1,
                            softWrap = false,
                        )
                        CartQuantityStepper(
                            quantity = item.quantity,
                            onQuantityChanged = onQuantityChanged,
                            enabled = canEditQuantity,
                            modifier = Modifier.align(Alignment.End),
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = priceLabel,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleSmall,
                            color = textColor,
                            maxLines = 1,
                            softWrap = false,
                        )
                        CartQuantityStepper(
                            quantity = item.quantity,
                            onQuantityChanged = onQuantityChanged,
                            enabled = canEditQuantity,
                        )
                    }
                }
            }
        }
    }
}
