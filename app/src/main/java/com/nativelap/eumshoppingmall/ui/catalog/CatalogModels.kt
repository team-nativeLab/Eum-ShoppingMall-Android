package com.nativelap.eumshoppingmall.ui.catalog

import androidx.annotation.DrawableRes

sealed interface CatalogPriceUiModel {
    data class Available(
        val currentLabel: String,
        val originalLabel: String? = null,
        val discountLabel: String? = null,
    ) : CatalogPriceUiModel

    data object QuoteRequired : CatalogPriceUiModel
}

data class CatalogProductUiModel(
    val id: String,
    val name: String,
    val price: CatalogPriceUiModel,
    @param:DrawableRes val imageResId: Int? = null,
    val imageDescription: String? = null,
    val subtitle: String? = null,
    val ratingLabel: String? = null,
    val reviewCountLabel: String? = null,
    val badgeLabel: String? = null,
    val isSoldOut: Boolean = false,
    val isFavorite: Boolean = false,
)

data class CatalogCategoryUiModel(
    val id: String,
    val label: String,
    @param:DrawableRes val iconResId: Int,
)

data class CatalogBrandUiModel(
    val id: String,
    val name: String,
    val productCountLabel: String,
    @param:DrawableRes val imageResId: Int? = null,
    val imageDescription: String? = null,
    val letterGroup: String? = null,
)

data class CatalogOptionUiModel(
    val id: String,
    val label: String,
    val quantity: Int,
    val priceLabel: String?,
)

data class CatalogOptionGroupUiModel(
    val id: String,
    val label: String,
    val options: List<CatalogOptionUiModel> = emptyList(),
    val selectedOptionId: String? = null,
)

data class CatalogKeyValueUiModel(
    val label: String,
    val value: String,
)

data class CatalogReviewUiModel(
    val authorLabel: String,
    val ratingLabel: String,
    val dateLabel: String,
    val content: String,
)

data class CatalogQuestionUiModel(
    val question: String,
    val answer: String? = null,
    val dateLabel: String,
)

data class CatalogCategoryScreenUiModel(
    val brandModeSelected: Boolean = false,
    val categories: List<CatalogCategoryUiModel> = emptyList(),
    val brands: List<CatalogBrandUiModel> = emptyList(),
)

sealed interface CatalogCategoryEvent {
    data object SearchClicked : CatalogCategoryEvent
    data object ViewAllClicked : CatalogCategoryEvent
    data class ModeSelected(val brandMode: Boolean) : CatalogCategoryEvent
    data class CategorySelected(val categoryId: String) : CatalogCategoryEvent
    data class BrandSelected(val brandId: String) : CatalogCategoryEvent
}

data class CatalogProductListUiModel(
    val title: String,
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val resultCountLabel: String? = null,
    val sortLabel: String,
    val products: List<CatalogProductUiModel> = emptyList(),
    val isListLayout: Boolean = false,
)

sealed interface CatalogProductListEvent {
    data object BackClicked : CatalogProductListEvent
    data object SearchClicked : CatalogProductListEvent
    data class CategorySelected(val category: String) : CatalogProductListEvent
    data object SortClicked : CatalogProductListEvent
    data object FilterClicked : CatalogProductListEvent
    data object LayoutToggled : CatalogProductListEvent
    data class ProductSelected(val productId: String) : CatalogProductListEvent
    data class FavoriteToggled(val productId: String) : CatalogProductListEvent
}

data class CatalogSearchUiModel(
    val query: String = "",
    val recentTerms: List<String> = emptyList(),
    val popularTerms: List<String> = emptyList(),
    val popularUpdatedAtLabel: String? = null,
)

sealed interface CatalogSearchEvent {
    data class QueryChanged(val query: String) : CatalogSearchEvent
    data object SearchSubmitted : CatalogSearchEvent
    data object Cancelled : CatalogSearchEvent
    data object ClearRecentTerms : CatalogSearchEvent
    data class RecentTermSelected(val term: String) : CatalogSearchEvent
    data class PopularTermSelected(val term: String) : CatalogSearchEvent
}

data class CatalogSearchResultsUiModel(
    val query: String,
    val filters: List<String> = emptyList(),
    val selectedFilter: String? = null,
    val resultCount: Int,
    val sortLabel: String,
    val products: List<CatalogProductUiModel> = emptyList(),
    val isListLayout: Boolean = false,
)

