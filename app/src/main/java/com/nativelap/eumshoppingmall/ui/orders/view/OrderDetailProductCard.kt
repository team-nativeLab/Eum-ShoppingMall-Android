package com.nativelap.eumshoppingmall.ui.orders.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderProductThumbnail
import com.nativelap.eumshoppingmall.ui.orders.view.components.OrderStatusBadge
import com.nativelap.eumshoppingmall.ui.orders.viewmodel.OrderDetailProductUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderDetailProductCard(product: OrderDetailProductUiModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = EumSpacing.x8),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        verticalAlignment = Alignment.Top,
    ) {
        OrderProductThumbnail(product.imageResId, product.name)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            OrderStatusBadge(product.status)
            Text(product.name, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(product.optionLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
            Text(product.priceAndQuantityLabel, style = MaterialTheme.typography.titleSmall)
        }
    }
}
