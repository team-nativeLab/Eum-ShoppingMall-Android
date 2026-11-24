package com.nativelap.eumshoppingmall.ui.catalog.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceSlot
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.QuickCartButton
import com.nativelap.eumshoppingmall.ui.catalog.canQuickAddToCart
import com.nativelap.eumshoppingmall.ui.catalog.highlightCatalogQuery
import com.nativelap.eumshoppingmall.ui.catalog.quickCartBlockReason
import com.nativelap.eumshoppingmall.ui.catalog.resolveCatalogPriceSlot
import com.nativelap.eumshoppingmall.ui.catalog.resolveCatalogProductBadge
import com.nativelap.eumshoppingmall.ui.common.FavoriteToggleButton
import com.nativelap.eumshoppingmall.ui.common.pressScale
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumShadows
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 그리드 카드. 슬롯 순서: 브랜드 1줄 · 상품명 2줄 · 규격 1줄 · 평점 1줄 · 가격 2줄 · 칩 1줄(담기 버튼 포함).
// 같은 행의 카드 높이는 호출 측이 IntrinsicSize.Max와 fillMaxHeight로 맞춘다.
@Composable
fun CatalogProductCard(
    product: CatalogProductUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    highlightQuery: String? = null,
    // null이면 담기 버튼을 그리지 않는다. 버튼이 있어도 옵션 상품과 품절·가격 문의 상품에는 나오지 않는다.
    onQuickCartClick: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(EumRadius.x12)
    // 상품 속성에는 순위가 없다. 순위 배지는 홈 베스트 섹션 순서로만 붙는다.
    val badge = resolveCatalogProductBadge(bestRank = null, badgeLabel = product.badgeLabel)
    val priceSlot = catalogPriceSlotOf(product)
    val highlightedName = remember(product.name, highlightQuery) { highlightCatalogQuery(product.name, highlightQuery) }
    Column(
        modifier = modifier
            .pressScale()
            // Figma 그림자(0,1,3 #1111110F)는 Modifier.shadow로 근사한다(blur·offset 정확 재현 불가).
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = EumShadows.level1.color,
                spotColor = EumShadows.level1.color,
            )
            .clip(shape)
            .background(MaterialTheme.eumColors.surface)
            .border(1.dp, MaterialTheme.eumColors.lineNeutral, shape)
            .clickable(onClick = onClick)
            .padding(start = EumSpacing.x8, top = EumSpacing.x8, end = EumSpacing.x8, bottom = EumSpacing.x10),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x10),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(EumRadius.x6))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.eumColors.imagePlaceholderStart,
                            MaterialTheme.eumColors.imagePlaceholderEnd,
                        ),
                    ),
                ),
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
            badge?.let { resolvedBadge ->
                // 순위·라벨 배지는 이미지 좌상단 같은 자리에 둔다. 순위가 있으면 라벨은 resolve 단계에서 빠진다.
                ProductCardBadge(
                    badge = resolvedBadge,
                    modifier = Modifier.align(Alignment.TopStart).padding(EumSpacing.x8),
                )
            }
            FavoriteToggleButton(
                isFavorite = product.isFavorite,
                onToggle = onFavoriteClick,
                contentDescription = stringResource(
                    if (product.isFavorite) R.string.catalog_remove_favorite else R.string.catalog_add_favorite,
                    product.name,
                ),
                modifier = Modifier.align(Alignment.TopEnd),
            )
            onQuickCartClick?.takeIf { canQuickAddToCart(product) }?.let { addToCart ->
                Box(Modifier.align(Alignment.BottomEnd)) {
                    QuickCartButton(
                        contentDescription = stringResource(R.string.catalog_quick_add_to_cart_description, product.name),
                        onClick = addToCart,
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            ProductBrandLine(product.subtitle)
            Text(
                highlightedName,
                style = MaterialTheme.typography.bodySmall,
                // Figma 품절 상품명은 회색 톤이라 품절일 때만 textTertiary로 낮춘다.
                color = if (product.isSoldOut) MaterialTheme.eumColors.textTertiary else MaterialTheme.eumColors.textPrimary,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            ProductSpecLine(product.specLabel)
            ProductRatingLine(
                ratingValue = product.ratingValue ?: product.ratingLabel?.toFloatOrNull(),
                ratingText = ratingTextOf(product),
                reviewCountText = product.reviewCountLabel,
            )
            ProductPriceSlot(slot = priceSlot)
            ProductChipRow(
                shippingLabel = product.shippingLabel,
                benefitLabels = product.benefitLabels,
                modifier = Modifier.fillMaxWidth(),
                reasonLabel = quickCartBlockLabel(quickCartBlockReason(product)),
            )
        }
    }
}

// 목록(리스트) 레이아웃 행. 그리드와 같은 정보(규격·평점·가격 슬롯·칩)를 가로 배치로 보여 준다.
@Composable
fun CatalogProductRow(
    product: CatalogProductUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    highlightQuery: String? = null,
    // 목록 행에도 담기 버튼을 넘긴 화면에서만 보인다(격자와 같은 조건).
    onQuickCartClick: (() -> Unit)? = null,
) {
    val priceSlot = catalogPriceSlotOf(product)
    val badge = resolveCatalogProductBadge(bestRank = null, badgeLabel = product.badgeLabel)
    val highlightedName = remember(product.name, highlightQuery) { highlightCatalogQuery(product.name, highlightQuery) }
    Row(
        modifier = modifier.pressScale().fillMaxWidth().clip(RoundedCornerShape(EumRadius.x8))
            // backgroundSubtle이 background와 같아져 구분이 사라져 surfaceSecondary로 보정한다(Figma 노드 값 없음).
            .background(MaterialTheme.eumColors.surfaceSecondary)
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
            badge?.let { resolvedBadge ->
                ProductCardBadge(
                    badge = resolvedBadge,
                    modifier = Modifier.align(Alignment.TopStart).padding(EumSpacing.x4),
                )
            }
            onQuickCartClick?.takeIf { canQuickAddToCart(product) }?.let { addToCart ->
                Box(Modifier.align(Alignment.BottomEnd)) {
                    QuickCartButton(
                        contentDescription = stringResource(R.string.catalog_quick_add_to_cart_description, product.name),
                        onClick = addToCart,
                    )
                }
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            ProductBrandLine(product.subtitle)
            Text(
                highlightedName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textPrimary,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            ProductSpecLine(product.specLabel)
            ProductRatingLine(
                ratingValue = product.ratingValue ?: product.ratingLabel?.toFloatOrNull(),
                ratingText = ratingTextOf(product),
                reviewCountText = product.reviewCountLabel,
            )
            ProductPriceSlot(slot = priceSlot)
            ProductChipRow(
                shippingLabel = product.shippingLabel,
                benefitLabels = product.benefitLabels,
                modifier = Modifier.fillMaxWidth(),
                reasonLabel = quickCartBlockLabel(quickCartBlockReason(product)),
            )
        }
        FavoriteToggleButton(
            isFavorite = product.isFavorite,
            onToggle = onFavoriteClick,
            contentDescription = stringResource(
                if (product.isFavorite) R.string.catalog_remove_favorite else R.string.catalog_add_favorite,
                product.name,
            ),
        )
    }
}

@Composable
private fun ProductBrandLine(subtitle: String?) {
    Text(
        subtitle.orEmpty(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.eumColors.textTertiary,
        minLines = 1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

// 평점 텍스트는 평점이 있을 때만 만든다. 평점이 없으면 null을 넘겨 "평점 없음"을 그린다.
private fun ratingTextOf(product: CatalogProductUiModel): String? {
    if (product.ratingLabel == null && product.ratingValue == null) return null
    return listOfNotNull(product.ratingLabel, product.reviewCountLabel).joinToString(" ")
}

private fun catalogPriceSlotOf(product: CatalogProductUiModel): CatalogPriceSlot {
    val available = product.price as? CatalogPriceUiModel.Available
    return resolveCatalogPriceSlot(
        currentLabel = available?.currentLabel,
        originalLabel = available?.originalLabel,
        discountLabel = available?.discountLabel,
        isPriceUndisclosed = product.price is CatalogPriceUiModel.Undisclosed,
        // 카탈로그 상품에는 장바구니 차단 플래그가 없다. 금액이 없으면 가격 미표시 상품과 같이 "금액 미표시"가 된다.
        isPurchaseUnavailable = false,
    )
}
