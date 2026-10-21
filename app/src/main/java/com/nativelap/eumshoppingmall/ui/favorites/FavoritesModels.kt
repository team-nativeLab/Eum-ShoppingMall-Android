package com.nativelap.eumshoppingmall.ui.favorites

import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel

data class FavoritesUiModel(
    val products: List<CatalogProductUiModel> = emptyList(),
    val sortLabel: String = "",
    val isEditing: Boolean = false,
)

sealed interface FavoritesEvent {
    data object BackClicked : FavoritesEvent
    data object EditClicked : FavoritesEvent
    data object SortClicked : FavoritesEvent
    data object BrowseProductsClicked : FavoritesEvent
    data class ProductClicked(val productId: String) : FavoritesEvent
    data class FavoriteToggled(val productId: String) : FavoritesEvent
}
