package com.nativelap.eumshoppingmall.ui.home.components

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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun HomeProductCard(
    product: HomeProductUiModel,
    isFavorite: Boolean,
    onClick: (HomeProductUiModel) -> Unit,
    onFavoriteClick: (HomeProductUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(EumRadius.x8)
    Column(
        modifier.clip(cardShape)
            .background(MaterialTheme.eumColors.background)
            .border(1.dp, MaterialTheme.eumColors.line, cardShape)
            .clickable { onClick(product) }
            .padding(EumSpacing.x8),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Box(
            Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(EumRadius.x6))
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        listOf(
                            MaterialTheme.eumColors.imagePlaceholderStart,
                            MaterialTheme.eumColors.imagePlaceholderEnd,
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            val imageResId = product.imageResId
            if (imageResId != null) {
                Image(
                    painterResource(imageResId),
                    product.imageDescription ?: product.name,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Icon(
                    painterResource(R.drawable.ic_image),
                    contentDescription = stringResource(R.string.home_product_image_placeholder),
                    tint = MaterialTheme.eumColors.placeholderGlyph,
                    modifier = Modifier.size(32.dp),
                )
            }
            if (product.isSoldOut) {
                Box(Modifier.fillMaxSize().background(MaterialTheme.eumColors.dim))
                Text(
                    stringResource(R.string.product_sold_out),
                    Modifier.align(Alignment.Center)
                        .padding(EumSpacing.x8).clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.eumColors.dangerTint)
                        .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.danger,
                )
            }
            IconButton(
                onClick = { onFavoriteClick(product) },
                modifier = Modifier.align(Alignment.TopEnd).size(44.dp),
            ) {
                Icon(
                    painter = painterResource(
                        if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart,
                    ),
                    contentDescription = stringResource(
                        if (isFavorite) R.string.home_product_favorite_remove else R.string.home_product_favorite_add,
                        product.name,
                    ),
                    tint = if (isFavorite) MaterialTheme.eumColors.danger else MaterialTheme.eumColors.textSecondary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        Column(
            Modifier.padding(horizontal = EumSpacing.x4),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            Text(
                product.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            product.ratingLabel?.let { rating ->
                Row(
                    modifier = Modifier.heightIn(min = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                ) {
                    product.ratingValue?.takeIf { it in 0f..5f }?.let { ratingValue ->
                        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                            repeat(5) { index ->
                                Icon(
                                    painterResource(
                                        if (index < ratingValue.toInt()) {
                                            R.drawable.ic_star_filled
                                        } else {
                                            R.drawable.ic_star
                                        },
                                    ),
                                    contentDescription = null,
                                    tint = MaterialTheme.eumColors.ratingStar,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        }
                    }
                    Text(
                        rating,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textTertiary,
                        maxLines = 1,
                    )
                }
            }
            if (product.isPurchaseUnavailable || product.priceLabel == null) {
                Text(
                    stringResource(R.string.home_product_purchase_unavailable),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.eumColors.textSecondary,
                )
                Text(
                    stringResource(R.string.home_product_unavailable_badge),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
            } else {
                val originalPrice = product.originalPriceLabel
                if (originalPrice != null) {
                    Text(
                        originalPrice,
                        style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.LineThrough),
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    product.discountRateLabel?.let { discount ->
                        Text(
                            discount,
                            style = EumTypographyTokens.labelS,
                            color = MaterialTheme.eumColors.danger,
                        )
                        Spacer(Modifier.width(EumSpacing.x4))
                    }
                    Text(
                        product.priceLabel.orEmpty(),
                        style = EumTypographyTokens.priceM,
                        color = if (product.discountRateLabel != null) {
                            MaterialTheme.eumColors.danger
                        } else {
                            MaterialTheme.eumColors.textPrimary
                        },
                    )
                }
            }
        }
    }
}
