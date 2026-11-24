package com.nativelap.eumshoppingmall.ui.catalog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed interface CatalogPriceUiModel {
    data class Available(
        val currentLabel: String,
        val originalLabel: String? = null,
        val discountLabel: String? = null,
        // 판매가 숫자(원). 장바구니 합계에 쓰며, 모르면 null이고 그 경우 합계는 "가격 확인 필요"가 된다.
        val unitPriceWon: Long? = null,
    ) : CatalogPriceUiModel

    data object Undisclosed : CatalogPriceUiModel
}

// 상품이 받은 안전인증 종류. 한 상품이 여러 종류를 함께 가질 수 있다.
enum class SafetyCertificationType {
    KcSafety,
    SelfSafetyCheck,
    TypeApproval,
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
    val ratingValue: Float? = null,
    // 규격 한 줄(예: "10켤레"). 없어도 슬롯 높이는 유지된다.
    val specLabel: String? = null,
    val shippingLabel: String? = null,
    val benefitLabels: List<String> = emptyList(),
    // 상위 카테고리와 세부 카테고리 id. 목록 칩 필터는 세부, 홈 베스트 탭은 상위를 쓴다.
    val categoryId: String? = null,
    val subcategoryId: String? = null,
    // 옵션 없음이 명시된 상품만 true. null(정보 없음)이면 카드에서 바로 담지 않는다(담기 불가가 기본).
    val hasOptions: Boolean? = null,
    // 브랜드 id와 표시 이름. 브랜드 필터 목록은 두 값이 모두 있는 상품에서만 만든다.
    val brandId: String? = null,
    val brandName: String? = null,
    // 상품이 받은 안전인증 종류. 비어 있으면 인증 없음이며, 안전인증 필터에 쓴다.
    val safetyCertifications: Set<SafetyCertificationType> = emptySet(),
    // 등록 순서. 값이 클수록 최근 상품이며 최신순 정렬에 쓴다.
    val registeredOrder: Long = 0L,
)

data class CatalogCategoryUiModel(
    val id: String,
    @param:StringRes val labelResId: Int,
    @param:DrawableRes val iconResId: Int,
    // 카테고리 설명 부제의 문자열 리소스. 없으면 null이고 홈 카테고리에는 줄을 그리지 않는다.
    @param:StringRes val descriptionResId: Int? = null,
)

data class CatalogBrandUiModel(
    val id: String,
    val name: String,
    val productCountLabel: String,
    @param:DrawableRes val imageResId: Int? = null,
    val imageDescription: String? = null,
    val letterGroup: String? = null,
    val productCount: Int = 0,
)

data class CatalogOptionUiModel(
    val id: String,
    val label: String,
    val quantity: Int,
    val priceLabel: String?,
    // 옵션 1개당 금액(원). 총액 계산에 쓰며, 모르면 null이고 그 경우 총액 블록을 숨긴다. 0이면 가격 행을 숨긴다.
    val unitPriceWon: Long? = null,
)

data class CatalogSelectedOptionUiModel(
    val groupId: String,
    val option: CatalogOptionUiModel,
)

data class CatalogOptionGroupUiModel(
    val id: String,
    val label: String,
    val options: List<CatalogOptionUiModel> = emptyList(),
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
    data object MenuClicked : CatalogCategoryEvent
    data object SearchClicked : CatalogCategoryEvent
    data object ViewAllClicked : CatalogCategoryEvent
    data class ModeSelected(val brandMode: Boolean) : CatalogCategoryEvent
    data class CategorySelected(val categoryId: String) : CatalogCategoryEvent
    data class BrandSelected(val brandId: String) : CatalogCategoryEvent
}

const val CATALOG_ALL_CATEGORY_ID = "all"

// 칩 식별은 id로 하고 label은 표시용이다. count가 null이면 개수 없이 라벨만 그린다.
data class CatalogCategoryChipUiModel(
    val id: String,
    val label: String,
    val count: Int? = null,
)

