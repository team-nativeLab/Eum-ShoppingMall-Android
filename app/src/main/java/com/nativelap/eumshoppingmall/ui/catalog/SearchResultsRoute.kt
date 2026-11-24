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
    onInquiryClick: () -> Unit,
    isLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
    onSearchCleared: () -> Unit = {},
    // null이면 카드에 담기 버튼을 그리지 않는다.
    onQuickAddToCart: ((CatalogProductUiModel) -> Unit)? = null,
    cartAddOutcome: CartAddOutcome? = null,
    onCartAddOutcomeChange: (CartAddOutcome?) -> Unit = {},
    onCartClick: () -> Unit = {},
    onCartLoginRequired: () -> Unit = onLoginRequired,
) {
    var editingQuery by rememberSaveable { mutableStateOf(uiModel.query) }
    var selectedFilter by rememberSaveable { mutableStateOf(uiModel.selectedFilter) }
    var isListLayout by rememberSaveable { mutableStateOf(uiModel.isListLayout) }
    val snackbarHostState = rememberCartAddSnackbarHostState(cartAddOutcome, onCartAddOutcomeChange, onCartClick)
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

            is CatalogSearchResultsEvent.QueryChanged -> {
                editingQuery = event.query
                // 검색창을 비우면 결과 화면을 떠나 최근·인기 검색어 화면으로 돌아간다.
                if (isClearedSearchQuery(event.query)) onSearchCleared()
            }

            CatalogSearchResultsEvent.SearchSubmitted -> if (editingQuery.isNotBlank()) onSearch(editingQuery.trim())

            is CatalogSearchResultsEvent.FilterSelected -> {
                selectedFilter = event.filter
                filterState.resetVisibleCount()
            }

            CatalogSearchResultsEvent.LoadMoreClicked -> filterState.loadMore()

            CatalogSearchResultsEvent.LayoutToggled -> isListLayout = !isListLayout

            is CatalogSearchResultsEvent.ProductSelected -> onProductClick(event.productId)

            // 검색 결과가 없을 때의 문의는 특정 상품의 가격 문의가 아니므로 제목을 채우지 않고 1:1 문의로 보낸다.
            CatalogSearchResultsEvent.PriceInquiryClicked -> onInquiryClick()

            // 정렬과 필터는 같은 A-48 화면에서 고른다.
            CatalogSearchResultsEvent.SortClicked,
            CatalogSearchResultsEvent.FilterClicked,
            -> filterState.open()

            is CatalogSearchResultsEvent.FavoriteToggled -> favoriteToggles.toggle(event.productId)

            is CatalogSearchResultsEvent.QuickCartClicked -> {
                if (isLoggedIn) {
                    uiModel.products.firstOrNull { product -> product.id == event.productId }?.let { product ->
                        onQuickAddToCart?.invoke(product)
                    }
                } else {
                    onCartLoginRequired()
                }
            }
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
            sourceProducts = uiModel.products,
        ) {
            // 결과 수는 필터 후 전체 개수이고, 목록에는 현재 페이지 분량만 보여 준다.
            val filteredProducts = filterState.apply(uiModel.products)
            val shownProducts = filterState.visibleProducts(filteredProducts).map { product ->
                product.copy(isFavorite = favoriteToggles.isFavorite(product.id))
            }
            CatalogSearchResultsScreen(
                uiModel = uiModel.copy(
                    query = editingQuery,
                    selectedFilter = selectedFilter,
                    isListLayout = isListLayout,
                    resultCount = filteredProducts.size,
                    sortLabel = productSortLabel(filterState.criteria.sort, uiModel.sortLabel),
                    products = shownProducts,
                ),
                hasMoreProducts = filteredProducts.size > shownProducts.size,
                onEvent = onEvent,
                snackbarHostState = snackbarHostState,
                showQuickCart = onQuickAddToCart != null,
                selectedBottomItem = selectedTab,
                onBottomItemSelected = onTabSelected,
                cartItemCount = cartItemCount,
            )
        }
    }
}
