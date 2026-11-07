package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutProductUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutOrderProductsCard(
    products: List<CheckoutProductUiModel>,
    isExpanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(R.string.checkout_product_count, products.size),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
            )
            TextButton(onClick = { onExpandedChanged(!isExpanded) }) {
                Text(stringResource(if (isExpanded) R.string.common_fold else R.string.common_view_details))
                Icon(
                    painterResource(if (isExpanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.textSecondary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        if (isExpanded) {
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                products.forEach { product -> CheckoutOrderProductRow(product) }
            }
        }
    }
}

@Composable
private fun CheckoutOrderProductRow(product: CheckoutProductUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(56.dp).clip(RoundedCornerShape(EumRadius.x8)).background(MaterialTheme.eumColors.surfaceSecondary),
            contentAlignment = Alignment.Center,
        ) {
            product.imageResId?.let { Image(painterResource(it), product.name, Modifier.size(56.dp)) }
                ?: Icon(
                    painterResource(R.drawable.ic_image),
                    contentDescription = stringResource(R.string.catalog_product_image_placeholder),
                    tint = MaterialTheme.eumColors.placeholderGlyph,
                    modifier = Modifier.size(24.dp),
                )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(product.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
            Text(product.optionAndQuantityLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
            Text(product.priceLabel, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
        }
    }
}
