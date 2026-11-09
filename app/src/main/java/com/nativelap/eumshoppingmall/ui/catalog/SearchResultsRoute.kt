package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

/** 검색 결과(A-06b)와 결과 0건(A-06z)을 같은 목적지에서 결과 유무로 나눠 보여 준다. */
@Composable
fun CatalogSearchResultsRoute(
    uiModel: CatalogSearchResultsUiModel,
    selectedTab: MainNavigationTab,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onBackClick: () -> Unit,
    onPopularSearchClick: () -> Unit,
    onSearch: (query: String) -> Unit,
    onProductClick: (productId: String) -> Unit,
    onQuoteRequestClick: () -> Unit,
) {
    var editingQuery by rememberSaveable { mutableStateOf(uiModel.query) }
    var selectedFilter by rememberSaveable { mutableStateOf(uiModel.selectedFilter) }
    var isListLayout by rememberSaveable { mutableStateOf(uiModel.isListLayout) }
    val onEvent: (CatalogSearchResultsEvent) -> Unit = { event ->
        when (event) {
            CatalogSearchResultsEvent.BackClicked,
            CatalogSearchResultsEvent.Cancelled,
            -> onBackClick()

            CatalogSearchResultsEvent.PopularSearchClicked -> onPopularSearchClick()

            is CatalogSearchResultsEvent.QueryChanged -> editingQuery = event.query

            CatalogSearchResultsEvent.SearchSubmitted -> if (editingQuery.isNotBlank()) onSearch(editingQuery.trim())

            is CatalogSearchResultsEvent.FilterSelected -> selectedFilter = event.filter

            CatalogSearchResultsEvent.LayoutToggled -> isListLayout = !isListLayout

            is CatalogSearchResultsEvent.ProductSelected -> onProductClick(event.productId)

            CatalogSearchResultsEvent.QuoteRequestClicked -> onQuoteRequestClick()

            // 정렬·필터(A-48)와 찜 저장은 데이터 계층이 생긴 뒤 연결한다.
            CatalogSearchResultsEvent.SortClicked,
            CatalogSearchResultsEvent.FilterClicked,
            is CatalogSearchResultsEvent.FavoriteToggled,
            -> Unit
        }
    }
    if (uiModel.products.isEmpty()) {
        CatalogSearchEmptyScreen(
            query = uiModel.query,
            onEvent = onEvent,
            selectedBottomItem = selectedTab,
            onBottomItemSelected = onTabSelected,
            cartItemCount = cartItemCount,
        )
    } else {
        CatalogSearchResultsScreen(
            uiModel = uiModel.copy(
                query = editingQuery,
                selectedFilter = selectedFilter,
                isListLayout = isListLayout,
            ),
            onEvent = onEvent,
            selectedBottomItem = selectedTab,
            onBottomItemSelected = onTabSelected,
            cartItemCount = cartItemCount,
        )
    }
}
