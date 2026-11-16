package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.BrandLogoPlate
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogBrandListScreen(
    uiModel: CatalogBrandListUiModel,
    onEvent: (CatalogBrandListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.Scaffold(
        modifier = modifier,
        topBar = {
            CatalogTopBar(
                title = stringResource(R.string.common_categories),
                actions = listOf(CatalogTopBarAction("search", R.drawable.ic_search, stringResource(R.string.common_search))),
                onActionClick = { onEvent(CatalogBrandListEvent.SearchClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).navigationBarsPadding(),
            contentPadding = PaddingValues(bottom = BrandDirectoryBottomPadding),
        ) {
            item {
                BrandModeSegmentedControl(
                    brandModeSelected = uiModel.brandModeSelected,
                    onProductClick = { onEvent(CatalogBrandListEvent.ProductModeSelected) },
                    modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                )
            }
            uiModel.featuredBrand?.let { featured ->
                item {
                    FeaturedBrandBanner(
                        brand = featured,
                        description = uiModel.featuredDescription,
                        onClick = { onEvent(CatalogBrandListEvent.BrandSelected(featured.id)) },
                    )
                }
            }
            item {
                BrandSearchRow(
                    query = uiModel.query,
                    sortAscending = uiModel.sortAscending,
                    onQueryChanged = { onEvent(CatalogBrandListEvent.QueryChanged(it)) },
                    onSortClick = { onEvent(CatalogBrandListEvent.SortToggled) },
                )
            }
            uiModel.letters.forEachIndexed { letterIndex, letter ->
                val brands = uiModel.brandsByLetter[letter].orEmpty()
                item(key = "letter-$letter") {
                    BrandGroupHeader(letter = letter, brandCount = brands.size, isFirstGroup = letterIndex == 0)
                }
                items(brands, key = { it.id }) { brand ->
                    BrandListRow(brand = brand, onClick = { onEvent(CatalogBrandListEvent.BrandSelected(brand.id)) })
                }
            }
        }
    }
}

@Composable
fun CatalogBrandDetailScreen(
    uiModel: CatalogBrandDetailUiModel,
    onEvent: (CatalogBrandDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.Scaffold(
        modifier = modifier,
        topBar = {
            CatalogTopBar(
                title = uiModel.brand.name,
                onBackClick = { onEvent(CatalogBrandDetailEvent.BackClicked) },
                actions = listOf(CatalogTopBarAction("share", R.drawable.ic_share_2, stringResource(R.string.catalog_share))),
                onActionClick = { onEvent(CatalogBrandDetailEvent.ShareClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
    ) { padding ->
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val columns = when {
                maxWidth >= 960.dp -> 4
                maxWidth >= 600.dp -> 3
                else -> 2
            }
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).navigationBarsPadding(),
                contentPadding = PaddingValues(bottom = EumSpacing.x24),
            ) {
                item {
                    BrandDetailIntro(
                        uiModel = uiModel,
                        onWebsiteClick = { onEvent(CatalogBrandDetailEvent.WebsiteClicked) },
                    )
                }
                item {
                    Column(Modifier.padding(start = EumSpacing.x20, end = EumSpacing.x20, top = BrandSectionVerticalPadding)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                stringResource(R.string.catalog_brand_products_count, uiModel.brand.productCount),
                                Modifier.weight(1f),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            Row(
                                Modifier.heightIn(min = BrandTouchTargetSize).clickable { onEvent(CatalogBrandDetailEvent.SortClicked) },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(uiModel.sortLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)
                                Icon(painterResource(R.drawable.ic_chevron_down), null, Modifier.size(16.dp), tint = MaterialTheme.eumColors.textSecondary)
                            }
                        }
                        LazyRow(
                            modifier = Modifier.padding(top = EumSpacing.x16),
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                        ) {
                            items(uiModel.categories) { category ->
                                BrandSelectionChip(category, category == uiModel.selectedCategory) {
                                    onEvent(CatalogBrandDetailEvent.CategorySelected(category))
                                }
                            }
                        }
                    }
                }
                val productRows = uiModel.products.chunked(columns)
                itemsIndexed(productRows) { rowIndex, row ->
                    Row(
                        Modifier.padding(
                            start = EumSpacing.x20,
                            end = EumSpacing.x20,
                            top = EumSpacing.x24,
                            bottom = if (rowIndex == productRows.lastIndex) BrandSectionVerticalPadding else 0.dp,
                        ),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
                    ) {
                        row.forEach { product ->
                            CatalogProductCard(
                                product,
                                onClick = { onEvent(CatalogBrandDetailEvent.ProductSelected(product.id)) },
                                onFavoriteClick = { onEvent(CatalogBrandDetailEvent.FavoriteToggled(product.id)) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
                if (uiModel.otherBrands.isNotEmpty()) {
                    item {
                        // backgroundSubtle이 background와 같아져 surfaceSecondary 띠로 구분한다.
                        Column(
                            Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surfaceSecondary)
                                .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x40),
                            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
                        ) {
                            Text(
                                stringResource(R.string.catalog_other_brands),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.eumColors.textPrimary,
                            )
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.eumColors.surface,
                                border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                                shape = RoundedCornerShape(EumRadius.x12),
                            ) {
                                Column(Modifier.padding(horizontal = EumSpacing.x16)) {
                                    uiModel.otherBrands.forEachIndexed { index, brand ->
                                        BrandNavigationRow(
                                            brand = brand,
                                            onClick = { onEvent(CatalogBrandDetailEvent.OtherBrandSelected(brand.id)) },
                                        )
                                        if (index < uiModel.otherBrands.lastIndex) {
                                            HorizontalDivider(color = MaterialTheme.eumColors.line)
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
}

@Composable
private fun BrandModeSegmentedControl(
    brandModeSelected: Boolean,
    onProductClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val outerShape = RoundedCornerShape(EumRadius.x8)
    Row(
        modifier
            .fillMaxWidth()
            .clip(outerShape)
            .background(MaterialTheme.eumColors.surface)
            .border(1.dp, MaterialTheme.eumColors.textPrimary, outerShape)
            .padding(EumSpacing.x4),
    ) {
        BrandModeSegment(
            label = stringResource(R.string.catalog_product_tab),
            selected = !brandModeSelected,
            onClick = onProductClick,
        )
        BrandModeSegment(
            label = stringResource(R.string.catalog_brand_tab),
            selected = brandModeSelected,
            onClick = {},
        )
    }
}

// 바깥 48dp 박스가 터치와 선택 의미를 맡고, 안쪽 36dp 박스가 시각 세그먼트를 그린다.
@Composable
private fun RowScope.BrandModeSegment(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .weight(1f)
            .heightIn(min = BrandTouchTargetSize)
            .selectable(selected = selected, role = Role.Tab, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(min = BrandSegmentHeight)
                .clip(RoundedCornerShape(EumRadius.x6))
                .background(if (selected) MaterialTheme.eumColors.textPrimary else Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
            )
        }
    }
}

@Composable
private fun FeaturedBrandBanner(brand: CatalogBrandUiModel, description: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.eumColors.textPrimary)
            .clickable(onClick = onClick)
            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x24),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x16),
    ) {
        BrandLogoPlate(brand, plateWidth = 64.dp, plateHeight = 64.dp, logoWidth = 48.dp, logoHeight = 48.dp)
        Column(Modifier.weight(1f)) {
            Text(
                stringResource(R.string.catalog_brand_monthly_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.inverse,
            )
            Text(
                brand.name,
                Modifier.padding(top = EumSpacing.x4),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.eumColors.inverse,
            )
            if (description.isNotBlank()) {
                Text(
                    description,
                    Modifier.padding(top = EumSpacing.x4),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.inverse,
                )
            }
        }
        Icon(painterResource(R.drawable.ic_arrow_right), null, Modifier.size(20.dp), tint = MaterialTheme.eumColors.inverse)
    }
}

@Composable
private fun BrandSearchRow(
    query: String,
    sortAscending: Boolean,
    onQueryChanged: (String) -> Unit,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)) {
        Row(
            Modifier.fillMaxWidth().padding(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x16, bottom = EumSpacing.x12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            // backgroundSubtle이 background와 같아져 surfaceSecondary 채움과 테두리로 구분한다.
            Row(
                Modifier.weight(1f).heightIn(min = BrandToolbarControlSize)
                    .clip(RoundedCornerShape(EumRadius.capsule))
                    .background(MaterialTheme.eumColors.surfaceSecondary)
                    .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.capsule))
                    .padding(horizontal = EumSpacing.x16),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(painterResource(R.drawable.ic_search), null, modifier = Modifier.size(20.dp), tint = MaterialTheme.eumColors.textTertiary)
                BasicTextField(
                    query,
                    onValueChange = onQueryChanged,
                    modifier = Modifier.weight(1f).padding(start = EumSpacing.x8),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.eumColors.textPrimary),
                    decorationBox = { inner ->
                        if (query.isEmpty()) {
                            Text(
                                stringResource(R.string.catalog_brand_list_search_hint),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.eumColors.textTertiary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        inner()
                    },
                )
            }
            // 바깥 48dp 박스가 터치를 맡고, 안쪽 44dp 박스가 시각 버튼을 그린다.
            Box(
                Modifier.size(BrandTouchTargetSize).clickable(role = Role.Button, onClick = onSortClick),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier.size(BrandToolbarControlSize)
                        .border(1.dp, MaterialTheme.eumColors.lineControl, RoundedCornerShape(EumRadius.x8)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painterResource(R.drawable.ic_arrow_up_down),
                        contentDescription = stringResource(
                            if (sortAscending) R.string.catalog_brand_sort_ascending else R.string.catalog_brand_sort_descending,
                        ),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.eumColors.textPrimary,
                    )
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.eumColors.line)
    }
}

@Composable
private fun BrandGroupHeader(letter: String, brandCount: Int, isFirstGroup: Boolean) {
    Column(
        Modifier.fillMaxWidth().padding(
            start = EumSpacing.x20,
            end = EumSpacing.x20,
            top = if (isFirstGroup) EumSpacing.x24 else EumSpacing.x32,
            bottom = EumSpacing.x8,
        ),
    ) {
        Row(Modifier.padding(bottom = EumSpacing.x8), verticalAlignment = Alignment.Bottom) {
            Text(letter, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.eumColors.textPrimary)
            Text(
                stringResource(R.string.catalog_brand_group_count, brandCount),
                Modifier.padding(start = EumSpacing.x8),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textTertiary,
            )
        }
        HorizontalDivider(color = MaterialTheme.eumColors.lineStrong)
    }
}

@Composable
private fun BrandListRow(brand: CatalogBrandUiModel, onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        BrandNavigationRow(brand = brand, onClick = onClick, modifier = Modifier.padding(horizontal = EumSpacing.x20))
        HorizontalDivider(Modifier.padding(horizontal = EumSpacing.x20), color = MaterialTheme.eumColors.line)
    }
}

/** 로고 판 56dp + 브랜드명·상품 수 + 화살표 20dp로 된 72dp 행. 목록과 상세의 다른 브랜드에서 함께 쓴다. */
@Composable
private fun BrandNavigationRow(brand: CatalogBrandUiModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().heightIn(min = BrandRowHeight).clickable(onClick = onClick).padding(vertical = BrandRowVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        BrandLogoPlate(brand, plateWidth = 56.dp, plateHeight = 56.dp, logoWidth = 44.dp, logoHeight = 44.dp)
        Column(Modifier.weight(1f)) {
            Text(brand.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
            Text(
                stringResource(R.string.catalog_brand_products_count, brand.productCount),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textTertiary,
            )
        }
        Icon(painterResource(R.drawable.ic_arrow_right), null, Modifier.size(20.dp), tint = MaterialTheme.eumColors.textSecondary)
    }
}

@Composable
private fun BrandDetailIntro(
    uiModel: CatalogBrandDetailUiModel,
    onWebsiteClick: () -> Unit,
) {
    // 브랜드가 바뀌면 펼침 상태를 초기화하고, 넘침 여부는 레이아웃에서 다시 측정하므로 저장하지 않는다.
    var isStoryExpanded by rememberSaveable(uiModel.brand.id) { mutableStateOf(false) }
    var isStoryOverflowing by remember(uiModel.brand.id) { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Box(
            Modifier.fillMaxWidth().height(BrandHeroHeight).background(MaterialTheme.eumColors.surface),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.figma_brand_detail_banner),
                contentDescription = uiModel.brand.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        Column(
            Modifier.fillMaxWidth().background(MaterialTheme.eumColors.textPrimary)
                .padding(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x24, bottom = EumSpacing.x32),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x20),
        ) {
            BrandLogoPlate(uiModel.brand, plateWidth = 120.dp, plateHeight = 48.dp, logoWidth = 88.dp, logoHeight = 32.dp)
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
                Text(
                    uiModel.brand.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.eumColors.inverse,
                )
                if (uiModel.description.isNotBlank()) {
                    Text(
                        uiModel.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.eumColors.inverse,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20)) {
            if (uiModel.storyTitle.isNotBlank() || uiModel.storyBody.isNotBlank()) {
                Column(Modifier.padding(top = BrandStoryTopPadding), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                    Text(
                        stringResource(R.string.catalog_brand_story_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                    if (uiModel.storyTitle.isNotBlank()) {
                        Text(
                            uiModel.storyTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                    }
                    if (uiModel.storyBody.isNotBlank()) {
                        Text(
                            uiModel.storyBody,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.eumColors.textSecondary,
                            maxLines = if (isStoryExpanded) Int.MAX_VALUE else BRAND_STORY_COLLAPSED_LINES,
                            overflow = TextOverflow.Ellipsis,
                            onTextLayout = { layout ->
                                if (!isStoryExpanded) isStoryOverflowing = layout.hasVisualOverflow
                            },
                        )
                        if (isStoryOverflowing || isStoryExpanded) {
                            val storyStateDescription = stringResource(
                                if (isStoryExpanded) R.string.catalog_brand_story_state_expanded else R.string.catalog_brand_story_state_collapsed,
                            )
                            Row(
                                Modifier.heightIn(min = BrandTouchTargetSize)
                                    .semantics {
                                        stateDescription = storyStateDescription
                                    }
                                    .clickable(role = Role.Button) { isStoryExpanded = !isStoryExpanded },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                            ) {
                                Text(
                                    stringResource(if (isStoryExpanded) R.string.common_fold else R.string.common_see_more),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.eumColors.textPrimary,
                                )
                                Icon(
                                    painterResource(if (isStoryExpanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.eumColors.textPrimary,
                                )
                            }
                        }
                    }
                }
            }
            Column(Modifier.fillMaxWidth().padding(top = EumSpacing.x8)) {
                uiModel.informationRows.forEach { row ->
                    Row(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x12)) {
                        Text(row.label, Modifier.width(BrandFactLabelWidth), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textTertiary)
                        Text(row.value, Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
                    }
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
            }
            Button(onClick = onWebsiteClick, modifier = Modifier.fillMaxWidth().padding(top = EumSpacing.x16)) {
                Text(stringResource(R.string.catalog_brand_website))
            }
        }
    }
}

@Composable
private fun BrandSelectionChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.heightIn(min = BrandChipHeight),
        shape = RoundedCornerShape(EumRadius.capsule),
        color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
        border = BorderStroke(1.dp, if (selected) MaterialTheme.eumColors.line else MaterialTheme.eumColors.lineStrong),
    ) {
        Box(Modifier.heightIn(min = BrandChipHeight).padding(horizontal = BrandChipHorizontalPadding), contentAlignment = Alignment.Center) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
            )
        }
    }
}

// 브랜드 화면 전용 Figma 값. EumSpacing 토큰에 없어 이 파일에 둔다.
private const val BRAND_STORY_COLLAPSED_LINES = 6
private val BrandSegmentHeight = 36.dp
private val BrandToolbarControlSize = 44.dp
private val BrandRowHeight = 72.dp
private val BrandRowVerticalPadding = 7.5.dp
private val BrandDirectoryBottomPadding = 64.dp
private val BrandHeroHeight = 220.dp
private val BrandStoryTopPadding = 48.dp
private val BrandTouchTargetSize = 48.dp
private val BrandFactLabelWidth = 88.dp
private val BrandSectionVerticalPadding = 48.dp
private val BrandChipHeight = 36.dp
private val BrandChipHorizontalPadding = 14.dp
