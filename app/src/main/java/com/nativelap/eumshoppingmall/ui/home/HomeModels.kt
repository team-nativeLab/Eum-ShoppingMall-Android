package com.nativelap.eumshoppingmall.ui.home

import androidx.annotation.DrawableRes

data class HomeProductUiModel(
    val id: String,
    val name: String,
    val priceLabel: String? = null,
    val ratingLabel: String? = null,
    val ratingValue: Float? = null,
    val originalPriceLabel: String? = null,
    val discountRateLabel: String? = null,
    @param:DrawableRes val imageResId: Int? = null,
    val imageDescription: String? = null,
    val isSoldOut: Boolean = false,
    val isPurchaseUnavailable: Boolean = false,
)

enum class HomeNavigationItem {
    Home,
    Categories,
    Search,
    Cart,
    MyPage,
}

data class HomeCategoryUiModel(
    val label: String,
    @param:DrawableRes val iconResId: Int,
)

sealed interface HomeScreenEvent {
    data class QueryChanged(val query: String) : HomeScreenEvent
    data object SearchSubmitted : HomeScreenEvent
    data class CategoryClicked(val category: HomeCategoryUiModel) : HomeScreenEvent
    data class ProductClicked(val product: HomeProductUiModel) : HomeScreenEvent
    data class HeroActionClicked(val slideIndex: Int) : HomeScreenEvent
    data object QuoteClicked : HomeScreenEvent
    data object MenuClicked : HomeScreenEvent
    data object NotificationsClicked : HomeScreenEvent
    data object SeeAllProductsClicked : HomeScreenEvent
    data object SeeRecommendedProductsClicked : HomeScreenEvent
    data class FavoriteClicked(val product: HomeProductUiModel) : HomeScreenEvent
    data class NavigationSelected(val item: HomeNavigationItem) : HomeScreenEvent
}