sealed interface CatalogSearchResultsEvent {
    data object BackClicked : CatalogSearchResultsEvent
    data object Cancelled : CatalogSearchResultsEvent
    data class QueryChanged(val query: String) : CatalogSearchResultsEvent
    data object SearchSubmitted : CatalogSearchResultsEvent
    data class FilterSelected(val filter: String) : CatalogSearchResultsEvent
    data object SortClicked : CatalogSearchResultsEvent
    data object FilterClicked : CatalogSearchResultsEvent
    data object LayoutToggled : CatalogSearchResultsEvent
    data class ProductSelected(val productId: String) : CatalogSearchResultsEvent
    data class FavoriteToggled(val productId: String) : CatalogSearchResultsEvent
    data object PopularSearchClicked : CatalogSearchResultsEvent
    data object QuoteRequestClicked : CatalogSearchResultsEvent
}

data class CatalogProductDetailUiModel(
    val product: CatalogProductUiModel,
    val galleryImageResIds: List<Int?> = emptyList(),
    val selectedImageIndex: Int = 0,
    val selectedTabIndex: Int = 0,
    val shippingRows: List<CatalogKeyValueUiModel> = emptyList(),
    val returnRows: List<CatalogKeyValueUiModel> = emptyList(),
    val specifications: List<CatalogKeyValueUiModel> = emptyList(),
    val reviewCount: Int = 0,
    val questionCount: Int = 0,
    val reviews: List<CatalogReviewUiModel> = emptyList(),
    val questions: List<CatalogQuestionUiModel> = emptyList(),
    val relatedProducts: List<CatalogProductUiModel> = emptyList(),
    val optionGroups: List<CatalogOptionGroupUiModel> = emptyList(),
    val selectedOptions: List<CatalogOptionUiModel> = emptyList(),
    val optionsSheetVisible: Boolean = false,
    val pendingPurchaseAction: CatalogPurchaseAction = CatalogPurchaseAction.AddToCart,
)

enum class CatalogPurchaseAction {
    AddToCart,
    BuyNow,
}

sealed interface CatalogProductDetailEvent {
    data object BackClicked : CatalogProductDetailEvent
    data object ShareClicked : CatalogProductDetailEvent
    data object CartClicked : CatalogProductDetailEvent
    data class ImageSelected(val index: Int) : CatalogProductDetailEvent
    data class TabSelected(val tabIndex: Int) : CatalogProductDetailEvent
    data class SelectOptionClicked(val action: CatalogPurchaseAction = CatalogPurchaseAction.AddToCart) : CatalogProductDetailEvent
    data class OptionSelected(val groupId: String, val optionId: String) : CatalogProductDetailEvent
    data class QuantityChanged(val optionId: String, val quantity: Int) : CatalogProductDetailEvent
    data object AddToCartClicked : CatalogProductDetailEvent
    data object BuyNowClicked : CatalogProductDetailEvent
    data object QuoteRequestClicked : CatalogProductDetailEvent
    data class RelatedProductSelected(val productId: String) : CatalogProductDetailEvent
    data class FavoriteToggled(val productId: String) : CatalogProductDetailEvent
    data object ProductFavoriteToggled : CatalogProductDetailEvent
    data object CloseOptionsSheet : CatalogProductDetailEvent
}

data class CatalogBrandListUiModel(
    val brandModeSelected: Boolean = true,
    val featuredBrand: CatalogBrandUiModel? = null,
    val query: String = "",
    val letters: List<String> = emptyList(),
    val brandsByLetter: Map<String, List<CatalogBrandUiModel>> = emptyMap(),
    val sortAscending: Boolean = true,
)

sealed interface CatalogBrandListEvent {
    data object ProductModeSelected : CatalogBrandListEvent
    data object SearchClicked : CatalogBrandListEvent
    data object SortToggled : CatalogBrandListEvent
    data class QueryChanged(val query: String) : CatalogBrandListEvent
    data class BrandSelected(val brandId: String) : CatalogBrandListEvent
}

data class CatalogBrandDetailUiModel(
    val brand: CatalogBrandUiModel,
    val description: String,
    val informationRows: List<CatalogKeyValueUiModel> = emptyList(),
    val products: List<CatalogProductUiModel> = emptyList(),
    val otherBrands: List<CatalogBrandUiModel> = emptyList(),
    val selectedCategory: String? = null,
    val categories: List<String> = emptyList(),
    val sortLabel: String,
)

sealed interface CatalogBrandDetailEvent {
    data object BackClicked : CatalogBrandDetailEvent
    data object ShareClicked : CatalogBrandDetailEvent
    data object WebsiteClicked : CatalogBrandDetailEvent
    data class CategorySelected(val category: String) : CatalogBrandDetailEvent
    data object SortClicked : CatalogBrandDetailEvent
    data class ProductSelected(val productId: String) : CatalogBrandDetailEvent
    data class FavoriteToggled(val productId: String) : CatalogBrandDetailEvent
    data class OtherBrandSelected(val brandId: String) : CatalogBrandDetailEvent
    data object ViewAllBrandsClicked : CatalogBrandDetailEvent
}
