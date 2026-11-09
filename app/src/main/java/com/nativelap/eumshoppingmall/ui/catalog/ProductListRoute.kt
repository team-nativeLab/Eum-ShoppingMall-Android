package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

@Composable
fun CatalogProductListRoute(
    uiModel: CatalogProductListUiModel,
    selectedTab: MainNavigationTab,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProductClick: (productId: String) -> Unit,
) {
    var selectedCategory by rememberSaveable { mutableStateOf(uiModel.selectedCategory) }
    var isListLayout by rememberSaveable { mutableStateOf(uiModel.isListLayout) }
    CatalogProductListScreen(
        uiModel = uiModel.copy(
            selectedCategory = selectedCategory,
            isListLayout = isListLayout,
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

                // 정렬·필터(A-48)와 찜 저장은 데이터 계층이 생긴 뒤 연결한다.
                CatalogProductListEvent.SortClicked,
                CatalogProductListEvent.FilterClicked,
                is CatalogProductListEvent.FavoriteToggled,
                -> Unit
            }
        },
    )
}
