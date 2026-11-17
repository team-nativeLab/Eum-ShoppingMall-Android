package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
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
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

// Figma 직접 지정 값(노드 179:938)이라 디자인 토큰이 아니다. 빈 별 색은 #38 합의대로 토큰을 쓴다(상품 상세 전용).
private val DETAIL_DISCOUNT_COLOR = Color(0xFFDB2129)
private val DETAIL_STAR_FILL_COLOR = Color(0xFFE6A93D)

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
                ProductDetailBottomActions(uiModel, onEvent)
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
                    Column(Modifier.padding(start = EumSpacing.x20, top = EumSpacing.x24, end = EumSpacing.x20, bottom = EumSpacing.x32)) {
                        // Figma 상단 순서: 브랜드(견적 상품 배지) → 상품명 → 평점 → 가격 → 배송 정보 박스
                        val brandName = uiModel.product.subtitle
                        val badgeLabel = uiModel.product.badgeLabel
                        // 브랜드와 배지는 같은 줄에 두되, 한쪽이 없어도 다른 쪽이 사라지지 않도록 각각 따로 판단한다.
                        if (brandName != null || badgeLabel != null) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                                brandName?.let { name ->
                                    Text(name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                                }
                                badgeLabel?.let { badge ->
                                    Surface(shape = RoundedCornerShape(EumRadius.x4), border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.line)) {
                                        Text(badge, Modifier.padding(horizontal = EumSpacing.x6, vertical = EumSpacing.x2), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
                                    }
                                }
                            }
                        }
                        Text(uiModel.product.name, Modifier.padding(top = EumSpacing.x6), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.eumColors.textPrimary)
                        uiModel.product.ratingLabel?.let { ratingLabel ->
                            ProductRatingRow(
                                ratingLabel = ratingLabel,
                                reviewCountLabel = uiModel.product.reviewCountLabel,
                                modifier = Modifier.padding(top = EumSpacing.x6),
                            )
                        }
                        when (val price = uiModel.product.price) {
                            is CatalogPriceUiModel.Available -> Row(
                                modifier = Modifier.padding(top = EumSpacing.x8),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                            ) {
                                price.discountLabel?.let {
                                    Text(it, style = MaterialTheme.typography.labelSmall, color = DETAIL_DISCOUNT_COLOR)
                                }
                                price.originalLabel?.let {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodySmall,
                                        // Figma 정가 #7A7A7A는 흰 배경 대비 4.5:1에 못 미칠 수 있어 textTertiary를 유지한다.
                                        color = MaterialTheme.eumColors.textTertiary,
                                        textDecoration = TextDecoration.LineThrough,
                                    )
                                }
                                Text(
                                    price.currentLabel,
                                    style = EumTypographyTokens.priceL,
                                    color = if (price.discountLabel != null) DETAIL_DISCOUNT_COLOR else MaterialTheme.eumColors.textPrimary,
                                )
                            }

                            CatalogPriceUiModel.QuoteRequired -> Text(
                                stringResource(R.string.product_price_inquiry),
                                Modifier.padding(top = EumSpacing.x8),
                                style = EumTypographyTokens.priceL,
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
                                    .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.x12))
                                    .border(EumSpacing.x1, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
                                    .padding(EumSpacing.x16),
                                verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                            ) {
                                uiModel.shippingRows.forEach { row -> ProductKeyValueRow(row, labelWidth = 72.dp) }
                            }
                        }
                    }
                }
                item { ProductDetailTabs(uiModel, onEvent) }
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Column(
                            Modifier.padding(start = EumSpacing.x20, top = EumSpacing.x24, end = EumSpacing.x20, bottom = EumSpacing.x24),
                            verticalArrangement = Arrangement.spacedBy(EumSpacing.x10),
                        ) {
                            Text(stringResource(R.string.product_information), style = MaterialTheme.typography.titleLarge)
                            uiModel.specifications.forEach { ProductKeyValueRow(it, labelWidth = 96.dp) }
                        }
                        ProductSectionDivider()
                    }
                }
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Column(
                            Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                            verticalArrangement = Arrangement.spacedBy(EumSpacing.x10),
                        ) {
                            Text(stringResource(R.string.product_shipping_exchange_tab), style = MaterialTheme.typography.titleLarge)
                            uiModel.returnRows.forEach { ProductKeyValueRow(it, labelWidth = 96.dp) }
                        }
                        ProductSectionDivider()
                    }
                }
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                            ProductSectionTitle(stringResource(R.string.catalog_product_reviews), uiModel.reviewCount)
                            if (uiModel.reviews.isEmpty()) {
                                Text(stringResource(R.string.catalog_no_reviews), Modifier.padding(vertical = EumSpacing.x20), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                            } else {
                                uiModel.reviews.forEach { review -> ProductReviewCard(review) }
                            }
                            if (uiModel.reviewCount > 0) {
                                TextButton(onClick = { onEvent(CatalogProductDetailEvent.AllReviewsClicked) }, modifier = Modifier.fillMaxWidth()) {
                                    Text(stringResource(R.string.flow_view_all_reviews))
                                }
                            }
                            OutlinedButton(onClick = { onEvent(CatalogProductDetailEvent.ReviewWriteClicked) }, modifier = Modifier.fillMaxWidth()) {
                                Text(stringResource(R.string.catalog_review_write))
                            }
                        }
                        ProductSectionDivider()
                    }
                }
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                            ProductSectionTitle(stringResource(R.string.catalog_product_questions), uiModel.questionCount)
                            if (uiModel.questions.isEmpty()) {
                                Text(stringResource(R.string.catalog_no_questions), Modifier.padding(vertical = EumSpacing.x20), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                            } else {
                                uiModel.questions.forEach { question -> ProductQuestionCard(question) }
                            }
                            OutlinedButton(onClick = { onEvent(CatalogProductDetailEvent.QuestionWriteClicked) }, modifier = Modifier.fillMaxWidth()) {
                                Text(stringResource(R.string.catalog_question_write))
                            }
                        }
                        ProductSectionDivider()
                    }
                }
                item {
                    BulkPurchaseInquiryCard(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16))
                }
                if (uiModel.relatedProducts.isNotEmpty()) {
                    item { Text(stringResource(R.string.catalog_product_related), Modifier.padding(EumSpacing.x20), style = MaterialTheme.typography.titleMedium) }
                    itemsIndexed(uiModel.relatedProducts.chunked(2)) { rowIndex, row ->
                        // 목록 격자와 같은 행 간격(x24)을 행 사이에만 둔다.
                        if (rowIndex > 0) Spacer(Modifier.height(EumSpacing.x24))
                        // 연관 상품 행의 카드는 가장 높은 카드에 맞춘다.
                        Row(
                            Modifier.padding(horizontal = EumSpacing.x20).height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                        ) {
                            row.forEach { product ->
                                CatalogProductCard(
                                    product,
                                    onClick = { onEvent(CatalogProductDetailEvent.RelatedProductSelected(product.id)) },
                                    onFavoriteClick = { onEvent(CatalogProductDetailEvent.FavoriteToggled(product.id)) },
                                    modifier = Modifier.weight(1f).fillMaxHeight(),
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
            Modifier.align(Alignment.BottomEnd).padding(EumSpacing.x16)
                .clip(RoundedCornerShape(EumRadius.capsule)).background(MaterialTheme.eumColors.textPrimary.copy(alpha = 0.72f))
                .padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x4),
            color = MaterialTheme.eumColors.inverse,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
internal fun ProductDetailTabs(uiModel: CatalogProductDetailUiModel, onEvent: (CatalogProductDetailEvent) -> Unit) {
    val selectedTabIndex = uiModel.selectedTabIndex.coerceIn(0, 3)
    Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)) {
        Box(Modifier.fillMaxWidth().height(EumSpacing.x1).background(MaterialTheme.eumColors.line))
        // 큰 글꼴에서 한 탭만 줄바꿈돼도 밑줄 y가 모든 탭에서 같도록 가장 높은 탭에 높이를 맞춘다.
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            listOf(
                stringResource(R.string.catalog_product_description),
                stringResource(R.string.catalog_product_shipping),
                stringResource(R.string.catalog_product_reviews),
                stringResource(R.string.catalog_product_questions),
            ).forEachIndexed { index, label ->
                val isSelected = selectedTabIndex == index
                Column(
                    Modifier.weight(1f).fillMaxHeight()
                        .selectable(
                            selected = isSelected,
                            role = Role.Tab,
                            onClick = { onEvent(CatalogProductDetailEvent.TabSelected(index)) },
                        ),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    // 탭 높이 48 = 위 14 + 글자 20 + 아래 12 + 밑줄 2.
                    Text(
                        label,
                        Modifier.fillMaxWidth().padding(top = EumSpacing.x14, bottom = EumSpacing.x12),
                        style = if (isSelected) EumTypographyTokens.labelM else MaterialTheme.typography.bodySmall,
                        color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.textTertiary,
                        textAlign = TextAlign.Center,
                    )
                    Box(
                        Modifier.fillMaxWidth().height(2.dp)
                            .background(if (isSelected) MaterialTheme.eumColors.textPrimary else Color.Transparent),
                    )
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(EumSpacing.x1).background(MaterialTheme.eumColors.line))
    }
}

// 섹션 사이의 전폭 8dp 구분띠. Figma 띠 색은 노드 값을 확인하지 못해 surfaceSecondary로 가정한다.
@Composable
private fun ProductSectionDivider() {
    Box(Modifier.fillMaxWidth().height(EumSpacing.x8).background(MaterialTheme.eumColors.surfaceSecondary))
}

@Composable
private fun ProductKeyValueRow(row: CatalogKeyValueUiModel, labelWidth: Dp) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Text(row.label, Modifier.width(labelWidth), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
        Text(row.value, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

// Figma 별은 면색과 테두리색이 달라 단색 tint로 못 그린다. 채움 아이콘과 윤곽 아이콘을 겹쳐 2색으로 그린다.
@Composable
private fun ProductRatingStar(isFilled: Boolean, modifier: Modifier = Modifier) {
    Box(modifier.size(28.dp)) {
        Icon(
            painterResource(R.drawable.ic_star_catalog_filled),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = if (isFilled) DETAIL_STAR_FILL_COLOR else MaterialTheme.eumColors.surface,
        )
        Icon(
            painterResource(R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = if (isFilled) MaterialTheme.eumColors.ratingStarOutline else MaterialTheme.eumColors.lineStrong,
        )
    }
}

// Figma 평점 행: 평점의 정수 부분만큼 별을 채우고 나머지는 빈 별로 둔다(4.7이면 4개를 채운다).
@Composable
private fun ProductRatingRow(ratingLabel: String, reviewCountLabel: String?, modifier: Modifier = Modifier) {
    val filledStarCount = ratingLabel.toFloatOrNull()?.toInt()?.coerceIn(0, 5) ?: 0
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            repeat(5) { index -> ProductRatingStar(isFilled = index < filledStarCount) }
        }
        val ratingText = reviewCountLabel?.let { stringResource(R.string.catalog_rating_with_review_count, ratingLabel, it) } ?: ratingLabel
        Text(
            ratingText,
            Modifier.padding(start = EumSpacing.x8),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.eumColors.textTertiary,
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
    val lineColor = MaterialTheme.eumColors.line
    if (isQuoteRequired && !isSoldOut) {
        ProductQuoteBottomActions(uiModel, onEvent, modifier)
        return
    }
    Row(
        modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)
            .drawBehind { drawLine(lineColor, Offset.Zero, Offset(size.width, 0f), strokeWidth = 1.dp.toPx()) }
            // 배경 뒤에 인셋을 적용해 내비게이션 바 구간도 흰색으로 이어진다.
            .navigationBarsPadding()
            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
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
                borderColor = MaterialTheme.eumColors.lineStrong,
                buttonSize = 56.dp,
                iconSize = 24.dp,
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
                borderColor = MaterialTheme.eumColors.lineStrong,
                buttonSize = 56.dp,
                iconSize = 24.dp,
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
    Column(modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface).navigationBarsPadding()) {
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
