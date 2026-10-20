package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogScreenScaffold
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogCategoryScreen(
    uiModel: CatalogCategoryScreenUiModel,
    onEvent: (CatalogCategoryEvent) -> Unit,
    selectedBottomItem: String = "categories",
    onBottomItemSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CatalogScreenScaffold(
        selectedBottomItem = selectedBottomItem,
        onBottomItemSelected = onBottomItemSelected,
        modifier = modifier,
        topBar = {
            CatalogTopBar(
                title = stringResource(R.string.common_categories),
                actions = listOf(
                    CatalogTopBarAction("search", R.drawable.ic_search, stringResource(R.string.common_search)),
                ),
                onActionClick = { onEvent(CatalogCategoryEvent.SearchClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        content = { contentModifier ->
            Column(contentModifier) {
                CategoryModeSelector(
                    isBrandSelected = uiModel.brandModeSelected,
                    onSelect = { onEvent(CatalogCategoryEvent.ModeSelected(it)) },
                    modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x20),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .clickable { onEvent(CatalogCategoryEvent.ViewAllClicked) }
                        .padding(horizontal = EumSpacing.x20),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        stringResource(R.string.catalog_view_all_products),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                    Icon(
                        painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = MaterialTheme.eumColors.textTertiary,
                    )
                }
                Box(
                    Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20)
                        .height(1.dp).background(MaterialTheme.eumColors.line),
                )
                if (uiModel.brandModeSelected) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = EumSpacing.x20,
                            vertical = EumSpacing.x12,
                        ),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        items(uiModel.brands, key = { it.id }) { brand ->
                            CategoryBrandRow(brand) { onEvent(CatalogCategoryEvent.BrandSelected(brand.id)) }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = EumSpacing.x20,
                            vertical = EumSpacing.x12,
                        ),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                    ) {
                        items(uiModel.categories, key = { it.id }) { category ->
                            CategoryRow(category) { onEvent(CatalogCategoryEvent.CategorySelected(category.id)) }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun CategoryModeSelector(
    isBrandSelected: Boolean,
    onSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.eumColors.textPrimary, RoundedCornerShape(EumRadius.x8))
            .padding(EumSpacing.x4),
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
    ) {
        CategoryModeItem(
            label = stringResource(R.string.catalog_product_tab),
            selected = !isBrandSelected,
            onClick = { onSelect(false) },
            modifier = Modifier.weight(1f),
        )
        CategoryModeItem(
            label = stringResource(R.string.catalog_brand_tab),
            selected = isBrandSelected,
            onClick = { onSelect(true) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CategoryModeItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 36.dp),
        shape = RoundedCornerShape(EumRadius.x6),
        color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
            )
        }
    }
}

@Composable
private fun CategoryRow(category: CatalogCategoryUiModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp).clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(EumRadius.x8))
                .background(MaterialTheme.eumColors.backgroundSubtle)
                .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x8)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painterResource(category.iconResId),
                contentDescription = null,
                tint = MaterialTheme.eumColors.textPrimary,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            category.label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.eumColors.textPrimary,
        )
        Icon(
            painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MaterialTheme.eumColors.textTertiary,
        )
    }
}

@Composable
private fun CategoryBrandRow(brand: CatalogBrandUiModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp).clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        CatalogBrandImage(brand, Modifier.size(44.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x2)) {
            Text(brand.name, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.eumColors.textPrimary)
            Text(brand.productCountLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
        }
        Icon(painterResource(R.drawable.ic_chevron_right), null, tint = MaterialTheme.eumColors.textTertiary)
    }
}

@Composable
fun CatalogBrandImage(brand: CatalogBrandUiModel, modifier: Modifier = Modifier) {
    val imageResId = brand.imageResId
    Box(
        modifier = modifier.clip(RoundedCornerShape(EumRadius.x6))
            .background(MaterialTheme.eumColors.surface)
            .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x6)),
        contentAlignment = Alignment.Center,
    ) {
        if (imageResId == null) {
            Icon(
                painterResource(R.drawable.ic_image),
                contentDescription = stringResource(R.string.catalog_brand_image_placeholder),
                tint = MaterialTheme.eumColors.placeholderGlyph,
                modifier = Modifier.size(20.dp),
            )
        } else {
            androidx.compose.foundation.Image(
                painterResource(imageResId),
                contentDescription = brand.imageDescription ?: brand.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            )
        }
    }
}
