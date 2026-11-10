package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogOptionGroupSelector
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogSelectedOptionCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.common.EumOutlinedButton
import com.nativelap.eumshoppingmall.ui.common.EumPrimaryButton
import com.nativelap.eumshoppingmall.ui.common.EumSnackbarHost
import com.nativelap.eumshoppingmall.ui.common.FavoriteToggleButton
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.common.ResponsiveWidthContainer
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

// LazyColumn item 순서: 0 상품 정보, 1 탭, 2 상품 설명(상품 정보 표), 3 배송·교환, 4 리뷰, 5 문의. 탭 선택 시 스크롤 대상이다.
private const val PRODUCT_INFORMATION_ITEM_INDEX = 2
private const val SHIPPING_EXCHANGE_ITEM_INDEX = 3
private const val REVIEWS_ITEM_INDEX = 4
private const val QUESTIONS_ITEM_INDEX = 5

/**
 * 상품 상세 화면. 옵션 시트 상태는 Route가 넘기며, 미리보기처럼 넘기지 않으면 화면이 자체 상태를 만든다.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CatalogProductDetailScreen(
    uiModel: CatalogProductDetailUiModel,
    onEvent: (CatalogProductDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    CatalogProductDetailContent(
        uiModel = uiModel,
        onEvent = onEvent,
        optionsSheetState = rememberModalBottomSheetState(),
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

// 시트 상태 타입이 실험 API라서 공개 시그니처에 노출하지 않고 같은 모듈 안에서만 받는다.
@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun CatalogProductDetailContent(
    uiModel: CatalogProductDetailUiModel,
    onEvent: (CatalogProductDetailEvent) -> Unit,
    optionsSheetState: SheetState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.Scaffold(
        modifier = modifier,
        snackbarHost = { EumSnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CatalogTopBar(
                title = "",
                onBackClick = { onEvent(CatalogProductDetailEvent.BackClicked) },
                actions = listOf(
                    CatalogTopBarAction("share", R.drawable.ic_share_2, stringResource(R.string.catalog_share)),
                    CatalogTopBarAction("cart", R.drawable.ic_shopping_cart, stringResource(R.string.common_cart)),
                ),
                onActionClick = { action ->
                    onEvent(if (action == "share") CatalogProductDetailEvent.ShareClicked else CatalogProductDetailEvent.CartClicked)
                },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        bottomBar = {
            ResponsiveWidthContainer {
                ProductDetailBottomActions(uiModel, onEvent, Modifier.navigationBarsPadding())
            }
        },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
    ) { padding ->
        val listState = rememberLazyListState()
        var hasInitializedTabs by remember { mutableStateOf(false) }
        val selectedTabIndex = uiModel.selectedTabIndex.coerceIn(0, 3)
        LaunchedEffect(selectedTabIndex) {
            if (hasInitializedTabs) {
                // 탭 순서(상품 설명·배송 안내·리뷰·문의)와 LazyColumn item 순서가 일치하도록 항목 인덱스를 고정한다.
                val targetItemIndex = when (selectedTabIndex) {
                    0 -> PRODUCT_INFORMATION_ITEM_INDEX
                    1 -> SHIPPING_EXCHANGE_ITEM_INDEX
                    2 -> REVIEWS_ITEM_INDEX
                    else -> QUESTIONS_ITEM_INDEX
                }
                listState.animateScrollToItem(targetItemIndex)
            }
            hasInitializedTabs = true
        }
        ResponsiveContentContainer(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                state = listState,
                contentPadding = PaddingValues(bottom = EumSpacing.x24),
            ) {
                item {
                    ProductGallery(uiModel, onEvent)
                    Column(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x20)) {
                        // Figma 상단 순서: 브랜드(견적 상품 배지) → 상품명 → 평점 → 가격 → 배송 정보 박스
                        val brandName = uiModel.product.subtitle
                        val badgeLabel = uiModel.product.badgeLabel
                        // 브랜드와 배지는 같은 줄에 두되, 한쪽이 없어도 다른 쪽이 사라지지 않도록 각각 따로 판단한다.
                        if (brandName != null || badgeLabel != null) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                                brandName?.let { name ->
                                    Text(name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                                }
                                badgeLabel?.let { badge ->
                                    Surface(shape = RoundedCornerShape(EumRadius.x4), border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line)) {
                                        Text(badge, Modifier.padding(horizontal = EumSpacing.x6, vertical = EumSpacing.x2), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                                    }
                                }
                            }
                        }
                        Text(uiModel.product.name, Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.eumColors.textPrimary)
                        uiModel.product.ratingLabel?.let { ratingLabel ->
                            ProductRatingRow(
                                ratingLabel = ratingLabel,
                                reviewCountLabel = uiModel.product.reviewCountLabel,
                                modifier = Modifier.padding(top = EumSpacing.x12),
                            )
                        }
                        when (val price = uiModel.product.price) {
                            is CatalogPriceUiModel.Available -> Row(
                                modifier = Modifier.padding(top = EumSpacing.x16),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                            ) {
                                price.discountLabel?.let {
                                    Text(it, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.danger)
                                }
                                price.originalLabel?.let {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.eumColors.textTertiary,
                                        textDecoration = TextDecoration.LineThrough,
                                    )
                                }
                                Text(
                                    price.currentLabel,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = if (price.discountLabel != null) MaterialTheme.eumColors.danger else MaterialTheme.eumColors.textPrimary,
                                )
                            }

                            CatalogPriceUiModel.QuoteRequired -> Text(
                                stringResource(R.string.product_price_inquiry),
                                Modifier.padding(top = EumSpacing.x16),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.eumColors.textPrimary,
                            )
                        }
                        uiModel.description?.takeIf(String::isNotBlank)?.let { description ->
                            Text(
                                description,
                                Modifier.padding(top = EumSpacing.x16),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.eumColors.textSecondary,
                            )
                        }
                        if (uiModel.shippingRows.isNotEmpty()) {
                            Column(
                                Modifier.fillMaxWidth().padding(top = EumSpacing.x20)
                                    .border(EumSpacing.x1, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
                                    .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x8),
                            ) {
                                uiModel.shippingRows.forEach { row -> ProductKeyValueRow(row) }
                            }
                        }
                    }
                }
                item { ProductDetailTabs(uiModel, onEvent) }
                item {
                    Column(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x24)) {
                        Text(stringResource(R.string.product_information), style = MaterialTheme.typography.titleMedium)
                        uiModel.specifications.forEach { ProductKeyValueRow(it) }
                    }
                }
                item {
                    Column(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                        Text(stringResource(R.string.product_shipping_exchange_tab), style = MaterialTheme.typography.titleMedium)
                        uiModel.returnRows.forEach { ProductKeyValueRow(it) }
                    }
                }
                item {
                    Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                        ProductSectionTitle(stringResource(R.string.catalog_product_reviews), uiModel.reviewCount)
                        if (uiModel.reviews.isEmpty()) {
                            Text(stringResource(R.string.catalog_no_reviews), Modifier.padding(vertical = EumSpacing.x20), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                        } else {
                            uiModel.reviews.forEach { review -> ProductReviewCard(review) }
                        }
                        OutlinedButton(onClick = { onEvent(CatalogProductDetailEvent.TabSelected(2)) }, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.catalog_review_write))
                        }
                    }
                }
                item {
                    Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                        ProductSectionTitle(stringResource(R.string.catalog_product_questions), uiModel.questionCount)
                        if (uiModel.questions.isEmpty()) {
                            Text(stringResource(R.string.catalog_no_questions), Modifier.padding(vertical = EumSpacing.x20), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                        } else {
                            uiModel.questions.forEach { question -> ProductQuestionCard(question) }
                        }
                        OutlinedButton(onClick = { onEvent(CatalogProductDetailEvent.TabSelected(3)) }, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.catalog_question_write))
                        }
                    }
                }
                item {
                    BulkPurchaseInquiryCard(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16))
                }
                if (uiModel.relatedProducts.isNotEmpty()) {
                    item { Text(stringResource(R.string.catalog_product_related), Modifier.padding(EumSpacing.x20), style = MaterialTheme.typography.titleMedium) }
                    items(uiModel.relatedProducts.chunked(2)) { row ->
                        Row(Modifier.padding(horizontal = EumSpacing.x20), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                            row.forEach { product ->
                                CatalogProductCard(
                                    product,
                                    onClick = { onEvent(CatalogProductDetailEvent.RelatedProductSelected(product.id)) },
                                    onFavoriteClick = { onEvent(CatalogProductDetailEvent.FavoriteToggled(product.id)) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
    if (uiModel.optionsSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(CatalogProductDetailEvent.CloseOptionsSheet) },
            sheetState = optionsSheetState,
        ) {
            ProductOptionsSheet(uiModel, onEvent)
        }
    }
}

@Composable
private fun ProductGallery(uiModel: CatalogProductDetailUiModel, onEvent: (CatalogProductDetailEvent) -> Unit) {
    val product = uiModel.product
    val galleryImages = uiModel.galleryImageResIds.ifEmpty { listOf(product.imageResId) }
    val selectedImageIndex = uiModel.selectedImageIndex.coerceIn(0, galleryImages.lastIndex)
    val pagerState = rememberPagerState(initialPage = selectedImageIndex) { galleryImages.size }
    val currentOnEvent by rememberUpdatedState(onEvent)
    // 호출자가 선택 인덱스를 바꾸면 페이저를 같은 페이지로 맞춘다. 스와이프로 바뀐 값은 아래 이벤트로 알리므로 되돌림이 생기지 않는다.
    LaunchedEffect(selectedImageIndex) {
        if (pagerState.currentPage != selectedImageIndex) {
            pagerState.animateScrollToPage(selectedImageIndex)
        }
    }
    // 스와이프로 페이지가 확정되면 기존 ImageSelected 이벤트를 보낸다. 첫 페이지는 초기 상태이므로 보내지 않는다.
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .drop(1)
            .collect { page -> currentOnEvent(CatalogProductDetailEvent.ImageSelected(page)) }
    }
    // Figma 갤러리는 좌우 20dp 여백의 정사각형이다(상단 여백 8dp).
    Box(
        Modifier.padding(start = EumSpacing.x20, top = EumSpacing.x8, end = EumSpacing.x20)
            .fillMaxWidth().aspectRatio(1f)
            .clip(RoundedCornerShape(EumRadius.x12))
            .background(MaterialTheme.eumColors.surface),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            // 이미지가 한 장이면 스와이프를 끄고, 세로 스크롤은 상위 목록이 그대로 받는다.
            userScrollEnabled = galleryImages.size > 1,
        ) { page ->
            val image = galleryImages[page]
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (image != null) {
                    Image(
                        painter = painterResource(image),
                        contentDescription = product.imageDescription ?: product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_image),
                        contentDescription = stringResource(R.string.catalog_product_image_placeholder),
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.eumColors.placeholderGlyph,
                    )
                }
            }
        }
        // Figma 갤러리는 화살표 없이 페이지 카운터만 둔다. 이미지 전환은 가로 스와이프로 한다.
        Text(
            "${pagerState.currentPage + 1} / ${galleryImages.size}",
            Modifier.align(Alignment.BottomEnd).padding(EumSpacing.x12)
                .clip(RoundedCornerShape(EumRadius.capsule)).background(MaterialTheme.eumColors.textPrimary.copy(alpha = 0.72f))
                .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
            color = MaterialTheme.eumColors.inverse,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun ProductDetailTabs(uiModel: CatalogProductDetailUiModel, onEvent: (CatalogProductDetailEvent) -> Unit) {
    Row(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.background)) {
        listOf(
            stringResource(R.string.catalog_product_description),
            stringResource(R.string.catalog_product_shipping),
            stringResource(R.string.catalog_product_reviews),
            stringResource(R.string.catalog_product_questions),
        ).forEachIndexed { index, label ->
            Surface(
                onClick = { onEvent(CatalogProductDetailEvent.TabSelected(index)) },
                modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                color = MaterialTheme.eumColors.background,
                border = BorderStroke(1.dp, if (uiModel.selectedTabIndex.coerceIn(0, 3) == index) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.line),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(label, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun ProductKeyValueRow(row: CatalogKeyValueUiModel) {
    Row(
        Modifier.fillMaxWidth().padding(top = EumSpacing.x12),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x16),
    ) {
        Text(row.label, Modifier.weight(0.38f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
        Text(row.value, Modifier.weight(0.62f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

// Figma 평점 행: 평점의 정수 부분만큼 별을 채우고 나머지는 빈 별로 둔다(4.7이면 4개를 채운다).
@Composable
private fun ProductRatingRow(ratingLabel: String, reviewCountLabel: String?, modifier: Modifier = Modifier) {
    val filledStarCount = ratingLabel.toFloatOrNull()?.toInt()?.coerceIn(0, 5) ?: 0
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            repeat(5) { index ->
                val isFilled = index < filledStarCount
                Icon(
                    painterResource(if (isFilled) R.drawable.ic_star_catalog_filled else R.drawable.ic_star),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isFilled) MaterialTheme.eumColors.ratingStar else MaterialTheme.eumColors.lineStrong,
                )
            }
        }
        val ratingText = reviewCountLabel?.let { stringResource(R.string.catalog_rating_with_review_count, ratingLabel, it) } ?: ratingLabel
        Text(
            ratingText,
            Modifier.padding(start = EumSpacing.x12).background(MaterialTheme.eumColors.surface)
                .padding(horizontal = EumSpacing.x6, vertical = EumSpacing.x4),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.eumColors.textSecondary,
        )
    }
}

// 대량 구매 카드는 진입점 추가 금지 제약으로 탭 동작을 연결하지 않는다(시각만 맞춘다).
@Composable
private fun BulkPurchaseInquiryCard(modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth()
            .border(EumSpacing.x1, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
            .padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x16),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painterResource(R.drawable.ic_message_square_text),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.eumColors.textSecondary,
        )
        Text(
            stringResource(R.string.product_bulk_custom_inquiry),
            Modifier.weight(1f).padding(start = EumSpacing.x12),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.eumColors.textPrimary,
        )
    }
}

@Composable
private fun ProductSectionTitle(title: String, count: Int) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 64.dp).padding(horizontal = EumSpacing.x20),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Text(count.toString(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textTertiary)
    }
}

@Composable
private fun ProductReviewCard(review: CatalogReviewUiModel) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = EumSpacing.x12),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(review.authorLabel, Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
            Icon(
                painterResource(R.drawable.ic_star_catalog_filled),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.eumColors.warning,
            )
            Text(review.ratingLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
        }
        Text(review.content, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(review.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
    }
}

@Composable
private fun ProductQuestionCard(question: CatalogQuestionUiModel) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = EumSpacing.x12),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Text(question.question, style = MaterialTheme.typography.bodySmall)
        question.answer?.let {
            Text("${stringResource(R.string.catalog_answer)}  $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        }
        Text(question.dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
    }
}

@Composable
private fun ProductDetailBottomActions(
    uiModel: CatalogProductDetailUiModel,
    onEvent: (CatalogProductDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isQuoteRequired = uiModel.product.price is CatalogPriceUiModel.QuoteRequired
    val isSoldOut = uiModel.product.isSoldOut
    if (isQuoteRequired && !isSoldOut) {
        ProductQuoteBottomActions(uiModel, onEvent, modifier)
        return
    }
    Row(
        modifier.fillMaxWidth().background(MaterialTheme.eumColors.background).padding(horizontal = EumSpacing.x16, vertical = EumSpacing.x8),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isSoldOut) {
            FavoriteToggleButton(
                isFavorite = uiModel.product.isFavorite,
                onToggle = { onEvent(CatalogProductDetailEvent.ProductFavoriteToggled) },
                contentDescription = stringResource(
                    if (uiModel.product.isFavorite) R.string.catalog_remove_favorite else R.string.catalog_add_favorite,
                    uiModel.product.name,
                ),
                bordered = true,
            )
            EumPrimaryButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.product_sold_out))
            }
        } else {
            FavoriteToggleButton(
                isFavorite = uiModel.product.isFavorite,
                onToggle = { onEvent(CatalogProductDetailEvent.ProductFavoriteToggled) },
                contentDescription = stringResource(
                    if (uiModel.product.isFavorite) R.string.catalog_remove_favorite else R.string.catalog_add_favorite,
                    uiModel.product.name,
                ),
                bordered = true,
            )
            EumOutlinedButton(
                onClick = {
                    onEvent(CatalogProductDetailEvent.SelectOptionClicked(CatalogPurchaseAction.AddToCart))
                },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.catalog_add_to_cart))
            }
            EumPrimaryButton(
                onClick = {
                    onEvent(CatalogProductDetailEvent.SelectOptionClicked(CatalogPurchaseAction.BuyNow))
                },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.catalog_buy_now))
            }
        }
    }
}

// 찜·장바구니 버튼은 이 상태에 없으므로 추가하지 않는다(시각만 맞춘다). 버튼 동작은 기존 분기와 같다.
@Composable
private fun ProductQuoteBottomActions(
    uiModel: CatalogProductDetailUiModel,
    onEvent: (CatalogProductDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)) {
        Row(
            // 가격 문의 안내는 오류가 아니라 정보이므로 중립 표면 토큰을 쓴다(L-c).
            Modifier.fillMaxWidth().heightIn(min = 40.dp).background(MaterialTheme.eumColors.surfaceSecondary).padding(horizontal = EumSpacing.x20),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 이 안내는 오류가 아니라 정보다. 오류 색 대신 보조 텍스트 색을 쓴다(N5).
            Icon(painterResource(R.drawable.ic_circle_alert), null, Modifier.size(16.dp), tint = MaterialTheme.eumColors.textSecondary)
            Spacer(Modifier.width(EumSpacing.x12))
            Text(
                stringResource(R.string.product_price_inquiry_description),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.eumColors.textSecondary,
            )
        }
        Box(Modifier.fillMaxWidth().height(EumSpacing.x1).background(MaterialTheme.eumColors.lineNeutral))
        OutlinedButton(
            onClick = {
                onEvent(
                    if (uiModel.optionGroups.isEmpty()) {
                        CatalogProductDetailEvent.QuoteRequestClicked
                    } else {
                        CatalogProductDetailEvent.SelectOptionClicked(CatalogPurchaseAction.QuoteRequest)
                    },
                )
            },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12)
                .heightIn(min = 56.dp),
            shape = RoundedCornerShape(EumRadius.x8),
            border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.productBadgeBorder),
        ) {
            Text(
                stringResource(R.string.product_request_quote),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.eumColors.textPrimary,
            )
        }
    }
}


@Composable
private fun ProductOptionsSheet(uiModel: CatalogProductDetailUiModel, onEvent: (CatalogProductDetailEvent) -> Unit) {
    // 필수 옵션을 고르지 않은 상태에서 담기·구매를 누르면 안내를 보여 준다. 버튼은 비활성처럼 보이되 누를 수 있다.
    var showSelectPrompt by remember { mutableStateOf(false) }
    val isQuoteRequired = uiModel.product.price is CatalogPriceUiModel.QuoteRequired
    val isSoldOut = uiModel.product.isSoldOut
    // 옵션 그룹이 있으면 선택된 옵션이 최소 1개여야 담기·구매를 할 수 있다(0개 담기 방지).
    val hasRequiredSelections = (uiModel.optionGroups.isEmpty() || uiModel.selectedOptions.isNotEmpty()) &&
        uiModel.optionGroups.all { group ->
        uiModel.selectedOptions.any { selectedOption ->
            selectedOption.groupId == group.id && group.options.any { it.id == selectedOption.option.id }
        }
    }
    // 강조는 "방금 탭함" 상태에서만 쓴다. 선택 충족 여부가 바뀌면(채우거나 지우면) 강조를 끈다(L-b).
    LaunchedEffect(hasRequiredSelections) {
        showSelectPrompt = false
    }

    Column(
        Modifier.fillMaxWidth().heightIn(max = 760.dp)
            .padding(horizontal = EumSpacing.x20).padding(bottom = EumSpacing.x24),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        Row(
            Modifier.fillMaxWidth().heightIn(min = 56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.catalog_product_options),
                Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
            )
            IconButton(
                onClick = { onEvent(CatalogProductDetailEvent.CloseOptionsSheet) },
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painterResource(R.drawable.ic_x),
                    contentDescription = stringResource(R.string.common_close),
                )
            }
        }
        Column(
            Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            if (!isQuoteRequired && uiModel.optionGroups.isEmpty()) {
                Text(stringResource(R.string.catalog_no_options), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
            }
            uiModel.optionGroups.forEach { group ->
                CatalogOptionGroupSelector(
                    group = group,
                    onOptionSelected = { optionId ->
                        onEvent(CatalogProductDetailEvent.OptionSelected(group.id, optionId))
                    },
                )
            }

            uiModel.selectedOptions.forEach { selectedOption ->
                val option = selectedOption.option
                CatalogSelectedOptionCard(
                    option = option,
                    isQuoteRequired = isQuoteRequired,
                    onQuantityChanged = { quantity ->
                        onEvent(CatalogProductDetailEvent.QuantityChanged(selectedOption.groupId, option.id, quantity))
                    },
                    onRemove = { onEvent(CatalogProductDetailEvent.OptionRemoved(selectedOption.groupId, option.id)) },
                )
            }

            if (uiModel.selectedOptions.isNotEmpty()) {
                if (isQuoteRequired) {
                    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                        Text(stringResource(R.string.catalog_options_selected_quantity), style = MaterialTheme.typography.labelMedium)
                        Text(
                            stringResource(
                                R.string.catalog_options_selected_count,
                                uiModel.selectedOptions.sumOf { it.option.quantity.coerceAtLeast(1).toLong() },
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                        Text(
                            stringResource(R.string.catalog_quote_options_notice),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.eumColors.textSecondary,
                        )
                    }
                } else {
                    // 수량 행은 항상 보이고, 추가금 합계(라벨과 금액)만 합계를 알 수 있을 때 보인다.
                    val extraTotalLabel = uiModel.totalPriceLabel?.takeIf(String::isNotBlank)
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = EumSpacing.x8),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                            if (extraTotalLabel != null) {
                                Text(stringResource(R.string.catalog_options_extra_total), style = MaterialTheme.typography.bodySmall)
                            }
                            Text(
                                stringResource(
                                    R.string.catalog_options_total_count,
                                    uiModel.selectedOptions.sumOf { it.option.quantity.coerceAtLeast(1).toLong() },
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.eumColors.textTertiary,
                            )
                        }
                        if (extraTotalLabel != null) {
                            Text(
                                extraTotalLabel,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.eumColors.textPrimary,
                            )
                        }
                    }
                }
            }
        }

        // 필수 옵션을 다 고르기 전에는 항상 안내를 보인다(L7). 버튼을 누르면 같은 안내를 강조한다.
        if (!hasRequiredSelections && !isSoldOut) {
            Text(
                stringResource(R.string.catalog_option_select_prompt),
                style = MaterialTheme.typography.labelMedium,
                color = if (showSelectPrompt) MaterialTheme.eumColors.danger else MaterialTheme.eumColors.textTertiary,
            )
        }
        if (isSoldOut) {
            EumPrimaryButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.product_sold_out))
            }
        } else if (isQuoteRequired) {
            EumPrimaryButton(
                onClick = {
                    if (hasRequiredSelections) onEvent(CatalogProductDetailEvent.QuoteRequestClicked) else showSelectPrompt = true
                },
                inactiveLook = !hasRequiredSelections,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.catalog_quote_request))
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
            ) {
                EumOutlinedButton(
                    onClick = {
                        if (hasRequiredSelections) onEvent(CatalogProductDetailEvent.AddToCartClicked) else showSelectPrompt = true
                    },
                    inactiveLook = !hasRequiredSelections,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.catalog_add_to_cart_options))
                }
                EumPrimaryButton(
                    onClick = {
                        if (hasRequiredSelections) onEvent(CatalogProductDetailEvent.BuyNowClicked) else showSelectPrompt = true
                    },
                    inactiveLook = !hasRequiredSelections,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.catalog_buy_now))
                }
            }
        }
    }
}
