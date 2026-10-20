package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogSelectedOptionCard(
    option: CatalogOptionUiModel,
    isQuoteRequired: Boolean,
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
                Row(
                    Modifier.height(42.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { onQuantityChanged(quantity - 1) },
                        enabled = quantity > 1,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_minus),
                            contentDescription = stringResource(R.string.catalog_quantity_decrease),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    Text(
                        quantity.toString(),
                        modifier = Modifier.width(28.dp),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                    )
                    IconButton(
                        onClick = { onQuantityChanged(if (quantity < Int.MAX_VALUE) quantity + 1 else Int.MAX_VALUE) },
                        enabled = quantity < Int.MAX_VALUE,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = stringResource(R.string.catalog_quantity_increase),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
                if (!isQuoteRequired) {
                    Text(
                        option.priceLabel.orEmpty(),
                        Modifier.weight(1f),
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}
