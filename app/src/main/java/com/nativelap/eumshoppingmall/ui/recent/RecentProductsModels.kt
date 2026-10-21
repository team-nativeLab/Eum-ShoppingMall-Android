package com.nativelap.eumshoppingmall.ui.recent

import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel

data class RecentProductGroupUiModel(
    val dateLabel: String,
    val products: List<CatalogProductUiModel>,
)

data class RecentProductsUiModel(
    val groups: List<RecentProductGroupUiModel> = emptyList(),
    val retentionNotice: String = "",
)

sealed interface RecentProductsEvent {
    data object BackClicked : RecentProductsEvent
    data object DeleteAllClicked : RecentProductsEvent
    data object BrowseProductsClicked : RecentProductsEvent
    data class ProductClicked(val productId: String) : RecentProductsEvent
    data class ProductRemoved(val productId: String) : RecentProductsEvent
}
