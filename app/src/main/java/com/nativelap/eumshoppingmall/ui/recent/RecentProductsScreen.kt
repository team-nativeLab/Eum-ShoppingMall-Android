package com.nativelap.eumshoppingmall.ui.recent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.common.EmptyCollectionState
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun RecentProductsScreen(
    uiModel: RecentProductsUiModel,
    onEvent: (RecentProductsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isEmpty = uiModel.groups.all { it.products.isEmpty() }
    Column(modifier.fillMaxSize()) {
        CatalogTopBar(
            title = stringResource(R.string.home_recent_products),
            onBackClick = { onEvent(RecentProductsEvent.BackClicked) },
            actions = listOf(CatalogTopBarAction("delete-all", R.drawable.ic_trash_2, stringResource(R.string.recent_delete_all), stringResource(R.string.recent_delete_all))),
            onActionClick = { onEvent(RecentProductsEvent.DeleteAllClicked) },
            modifier = Modifier.statusBarsPadding(),
        )
        if (isEmpty) {
            EmptyCollectionState(
                title = stringResource(R.string.recent_empty_title),
                description = stringResource(R.string.recent_empty_description),
                actionLabel = stringResource(R.string.recent_empty_action),
                onAction = { onEvent(RecentProductsEvent.BrowseProductsClicked) },
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.eumColors.surface).border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(12.dp))
                            .padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x12),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(painterResource(R.drawable.ic_info), null, tint = MaterialTheme.eumColors.textSecondary, modifier = Modifier.size(16.dp))
                        Text(uiModel.retentionNotice, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                    }
                }
                uiModel.groups.forEach { group ->
                    item(key = "date-${group.dateLabel}") {
                        Text(group.dateLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                    }
                    item(key = "items-${group.dateLabel}") {
                        BoxWithConstraints {
                            val columns = if (maxWidth >= 600.dp) 3 else 2
                            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                                group.products.chunked(columns).forEach { rowProducts ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                        rowProducts.forEach { product ->
                                            RecentProductCard(
                                                product = product,
                                                onClick = { onEvent(RecentProductsEvent.ProductClicked(product.id)) },
                                                onRemove = { onEvent(RecentProductsEvent.ProductRemoved(product.id)) },
                                                modifier = Modifier.weight(1f),
                                            )
                                        }
                                        repeat(columns - rowProducts.size) { Spacer(Modifier.weight(1f)) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentProductCard(
    product: CatalogProductUiModel,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(8.dp)
    Column(
        modifier.clip(cardShape).background(MaterialTheme.eumColors.surface).border(1.dp, MaterialTheme.eumColors.line, cardShape)
            .clickable(onClick = onClick).padding(EumSpacing.x8),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Box(
            Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.eumColors.surfaceSecondary),
            contentAlignment = Alignment.Center,
        ) {
            product.imageResId?.let { image ->
                Image(painterResource(image), product.imageDescription ?: product.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } ?: Icon(painterResource(R.drawable.ic_image), stringResource(R.string.catalog_product_image_placeholder), tint = MaterialTheme.eumColors.placeholderGlyph, modifier = Modifier.size(28.dp))
            IconButton(onClick = onRemove, modifier = Modifier.align(Alignment.TopEnd).size(48.dp)) {
                Icon(painterResource(R.drawable.ic_x), stringResource(R.string.recent_remove_product, product.name), tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
            }
            if (product.isSoldOut) {
                Text(stringResource(R.string.product_sold_out), Modifier.align(Alignment.Center).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.eumColors.dangerTint).padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.danger)
            }
        }
        Column(Modifier.padding(horizontal = EumSpacing.x4), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(product.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary, maxLines = 2)
            product.subtitle?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary, maxLines = 1) }
            when (val price = product.price) {
                is CatalogPriceUiModel.Available -> Text(price.currentLabel, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
                CatalogPriceUiModel.QuoteRequired -> Text(stringResource(R.string.product_price_inquiry), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textSecondary)
            }
        }
    }
}
