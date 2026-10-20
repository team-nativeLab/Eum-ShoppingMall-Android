package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CatalogSearchResultsScreen(
    uiModel: CatalogSearchResultsUiModel,
    onEvent: (CatalogSearchResultsEvent) -> Unit,
    onBottomItemSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CatalogScreenScaffold(
        selectedBottomItem = "search",
        onBottomItemSelected = onBottomItemSelected,
        modifier = modifier,
        topBar = {
            CatalogSearchHeader(
                query = uiModel.query,
                onQueryChanged = { onEvent(CatalogSearchResultsEvent.QueryChanged(it)) },
                onSubmit = { onEvent(CatalogSearchResultsEvent.SearchSubmitted) },
                onCancel = { onEvent(CatalogSearchResultsEvent.Cancelled) },
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
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        uiModel.filters.forEach { filter ->
                            CatalogSelectionChip(filter, filter == uiModel.selectedFilter) {
                                onEvent(CatalogSearchResultsEvent.FilterSelected(filter))
                            }
                        }
                    }
                    CatalogProductListControls(
                        countLabel = stringResource(R.string.catalog_search_result_count, uiModel.resultCount),
                        sortLabel = uiModel.sortLabel,
                        isListLayout = uiModel.isListLayout,
                        onSortClick = { onEvent(CatalogSearchResultsEvent.SortClicked) },
                        onFilterClick = { onEvent(CatalogSearchResultsEvent.FilterClicked) },
                        onLayoutClick = { onEvent(CatalogSearchResultsEvent.LayoutToggled) },
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
                    ) {
                        if (uiModel.isListLayout) {
                            items(uiModel.products, key = { it.id }) { product ->
                                CatalogProductRow(
                                    product = product,
                                    onClick = { onEvent(CatalogSearchResultsEvent.ProductSelected(product.id)) },
                                    onFavoriteClick = { onEvent(CatalogSearchResultsEvent.FavoriteToggled(product.id)) },
                                )
                            }
                        } else {
                            items(uiModel.products.chunked(columns), key = { row -> row.joinToString { it.id } }) { row ->
                                Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                                    row.forEach { product ->
                                        CatalogProductCard(
                                            product = product,
                                            onClick = { onEvent(CatalogSearchResultsEvent.ProductSelected(product.id)) },
                                            onFavoriteClick = { onEvent(CatalogSearchResultsEvent.FavoriteToggled(product.id)) },
                                            modifier = Modifier.weight(1f),
                                        )
                                    }
                                    if (row.size == 1) androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
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
fun CatalogSearchEmptyScreen(
    query: String,
    popularSearchLabel: String,
    onEvent: (CatalogSearchResultsEvent) -> Unit,
    onBottomItemSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CatalogScreenScaffold(
        selectedBottomItem = "search",
        onBottomItemSelected = onBottomItemSelected,
        modifier = modifier,
        topBar = {
            CatalogTopBar(
                title = query,
                onBackClick = { onEvent(CatalogSearchResultsEvent.BackClicked) },
                modifier = Modifier.statusBarsPadding(),
            )
        },
        content = { contentModifier ->
            Column(
                modifier = contentModifier.fillMaxSize().padding(horizontal = EumSpacing.x24),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painterResource(R.drawable.ic_search_x),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.eumColors.textTertiary,
                )
                Text(
                    stringResource(R.string.catalog_search_empty_title),
                    modifier = Modifier.padding(top = EumSpacing.x20),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    stringResource(R.string.catalog_search_empty_description, query),
                    modifier = Modifier.padding(top = EumSpacing.x8),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textTertiary,
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier.padding(top = EumSpacing.x24).heightIn(min = 48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(R.string.catalog_popular_search_prefix), style = MaterialTheme.typography.labelMedium)
                    Text(
                        popularSearchLabel,
                        modifier = Modifier.padding(start = EumSpacing.x8),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
                androidx.compose.material3.OutlinedButton(
                    onClick = { onEvent(CatalogSearchResultsEvent.PopularSearchClicked) },
                    modifier = Modifier.fillMaxWidth().padding(top = EumSpacing.x16),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(stringResource(R.string.catalog_popular_search_button))
                }
                androidx.compose.material3.Button(
                    onClick = { onEvent(CatalogSearchResultsEvent.QuoteRequestClicked) },
                    modifier = Modifier.fillMaxWidth().padding(top = EumSpacing.x8),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(stringResource(R.string.catalog_quote_request))
                }
            }
        },
    )
}
