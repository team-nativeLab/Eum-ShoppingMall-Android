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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
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
            contentPadding = PaddingValues(bottom = EumSpacing.x24),
        ) {
            item {
                BrandModeSegmentedControl(
                    brandModeSelected = uiModel.brandModeSelected,
                    onProductClick = { onEvent(CatalogBrandListEvent.ProductModeSelected) },
                    modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
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
                    modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
                )
            }
            uiModel.letters.forEach { letter ->
                val brands = uiModel.brandsByLetter[letter].orEmpty()
                item(key = "letter-$letter") {
                    BrandGroupHeader(letter = letter, brandCount = brands.size)
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
                    Column(Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                stringResource(R.string.catalog_brand_products_count, uiModel.brand.productCount),
                                Modifier.weight(1f),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Row(
                                Modifier.heightIn(min = 48.dp).clickable { onEvent(CatalogBrandDetailEvent.SortClicked) },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(uiModel.sortLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)
                                Icon(painterResource(R.drawable.ic_chevron_down), null, Modifier.size(16.dp), tint = MaterialTheme.eumColors.textSecondary)
                            }
                        }
                        LazyRow(
                            modifier = Modifier.padding(top = EumSpacing.x8),
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
                items(uiModel.products.chunked(columns)) { row ->
                    Row(Modifier.padding(horizontal = EumSpacing.x20), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
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
                        Text(
                            stringResource(R.string.catalog_other_brands),
                            Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    items(uiModel.otherBrands) { brand ->
                        Surface(
                            onClick = { onEvent(CatalogBrandDetailEvent.OtherBrandSelected(brand.id)) },
                            modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x4).fillMaxWidth(),
                            color = MaterialTheme.eumColors.background,
                            border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
                            shape = RoundedCornerShape(EumRadius.x8),
                        ) {
                            Row(Modifier.padding(EumSpacing.x12), verticalAlignment = Alignment.CenterVertically) {
                                CatalogBrandImage(brand, Modifier.size(40.dp))
                                Column(Modifier.weight(1f).padding(start = EumSpacing.x12)) {
                                    Text(brand.name, style = MaterialTheme.typography.labelLarge)
                                    Text(
                                        stringResource(R.string.catalog_brand_products_count, brand.productCount),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.eumColors.textTertiary,
                                    )
                                }
                                Icon(painterResource(R.drawable.ic_arrow_right), null, tint = MaterialTheme.eumColors.textSecondary)
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
    Row(
        modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.eumColors.textPrimary, RoundedCornerShape(EumRadius.capsule))
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

@Composable
private fun RowScope.BrandModeSegment(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .weight(1f)
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(EumRadius.capsule))
            .background(if (selected) MaterialTheme.eumColors.textPrimary else Color.Transparent)
            .clickable(role = Role.Tab, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary,
        )
    }
}

@Composable
private fun FeaturedBrandBanner(brand: CatalogBrandUiModel, description: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.eumColors.textPrimary)
            .clickable(onClick = onClick)
            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x16),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CatalogBrandImage(brand, Modifier.size(64.dp))
        Column(Modifier.weight(1f).padding(start = EumSpacing.x16)) {
            Text(
                stringResource(R.string.catalog_brand_monthly_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.inverse,
            )
            Text(
                brand.name,
                Modifier.padding(top = EumSpacing.x4),
                style = MaterialTheme.typography.titleMedium,
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
        Icon(painterResource(R.drawable.ic_arrow_right), null, tint = MaterialTheme.eumColors.inverse)
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
    Row(modifier.fillMaxWidth().heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically) {
        Row(
            Modifier.weight(1f).clip(RoundedCornerShape(EumRadius.capsule))
                .background(MaterialTheme.eumColors.backgroundSubtle).padding(horizontal = EumSpacing.x16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(R.drawable.ic_search), null, modifier = Modifier.size(20.dp), tint = MaterialTheme.eumColors.textTertiary)
            BasicTextField(
                query,
                onValueChange = onQueryChanged,
                modifier = Modifier.weight(1f).heightIn(min = 48.dp).padding(start = EumSpacing.x8),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.eumColors.textPrimary),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text(
                            stringResource(R.string.catalog_brand_list_search_hint),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.eumColors.textTertiary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    inner()
                },
            )
        }
        Box(
            Modifier.padding(start = EumSpacing.x12).size(48.dp)
                .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x8))
                .clickable(onClick = onSortClick),
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

@Composable
private fun BrandGroupHeader(letter: String, brandCount: Int) {
    Column(Modifier.fillMaxWidth().padding(start = EumSpacing.x20, end = EumSpacing.x20, top = EumSpacing.x24)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(letter, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.eumColors.textPrimary)
            Text(
                stringResource(R.string.catalog_brand_group_count, brandCount),
                Modifier.padding(start = EumSpacing.x8),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textTertiary,
            )
        }
        HorizontalDivider(Modifier.padding(top = EumSpacing.x8), color = MaterialTheme.eumColors.textPrimary)
    }
}

@Composable
private fun BrandListRow(brand: CatalogBrandUiModel, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 64.dp).clickable(onClick = onClick)
            .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CatalogBrandImage(brand, Modifier.size(44.dp))
        Column(Modifier.weight(1f).padding(start = EumSpacing.x16)) {
            Text(brand.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.eumColors.textPrimary)
            Text(
                stringResource(R.string.catalog_brand_products_count, brand.productCount),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.eumColors.textTertiary,
            )
        }
        Icon(painterResource(R.drawable.ic_arrow_right), null, tint = MaterialTheme.eumColors.textSecondary)
    }
}

@Composable
private fun BrandDetailIntro(
    uiModel: CatalogBrandDetailUiModel,
    onWebsiteClick: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Box(
            Modifier.fillMaxWidth().aspectRatio(1.8f).background(MaterialTheme.eumColors.surface),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.figma_brand_detail_banner),
                contentDescription = uiModel.brand.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.textPrimary).padding(EumSpacing.x20)) {
            CatalogBrandImage(uiModel.brand, Modifier.size(width = 88.dp, height = 48.dp))
            Text(
                uiModel.brand.name,
                Modifier.padding(top = EumSpacing.x16),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.eumColors.inverse,
            )
            if (uiModel.description.isNotBlank()) {
                Text(
                    uiModel.description,
                    Modifier.padding(top = EumSpacing.x8),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.inverse,
                )
            }
        }
        Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x24)) {
            if (uiModel.storyTitle.isNotBlank() || uiModel.storyBody.isNotBlank()) {
                Text(
                    stringResource(R.string.catalog_brand_story_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.eumColors.textTertiary,
                )
                if (uiModel.storyTitle.isNotBlank()) {
                    Text(
                        uiModel.storyTitle,
                        Modifier.padding(top = EumSpacing.x8),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                }
                if (uiModel.storyBody.isNotBlank()) {
                    Text(
                        uiModel.storyBody,
                        Modifier.padding(top = EumSpacing.x12),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
            }
            Column(Modifier.fillMaxWidth().padding(top = EumSpacing.x24)) {
                uiModel.informationRows.forEach { row ->
                    Row(Modifier.fillMaxWidth().padding(vertical = EumSpacing.x12)) {
                        Text(row.label, Modifier.weight(0.35f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textTertiary)
                        Text(row.value, Modifier.weight(0.65f), style = MaterialTheme.typography.bodySmall)
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
        modifier = Modifier.heightIn(min = 48.dp),
        shape = RoundedCornerShape(EumRadius.capsule),
        color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.eumColors.lineControl),
    ) {
        Box(Modifier.padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8), contentAlignment = Alignment.Center) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
            )
        }
    }
}
