package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
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
                    // Figma(180:1511)처럼 칩 줄은 가로로 스크롤되어 넘치는 칩이 잘려 보이게 한다.
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = EumSpacing.x20),
                        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                    ) {
                        items(uiModel.filters) { filter ->
                            CatalogSelectionChip(filter, filter == uiModel.selectedFilter) {
                                onEvent(CatalogSearchResultsEvent.FilterSelected(filter))
                            }
                        }
                    }
                    CatalogSearchResultHeader(
                        query = uiModel.query,
                        resultCount = uiModel.resultCount,
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

// A-06b 결과 헤더 Figma(180:1511): 결과 수는 왼쪽, 정렬·필터·보기 전환은 오른쪽 인라인 버튼이다.
// 정렬 pill은 공용 CatalogProductListControls에 묶여 있어 이 화면에서만 헤더 행을 따로 구성한다.
@Composable
private fun CatalogSearchResultHeader(
    query: String,
    resultCount: Int,
    sortLabel: String,
    isListLayout: Boolean,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
    onLayoutClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = EumSpacing.x20, top = EumSpacing.x20, end = EumSpacing.x20),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(R.string.catalog_search_result_header, query, resultCount),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.eumColors.textSecondary,
        )
        Row(
            modifier = Modifier.heightIn(min = 48.dp).clickable(onClick = onSortClick).padding(horizontal = EumSpacing.x4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            Icon(painterResource(R.drawable.ic_arrow_up_down), null, modifier = Modifier.size(16.dp), tint = MaterialTheme.eumColors.textPrimary)
            Text(sortLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
        }
        Row(
            modifier = Modifier.heightIn(min = 48.dp).clickable(onClick = onFilterClick).padding(horizontal = EumSpacing.x4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4),
        ) {
            Icon(painterResource(R.drawable.ic_sliders_horizontal), null, modifier = Modifier.size(20.dp), tint = MaterialTheme.eumColors.textPrimary)
            Text(stringResource(R.string.common_filter), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textPrimary)
        }
        IconButton(onClick = onLayoutClick, modifier = Modifier.size(48.dp)) {
            Icon(
                painterResource(if (isListLayout) R.drawable.ic_layout_list else R.drawable.ic_layout_grid),
                contentDescription = stringResource(R.string.catalog_toggle_layout),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.eumColors.textPrimary,
            )
        }
    }
}

// A-06z 검색 결과 없음 Figma(180:1681): 가운데 안내와 하단 견적 문의 카드 순서다. 헤더는 기존 CatalogTopBar를 유지한다.
// 인기 검색어 행은 Figma에 없어 제거했고, 인기 검색어 보기 버튼이 같은 동작을 맡는다.
@Composable
fun CatalogSearchEmptyScreen(
    query: String,
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
                modifier = contentModifier.fillMaxSize().padding(horizontal = EumSpacing.x20),
            ) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(MaterialTheme.eumColors.backgroundSubtle, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_search_x),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.eumColors.textSecondary,
                        )
                    }
                    Text(
                        stringResource(R.string.catalog_search_empty_result_title, query),
                        modifier = Modifier.padding(top = EumSpacing.x20),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        stringResource(R.string.catalog_search_empty_result_guide),
                        modifier = Modifier.padding(top = EumSpacing.x8),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                    OutlinedButton(
                        onClick = { onEvent(CatalogSearchResultsEvent.PopularSearchClicked) },
                        modifier = Modifier.padding(top = EumSpacing.x24).widthIn(min = 160.dp),
                        shape = RoundedCornerShape(EumRadius.x8),
                    ) {
                        Text(stringResource(R.string.catalog_popular_search_button))
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = EumSpacing.x24)
                        .border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12))
                        .padding(EumSpacing.x20),
                ) {
                    Text(
                        stringResource(R.string.catalog_search_quote_card_title),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        stringResource(R.string.catalog_search_quote_card_description),
                        modifier = Modifier.padding(top = EumSpacing.x4),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                    OutlinedButton(
                        onClick = { onEvent(CatalogSearchResultsEvent.QuoteRequestClicked) },
                        modifier = Modifier.fillMaxWidth().padding(top = EumSpacing.x16),
                        shape = RoundedCornerShape(EumRadius.x8),
                    ) {
                        Text(stringResource(R.string.catalog_quote_request))
                    }
                }
            }
        },
    )
}
