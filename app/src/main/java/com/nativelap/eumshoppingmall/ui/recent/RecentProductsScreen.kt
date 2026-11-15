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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.common.EmptyCollectionState
import com.nativelap.eumshoppingmall.ui.common.NoticeInline
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
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
            // Figma 리듬: Body 상 16/하 32, 그룹 간 24, 날짜→그리드 12, 행 간 20.
            LazyColumn(
                modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                contentPadding = PaddingValues(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x16, bottom = EumSpacing.x32),
            ) {
                item { NoticeInline(uiModel.retentionNotice) }
                uiModel.groups.forEach { group ->
                    item(key = "date-${group.dateLabel}") {
                        Text(
                            group.dateLabel,
                            modifier = Modifier.padding(top = EumSpacing.x24, bottom = EumSpacing.x12),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                    }
                    item(key = "items-${group.dateLabel}") {
                        BoxWithConstraints {
                            val columns = if (maxWidth >= 600.dp) 3 else 2
                            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x20)) {
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
    val cardShape = RoundedCornerShape(EumRadius.x8)
    // 평점 줄 규칙: 평점(+리뷰 수) → subtitle → "평점 없음". 카드 공용 파일은 수정하지 않는다.
    val ratingText = product.ratingLabel?.let { rating -> listOfNotNull(rating, product.reviewCountLabel).joinToString(" ") }
        ?: product.subtitle
        ?: stringResource(R.string.product_no_rating)
    val nameColor = if (product.isSoldOut) MaterialTheme.eumColors.textTertiary else MaterialTheme.eumColors.textPrimary
    Column(
        modifier.clip(cardShape).background(MaterialTheme.eumColors.surface).border(1.dp, MaterialTheme.eumColors.line, cardShape)
            .clickable(onClick = onClick).padding(start = EumSpacing.x8, end = EumSpacing.x8, top = EumSpacing.x8, bottom = RECENT_CARD_BOTTOM_PADDING),
        verticalArrangement = Arrangement.spacedBy(RECENT_CARD_INFO_GAP),
    ) {
        Box(
            Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(EumRadius.x6)).background(MaterialTheme.eumColors.surfaceSecondary),
            contentAlignment = Alignment.Center,
        ) {
            product.imageResId?.let { image ->
                Image(painterResource(image), product.imageDescription ?: product.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } ?: Icon(painterResource(R.drawable.ic_image), stringResource(R.string.catalog_product_image_placeholder), tint = MaterialTheme.eumColors.placeholderGlyph, modifier = Modifier.size(32.dp))
            if (product.isSoldOut) {
                // 품절: 이미지 전체를 흰색 60% 오버레이로 덮고 가운데에 배지를 둔다. 삭제 버튼은 오버레이 위에 그린다.
                Box(Modifier.matchParentSize().background(MaterialTheme.eumColors.inverse.copy(alpha = SOLD_OUT_OVERLAY_ALPHA)))
                Text(
                    stringResource(R.string.product_sold_out),
                    Modifier.align(Alignment.Center).clip(RoundedCornerShape(EumRadius.x4)).background(MaterialTheme.eumColors.dangerTint)
                        .padding(horizontal = EumSpacing.x6, vertical = RECENT_BADGE_VERTICAL_PADDING),
                    style = EumTypographyTokens.labelXs,
                    color = MaterialTheme.eumColors.danger,
                )
            }
            // 견적 배지는 가격이 견적 문의일 때만 견적 스타일로 보여 준다.
            product.badgeLabel?.takeIf { product.price == CatalogPriceUiModel.QuoteRequired }?.let { badge ->
                Text(
                    badge,
                    Modifier.align(Alignment.TopStart).padding(EumSpacing.x8).clip(RoundedCornerShape(EumRadius.x4))
                        .background(MaterialTheme.eumColors.surface).border(1.dp, MaterialTheme.eumColors.lineStrong, RoundedCornerShape(EumRadius.x4))
                        .padding(horizontal = EumSpacing.x6, vertical = RECENT_BADGE_VERTICAL_PADDING),
                    style = EumTypographyTokens.labelXs,
                    color = MaterialTheme.eumColors.textPrimary,
                )
            }
            IconButton(onClick = onRemove, modifier = Modifier.align(Alignment.TopEnd).size(48.dp)) {
                Icon(painterResource(R.drawable.ic_x), stringResource(R.string.recent_remove_product, product.name), tint = MaterialTheme.eumColors.textPrimary, modifier = Modifier.size(20.dp))
            }
        }
        // Figma: 정보 영역은 좌우 inset 0, 제품명은 한 줄 말줄임, 평점 줄은 항상 표시한다.
        Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(product.name, style = MaterialTheme.typography.bodySmall, color = nameColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                ratingText,
                style = EumTypographyTokens.labelXs,
                color = MaterialTheme.eumColors.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            when (val price = product.price) {
                is CatalogPriceUiModel.Available -> Text(price.currentLabel, style = EumTypographyTokens.priceM, color = nameColor)

                CatalogPriceUiModel.QuoteRequired -> Text(
                    stringResource(R.string.product_price_inquiry),
                    style = EumTypographyTokens.priceM,
                    color = if (product.isSoldOut) MaterialTheme.eumColors.textTertiary else MaterialTheme.eumColors.textSecondary,
                )
            }
        }
    }
}

// 10dp 간격은 EumSpacing에 토큰이 없고(#39 변경과 충돌 가능) 이 카드에서만 쓴다.
private val RECENT_CARD_BOTTOM_PADDING = 10.dp
private val RECENT_CARD_INFO_GAP = 10.dp
private val RECENT_BADGE_VERTICAL_PADDING = 3.dp
private const val SOLD_OUT_OVERLAY_ALPHA = 0.6f