data class CatalogProductListUiModel(
    val title: String,
    val categoryChips: List<CatalogCategoryChipUiModel> = emptyList(),
    val selectedCategoryId: String = CATALOG_ALL_CATEGORY_ID,
    val resultCountLabel: String? = null,
    val sortLabel: String,
    val products: List<CatalogProductUiModel> = emptyList(),
    val isListLayout: Boolean = false,
    // 상단 제목 리소스. 카테고리 타일과 같은 라벨 리소스를 쓰며, 있으면 title보다 우선한다(F2).
    @param:StringRes val titleResId: Int? = null,
)

sealed interface CatalogProductListEvent {
    data object BackClicked : CatalogProductListEvent
    data object SearchClicked : CatalogProductListEvent
    data class CategorySelected(val categoryId: String) : CatalogProductListEvent
    data object SortClicked : CatalogProductListEvent
    data object FilterClicked : CatalogProductListEvent
    data object LayoutToggled : CatalogProductListEvent
    data class ProductSelected(val productId: String) : CatalogProductListEvent
    data class FavoriteToggled(val productId: String) : CatalogProductListEvent
    data class QuickCartClicked(val productId: String) : CatalogProductListEvent
    data object LoadMoreClicked : CatalogProductListEvent
    data object FiltersResetClicked : CatalogProductListEvent
}

data class CatalogSearchUiModel(
    val query: String = "",
    val recentTerms: List<String> = emptyList(),
    val popularTerms: List<String> = emptyList(),
    val popularUpdatedAtLabel: String? = null,
)

sealed interface CatalogSearchEvent {
    data object MenuClicked : CatalogSearchEvent
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
    data class QuickCartClicked(val productId: String) : CatalogSearchResultsEvent
    data object PopularSearchClicked : CatalogSearchResultsEvent
    data object PriceInquiryClicked : CatalogSearchResultsEvent
    data object LoadMoreClicked : CatalogSearchResultsEvent
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
    val selectedOptions: List<CatalogSelectedOptionUiModel> = emptyList(),
    val optionsSheetVisible: Boolean = false,
    val isCartOptionEdit: Boolean = false,
    val totalPriceLabel: String? = null,
    // 상품 설명 본문. 데이터 원천이 없으면 null이며, 화면은 값이 있을 때만 그린다.
    val description: String? = null,
)

enum class CatalogPurchaseAction {
    AddToCart,
    BuyNow,
    PriceInquiry,
}

sealed interface CatalogProductDetailEvent {
    data object BackClicked : CatalogProductDetailEvent
    data object ShareClicked : CatalogProductDetailEvent
    data object CartClicked : CatalogProductDetailEvent
    data class ImageSelected(val index: Int) : CatalogProductDetailEvent
    data class TabSelected(val tabIndex: Int) : CatalogProductDetailEvent
    data class SelectOptionClicked(val action: CatalogPurchaseAction = CatalogPurchaseAction.AddToCart) : CatalogProductDetailEvent
    data class OptionSelected(val groupId: String, val optionId: String) : CatalogProductDetailEvent
    data class QuantityChanged(val groupId: String, val optionId: String, val quantity: Int) : CatalogProductDetailEvent
    data class OptionRemoved(val groupId: String, val optionId: String) : CatalogProductDetailEvent
    data object AddToCartClicked : CatalogProductDetailEvent
    data object BuyNowClicked : CatalogProductDetailEvent
    data object PriceInquiryClicked : CatalogProductDetailEvent
    data class RelatedProductSelected(val productId: String) : CatalogProductDetailEvent
    data class FavoriteToggled(val productId: String) : CatalogProductDetailEvent
    data object ProductFavoriteToggled : CatalogProductDetailEvent
    data object CloseOptionsSheet : CatalogProductDetailEvent
    data object AllReviewsClicked : CatalogProductDetailEvent
    data object ReviewWriteClicked : CatalogProductDetailEvent
    data object QuestionWriteClicked : CatalogProductDetailEvent
}

data class CatalogBrandListUiModel(
    val brandModeSelected: Boolean = true,
    val featuredBrand: CatalogBrandUiModel? = null,
    val featuredDescription: String = "",
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
    val storyTitle: String = "",
    val storyBody: String = "",
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
