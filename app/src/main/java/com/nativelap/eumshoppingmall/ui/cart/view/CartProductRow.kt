package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CartProductRow(
    item: CartItemUiModel,
    onSelectionChanged: (Boolean) -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onOptionChangeClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOrderable = item.type == CartItemType.Orderable && !item.isSoldOut
    val canEditQuantity = item.type == CartItemType.QuoteRequired || isOrderable
    val textColor = if (item.isSoldOut) MaterialTheme.eumColors.textTertiary else MaterialTheme.eumColors.textPrimary
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 120.dp).padding(vertical = EumSpacing.x12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Checkbox(
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
                        modifier = Modifier.padding(end = EumSpacing.x4),
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
                Text(stringResource(R.string.product_sold_out), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.danger)
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
            if (item.type == CartItemType.Orderable && item.unitPriceLabel != item.totalPriceLabel) {
                Text(
                    item.unitPriceLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                    textDecoration = TextDecoration.LineThrough,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (item.type == CartItemType.QuoteRequired) stringResource(R.string.catalog_quote_request) else item.totalPriceLabel,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                )
                CartQuantityStepper(quantity = item.quantity, onQuantityChanged = onQuantityChanged, enabled = canEditQuantity)
            }
        }
    }
}
