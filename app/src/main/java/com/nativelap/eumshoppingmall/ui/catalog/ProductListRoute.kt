package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import com.nativelap.eumshoppingmall.ui.common.rememberFavoriteToggles

@Composable
fun CatalogProductListRoute(
    uiModel: CatalogProductListUiModel,
    selectedTab: MainNavigationTab,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProductClick: (productId: String) -> Unit,
    isLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
) {
    var selectedCategory by rememberSaveable { mutableStateOf(uiModel.selectedCategory) }
    var isListLayout by rememberSaveable { mutableStateOf(uiModel.isListLayout) }
    val filterState = rememberProductFilterState()
    val favoriteToggles = rememberFavoriteToggles(
        initialFavoriteIds = uiModel.products.filter { product -> product.isFavorite }.map { product -> product.id }.toSet(),
        isLoggedIn = isLoggedIn,
        onLoginRequired = onLoginRequired,
    )
    ProductFilterHost(
        state = filterState,
        categories = uiModel.categories,
        selectedCategory = selectedCategory,
        onCategorySelected = { category -> selectedCategory = category },
    ) {
        CatalogProductListScreen(
            uiModel = uiModel.copy(
                selectedCategory = selectedCategory,
                isListLayout = isListLayout,
                sortLabel = productSortLabel(filterState.sort, uiModel.sortLabel),
                products = filterState.apply(uiModel.products).map { product ->
                    product.copy(isFavorite = favoriteToggles.isFavorite(product.id))
                },
            ),
            selectedBottomItem = selectedTab,
            onBottomItemSelected = onTabSelected,
            cartItemCount = cartItemCount,
            onEvent = { event ->
                when (event) {
                    CatalogProductListEvent.BackClicked -> onBackClick()

                    CatalogProductListEvent.SearchClicked -> onSearchClick()

                    is CatalogProductListEvent.CategorySelected -> selectedCategory = event.category

                    CatalogProductListEvent.LayoutToggled -> isListLayout = !isListLayout

                    is CatalogProductListEvent.ProductSelected -> onProductClick(event.productId)

                    // 정렬과 필터는 같은 A-48 화면에서 고른다.
                    CatalogProductListEvent.SortClicked,
                    CatalogProductListEvent.FilterClicked,
                    -> filterState.open()

                    is CatalogProductListEvent.FavoriteToggled -> favoriteToggles.toggle(event.productId)
                }
            },
        )
    }
}
