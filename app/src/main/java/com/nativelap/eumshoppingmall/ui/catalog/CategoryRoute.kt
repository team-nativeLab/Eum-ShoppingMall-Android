package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

@Composable
fun CatalogCategoryRoute(
    uiModel: CatalogCategoryScreenUiModel,
    selectedTab: MainNavigationTab,
    cartItemCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (categoryId: String?) -> Unit,
) {
    var isBrandModeSelected by rememberSaveable { mutableStateOf(uiModel.brandModeSelected) }
    CatalogCategoryScreen(
        uiModel = uiModel.copy(brandModeSelected = isBrandModeSelected),
        selectedBottomItem = selectedTab,
        onBottomItemSelected = onTabSelected,
        cartItemCount = cartItemCount,
        onEvent = { event ->
            when (event) {
                CatalogCategoryEvent.SearchClicked -> onSearchClick()

                CatalogCategoryEvent.ViewAllClicked -> onCategoryClick(null)

                is CatalogCategoryEvent.ModeSelected -> isBrandModeSelected = event.brandMode

                is CatalogCategoryEvent.CategorySelected -> onCategoryClick(event.categoryId)

                // 브랜드 상세(A-34) 연결은 이번 범위가 아니다.
                is CatalogCategoryEvent.BrandSelected -> Unit
            }
        },
    )
}
