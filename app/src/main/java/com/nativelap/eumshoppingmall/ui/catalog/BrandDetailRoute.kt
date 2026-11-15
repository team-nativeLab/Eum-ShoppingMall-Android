package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.common.rememberFavoriteToggles

/**
 * 브랜드 상세(A-34). 공유·브랜드 웹사이트·정렬은 외부 앱·정렬 UI 연동 전이라 연결하지 않는다.
 * 찜은 로그인이 필요하므로 비로그인이면 [onLoginRequired]를 부른다.
 */
@Composable
fun CatalogBrandDetailRoute(
    uiModel: CatalogBrandDetailUiModel,
    onBackClick: () -> Unit,
    onProductClick: (productId: String) -> Unit,
    onOtherBrandClick: (brandId: String) -> Unit,
    onViewAllBrandsClick: () -> Unit,
    isLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
) {
    var selectedCategory by rememberSaveable { mutableStateOf(uiModel.selectedCategory) }
    val favoriteToggles = rememberFavoriteToggles(
        initialFavoriteIds = uiModel.products.filter { product -> product.isFavorite }.map { product -> product.id }.toSet(),
        isLoggedIn = isLoggedIn,
        onLoginRequired = onLoginRequired,
    )
    CatalogBrandDetailScreen(
        uiModel = uiModel.copy(
            selectedCategory = selectedCategory,
            products = uiModel.products.map { product -> product.copy(isFavorite = favoriteToggles.isFavorite(product.id)) },
        ),
        onEvent = { event ->
            when (event) {
                CatalogBrandDetailEvent.BackClicked -> onBackClick()

                is CatalogBrandDetailEvent.CategorySelected -> selectedCategory = event.category

                is CatalogBrandDetailEvent.ProductSelected -> onProductClick(event.productId)

                is CatalogBrandDetailEvent.FavoriteToggled -> favoriteToggles.toggle(event.productId)

                is CatalogBrandDetailEvent.OtherBrandSelected -> onOtherBrandClick(event.brandId)

                CatalogBrandDetailEvent.ViewAllBrandsClicked -> onViewAllBrandsClick()

                CatalogBrandDetailEvent.ShareClicked,
                CatalogBrandDetailEvent.WebsiteClicked,
                CatalogBrandDetailEvent.SortClicked,
                -> Unit
            }
        },
    )
}
