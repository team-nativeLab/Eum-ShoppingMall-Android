package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductCard
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogProductRow
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogScreenScaffold
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBarAction
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogProductListScreen(
    uiModel: CatalogProductListUiModel,
    onEvent: (CatalogProductListEvent) -> Unit,
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
                title = uiModel.title,
                onBackClick = { onEvent(CatalogProductListEvent.BackClicked) },
                actions = listOf(CatalogTopBarAction("search", R.drawable.ic_search, stringResource(R.string.common_search))),
                onActionClick = { onEvent(CatalogProductListEvent.SearchClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        content = { contentModifier ->
            BoxWithConstraints(contentModifier) {
                val columns = when {
                    maxWidth >= 960.dp -> 4
                    maxWidth >= 600.dp -> 3
                    else -> 2
                }
                Column(Modifier.fillMaxSize()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        items(uiModel.categories) { category ->
                            val selected = category == uiModel.selectedCategory
                            CatalogSelectionChip(category, selected) {
                                onEvent(CatalogProductListEvent.CategorySelected(category))
                            }
                        }
                    }
                    CatalogProductListControls(
                        countLabel = uiModel.resultCountLabel,
                        sortLabel = uiModel.sortLabel,
                        isListLayout = uiModel.isListLayout,
                        onSortClick = { onEvent(CatalogProductListEvent.SortClicked) },
                        onFilterClick = { onEvent(CatalogProductListEvent.FilterClicked) },
                        onLayoutClick = { onEvent(CatalogProductListEvent.LayoutToggled) },
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = EumSpacing.x20,
                            top = EumSpacing.x8,
                            end = EumSpacing.x20,
                            bottom = EumSpacing.x20,
                        ),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
                    ) {
                        if (uiModel.products.isEmpty()) {
                            item {
                                Text(
                                    stringResource(R.string.catalog_empty_products),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x32),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.eumColors.textTertiary,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        } else if (uiModel.isListLayout) {
                            items(uiModel.products, key = { it.id }) { product ->
                                CatalogProductRow(
                                    product = product,
                                    onClick = { onEvent(CatalogProductListEvent.ProductSelected(product.id)) },
                                    onFavoriteClick = { onEvent(CatalogProductListEvent.FavoriteToggled(product.id)) },
                                )
                            }
                        } else {
                            items(uiModel.products.chunked(columns)) { rowProducts ->
                                Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                    rowProducts.forEach { product ->
                                        CatalogProductCard(
                                            product = product,
                                            onClick = { onEvent(CatalogProductListEvent.ProductSelected(product.id)) },
                                            onFavoriteClick = { onEvent(CatalogProductListEvent.FavoriteToggled(product.id)) },
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
        },
    )
}

@Composable
fun CatalogSelectionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(EumRadius.capsule),
        color = if (selected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.background,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.eumColors.lineControl),
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8),
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
        )
    }
}

@Composable
internal fun CatalogProductListControls(
    countLabel: String?,
    sortLabel: String,
    isListLayout: Boolean,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
    onLayoutClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        Surface(
            onClick = onSortClick,
            shape = RoundedCornerShape(EumRadius.capsule),
            color = MaterialTheme.eumColors.background,
            border = BorderStroke(1.dp, MaterialTheme.eumColors.textPrimary),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x6),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
            ) {
                Text(sortLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textPrimary)
                Icon(painterResource(R.drawable.ic_chevron_down), null, modifier = Modifier.size(16.dp))
            }
        }
        countLabel?.let {
            Text(it, modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
        } ?: Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier.heightIn(min = 48.dp).clickable(onClick = onFilterClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            Icon(painterResource(R.drawable.ic_sliders_horizontal), null, modifier = Modifier.size(20.dp))
            Text(stringResource(R.string.common_filter), style = MaterialTheme.typography.labelMedium)
        }
        IconButton(onClick = onLayoutClick, modifier = Modifier.size(48.dp)) {
            Icon(
                painterResource(if (isListLayout) R.drawable.ic_layout_list else R.drawable.ic_layout_grid),
                contentDescription = stringResource(R.string.catalog_toggle_layout),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
