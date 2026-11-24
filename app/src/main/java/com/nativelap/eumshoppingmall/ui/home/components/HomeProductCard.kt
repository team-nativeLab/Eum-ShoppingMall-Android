package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.QuickCartButton
import com.nativelap.eumshoppingmall.ui.catalog.canQuickAddToHomeProduct
import com.nativelap.eumshoppingmall.ui.catalog.components.ProductCardBadge
import com.nativelap.eumshoppingmall.ui.catalog.components.ProductChipRow
import com.nativelap.eumshoppingmall.ui.catalog.components.ProductPriceSlot
import com.nativelap.eumshoppingmall.ui.catalog.components.ProductRatingLine
import com.nativelap.eumshoppingmall.ui.catalog.components.ProductSpecLine
import com.nativelap.eumshoppingmall.ui.catalog.components.quickCartBlockLabel
import com.nativelap.eumshoppingmall.ui.catalog.homeQuickCartBlockReason
import com.nativelap.eumshoppingmall.ui.catalog.resolveCatalogPriceSlot
import com.nativelap.eumshoppingmall.ui.catalog.resolveCatalogProductBadge
import com.nativelap.eumshoppingmall.ui.common.FavoriteToggleButton
import com.nativelap.eumshoppingmall.ui.common.pressScale
import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun HomeProductCard(
    product: HomeProductUiModel,
    isFavorite: Boolean,
    onClick: (HomeProductUiModel) -> Unit,
    onFavoriteClick: (HomeProductUiModel) -> Unit,
    modifier: Modifier = Modifier,
    // null이면 담기 버튼을 그리지 않는다. 버튼이 있어도 옵션·품절·가격 문의 상품에는 나오지 않는다.
    onQuickCartClick: (() -> Unit)? = null,
) {
    val cardShape = RoundedCornerShape(EumRadius.x8)
    // 순위 배지가 있으면 가격 미표시 라벨은 가린다(resolveCatalogProductBadge 규칙).
    val badge = resolveCatalogProductBadge(bestRank = product.bestRank, badgeLabel = product.badgeLabel)
    // 가격 미표시 상품은 "금액 미표시", 가격 없이 장바구니가 막힌 상품은 "구매 불가"를 보여 준다. 품절은 가격 자리를 바꾸지 않는다(웹 기준).
    val priceSlot = resolveCatalogPriceSlot(
        currentLabel = product.priceLabel,
        originalLabel = product.originalPriceLabel,
        discountLabel = product.discountRateLabel,
        isPriceUndisclosed = product.isPriceUndisclosed,
        isPurchaseUnavailable = product.isPurchaseUnavailable,
    )
    Column(
        modifier.pressScale().clip(cardShape)
            .background(MaterialTheme.eumColors.surface)
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
            badge?.let { resolvedBadge ->
                ProductCardBadge(
                    badge = resolvedBadge,
                    modifier = Modifier.align(Alignment.TopStart).padding(EumSpacing.x8),
                )
            }
            FavoriteToggleButton(
                isFavorite = isFavorite,
                onToggle = { onFavoriteClick(product) },
                contentDescription = stringResource(
                    if (isFavorite) R.string.home_product_favorite_remove else R.string.home_product_favorite_add,
                    product.name,
                ),
                modifier = Modifier.align(Alignment.TopEnd),
                buttonSize = 44.dp,
            )
            onQuickCartClick?.takeIf { canQuickAddToHomeProduct(product) }?.let { addToCart ->
                Box(Modifier.align(Alignment.BottomEnd)) {
                    QuickCartButton(
                        contentDescription = stringResource(R.string.catalog_quick_add_to_cart_description, product.name),
                        onClick = addToCart,
                    )
                }
            }
        }
        // 슬롯 순서: 상품명 1줄 · 규격 1줄 · 평점 1줄 · 가격 2줄 · 배송·혜택 칩 1줄. 값이 없으면 빈 줄을 유지한다.
        // 상품명을 1줄로 두는 것은 사용자 결정이다(카탈로그 카드는 2줄). 바꾸려면 사용자 확인이 필요하다.
        Column(
            Modifier.padding(horizontal = EumSpacing.x4),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            Text(
                product.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textPrimary,
                minLines = 1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            ProductSpecLine(product.specLabel)
            ProductRatingLine(
                ratingValue = product.ratingValue,
                ratingText = product.ratingLabel,
                reviewCountText = null,
            )
            ProductPriceSlot(slot = priceSlot)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProductChipRow(
                    shippingLabel = product.shippingLabel,
                    benefitLabels = product.benefitLabels,
                    modifier = Modifier.fillMaxWidth(),
                    reasonLabel = quickCartBlockLabel(homeQuickCartBlockReason(product)),
                )
            }
        }
    }
}
