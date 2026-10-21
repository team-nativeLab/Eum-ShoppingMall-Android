package com.nativelap.eumshoppingmall.ui.catalog.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogProductCard(
    product: CatalogProductUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(EumRadius.x8)
    Column(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.eumColors.backgroundSubtle)
            .border(1.dp, MaterialTheme.eumColors.lineStrong, shape)
            .clickable(onClick = onClick)
            .padding(EumSpacing.x8),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(EumRadius.x4))
                .background(MaterialTheme.eumColors.surface),
            contentAlignment = Alignment.Center,
        ) {
            val imageResId = product.imageResId
            if (imageResId == null) {
                Icon(
                    painterResource(R.drawable.ic_image),
                    contentDescription = stringResource(R.string.catalog_product_image_placeholder),
                    tint = MaterialTheme.eumColors.placeholderGlyph,
                    modifier = Modifier.size(28.dp),
                )
            } else {
                Image(
                    painterResource(imageResId),
                    contentDescription = product.imageDescription ?: product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            if (product.isSoldOut) {
                Box(Modifier.fillMaxSize().background(MaterialTheme.eumColors.inverse.copy(alpha = 0.6f)))
                Text(
                    stringResource(R.string.product_sold_out),
                    modifier = Modifier.align(Alignment.Center)
                        .clip(RoundedCornerShape(EumRadius.x4))
                        .background(MaterialTheme.eumColors.dangerTint)
                        .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.danger,
                )
            }
            product.badgeLabel?.let { badge ->
                Text(
                    badge,
                    modifier = Modifier.align(Alignment.TopStart)
                        .padding(EumSpacing.x4)
                        .clip(RoundedCornerShape(EumRadius.x4))
                        .background(MaterialTheme.eumColors.background)
                        .padding(horizontal = EumSpacing.x4, vertical = EumSpacing.x2),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textSecondary,
                )
            }
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.align(Alignment.TopEnd).size(48.dp),
            ) {
                Icon(
                    painterResource(if (product.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                    contentDescription = stringResource(
                        if (product.isFavorite) R.string.catalog_remove_favorite else R.string.catalog_add_favorite,
                        product.name,
                    ),
                    tint = if (product.isFavorite) MaterialTheme.eumColors.danger else MaterialTheme.eumColors.textSecondary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = EumSpacing.x4),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            Text(
                product.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textPrimary,
                maxLines = 2,
            )
            product.subtitle?.let { subtitle ->
                Text(
                    subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                    maxLines = 1,
                )
            }
            product.ratingLabel?.let { rating ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    rating.toFloatOrNull()?.takeIf { it in 0f..5f }?.let { value ->
                        val filledStarCount = value.toInt()
                        repeat(5) { index ->
                            Icon(
                                painterResource(
                                    if (index < filledStarCount) R.drawable.ic_star_catalog_filled else R.drawable.ic_star,
                                ),
                                contentDescription = null,
                                tint = if (index < filledStarCount) MaterialTheme.eumColors.warning else MaterialTheme.eumColors.lineStrong,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                        Spacer(Modifier.width(EumSpacing.x4))
                    }
                    Text(
                        listOfNotNull(rating, product.reviewCountLabel).joinToString(" "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textTertiary,
                        maxLines = 1,
                    )
                }
            }
            when (val price = product.price) {
                is CatalogPriceUiModel.Available -> {
                    if (price.originalLabel != null || price.discountLabel != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            price.originalLabel?.let { original ->
                                Text(
                                    original,
                                    style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.LineThrough),
                                    color = MaterialTheme.eumColors.textTertiary,
                                )
                            }
                            price.discountLabel?.let { discount ->
                                Spacer(Modifier.width(EumSpacing.x4))
                                Text(discount, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.danger)
                            }
                        }
                    }
                    Text(price.currentLabel, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
                }

                CatalogPriceUiModel.QuoteRequired -> Text(
                    stringResource(R.string.product_price_inquiry),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.eumColors.textSecondary,
                )
            }
        }
    }
}

@Composable
fun CatalogProductRow(
    product: CatalogProductUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(EumRadius.x8))
            .background(MaterialTheme.eumColors.backgroundSubtle)
            .border(1.dp, MaterialTheme.eumColors.lineStrong, RoundedCornerShape(EumRadius.x8))
            .clickable(onClick = onClick).padding(EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        Box(
            modifier = Modifier.size(96.dp).clip(RoundedCornerShape(EumRadius.x4)).background(MaterialTheme.eumColors.surface),
            contentAlignment = Alignment.Center,
        ) {
            product.imageResId?.let { imageResId ->
                Image(
                    painterResource(imageResId),
                    product.imageDescription ?: product.name,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } ?: Icon(
                painterResource(R.drawable.ic_image),
                contentDescription = stringResource(R.string.catalog_product_image_placeholder),
                tint = MaterialTheme.eumColors.placeholderGlyph,
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(product.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary, maxLines = 2)
            product.subtitle?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary, maxLines = 1) }
            when (val price = product.price) {
                is CatalogPriceUiModel.Available -> Text(price.currentLabel, style = MaterialTheme.typography.titleSmall)
                CatalogPriceUiModel.QuoteRequired -> Text(stringResource(R.string.product_price_inquiry), style = MaterialTheme.typography.titleSmall)
            }
        }
        IconButton(onClick = onFavoriteClick, modifier = Modifier.size(48.dp)) {
            Icon(
                painterResource(if (product.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                stringResource(if (product.isFavorite) R.string.catalog_remove_favorite else R.string.catalog_add_favorite, product.name),
                tint = if (product.isFavorite) MaterialTheme.eumColors.danger else MaterialTheme.eumColors.textSecondary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
