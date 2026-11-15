package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.common.rememberFavoriteToggles

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
    isLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
) {
    var editingQuery by rememberSaveable { mutableStateOf(uiModel.query) }
    var selectedFilter by rememberSaveable { mutableStateOf(uiModel.selectedFilter) }
    var isListLayout by rememberSaveable { mutableStateOf(uiModel.isListLayout) }
    val filterState = rememberProductFilterState()
    val favoriteToggles = rememberFavoriteToggles(
        initialFavoriteIds = uiModel.products.filter { product -> product.isFavorite }.map { product -> product.id }.toSet(),
        isLoggedIn = isLoggedIn,
        onLoginRequired = onLoginRequired,
    )
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

            // 정렬과 필터는 같은 A-48 화면에서 고른다.
            CatalogSearchResultsEvent.SortClicked,
            CatalogSearchResultsEvent.FilterClicked,
            -> filterState.open()

            is CatalogSearchResultsEvent.FavoriteToggled -> favoriteToggles.toggle(event.productId)
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
        ProductFilterHost(
            state = filterState,
            categories = uiModel.filters,
            selectedCategory = selectedFilter,
            onCategorySelected = { filter -> selectedFilter = filter },
        ) {
            CatalogSearchResultsScreen(
                uiModel = uiModel.copy(
                    query = editingQuery,
                    selectedFilter = selectedFilter,
                    isListLayout = isListLayout,
                    sortLabel = productSortLabel(filterState.sort, uiModel.sortLabel),
                    products = filterState.apply(uiModel.products).map { product ->
                        product.copy(isFavorite = favoriteToggles.isFavorite(product.id))
                    },
                ),
                onEvent = onEvent,
                selectedBottomItem = selectedTab,
                onBottomItemSelected = onTabSelected,
                cartItemCount = cartItemCount,
            )
        }
    }
}
