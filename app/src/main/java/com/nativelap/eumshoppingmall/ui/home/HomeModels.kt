package com.nativelap.eumshoppingmall.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nativelap.eumshoppingmall.ui.catalog.ProductListPreset
import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab

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
    // 가격 미표시 상품(금액을 보여 주지 않고 1:1 문의로 연결). 가격 문구를 "금액 미표시"로 고르는 기준이다.
    val isPriceUndisclosed: Boolean = false,
    // 베스트 섹션의 표시 순서 순위(withSectionRanks로만 채운다). 상품 속성이 아니므로 다른 섹션에는 붙지 않는다.
    val bestRank: Int? = null,
    val specLabel: String? = null,
    val shippingLabel: String? = null,
    val benefitLabels: List<String> = emptyList(),
    // 라벨 배지. 순위 배지가 있으면 가린다(카탈로그와 같은 규칙).
    val badgeLabel: String? = null,
    // 상위 카테고리 id. 베스트 셀러 탭이 이 값으로 거른다.
    val categoryId: String? = null,
    // 담기에 쓰는 판매가(원). null이면 가격 미상으로 담긴다.
    val unitPriceWon: Long? = null,
    // 옵션 없음이 명시된 상품만 false. null이면 홈 카드에서 바로 담지 않는다.
    val hasOptions: Boolean? = null,
)

// 베스트 섹션의 순위 배지 상한(임시 기본값, 사용자 확인 후 조정).
const val HOME_BEST_RANK_LIMIT = 5

// 베스트 순위는 상품 속성이 아니라 섹션에 보이는 순서로 매긴다. 상위 HOME_BEST_RANK_LIMIT개에만 붙인다.
fun List<HomeProductUiModel>.withSectionRanks(): List<HomeProductUiModel> = mapIndexed { index, product ->
    product.copy(bestRank = if (index < HOME_BEST_RANK_LIMIT) index + 1 else null)
}

// 베스트 탭: null이면 전체, 아니면 상위 카테고리 id가 같은 상품만 남긴다.
fun List<HomeProductUiModel>.filterByBestCategory(categoryId: String?): List<HomeProductUiModel> {
    if (categoryId == null) return this
    return filter { product -> product.categoryId == categoryId }
}

// 상품이 하나도 없는 카테고리 탭은 숨긴다. 탭 목록은 전체 베스트 목록 기준으로 만든다.
fun List<HomeCategoryUiModel>.withBestProducts(products: List<HomeProductUiModel>): List<HomeCategoryUiModel> = filter { category -> products.any { product -> product.categoryId == category.id } }

data class HomeCategoryUiModel(
    val id: String,
    @param:StringRes val labelResId: Int,
    @param:DrawableRes val iconResId: Int,
    // 카테고리 설명 부제의 문자열 리소스. 없으면 null이고 화면은 줄을 그리지 않는다.
    @param:StringRes val descriptionResId: Int? = null,
)


sealed interface HomeScreenEvent {
    data class QueryChanged(val query: String) : HomeScreenEvent
    data object SearchSubmitted : HomeScreenEvent
    data class CategoryClicked(val category: HomeCategoryUiModel) : HomeScreenEvent
    data class BestCategorySelected(val categoryId: String?) : HomeScreenEvent
    data class QuickCartClicked(val product: HomeProductUiModel) : HomeScreenEvent
    data class ProductClicked(val product: HomeProductUiModel) : HomeScreenEvent
    data class HeroActionClicked(val action: HomeHeroActionId) : HomeScreenEvent
    data object MenuClicked : HomeScreenEvent

    // 섹션의 "전체 보기"(베스트·할인·오늘·추천)와 하단 전체 상품 보기가 모두 이 이벤트로 목록 preset을 넘긴다.
    data class SeeProductsClicked(val preset: ProductListPreset) : HomeScreenEvent
    data class FavoriteClicked(val product: HomeProductUiModel) : HomeScreenEvent
    data class NavigationSelected(val item: MainNavigationTab) : HomeScreenEvent
}

// 검색 결과에서 다시 검색하면 그 검색어를 홈 검색창 공유 상태로 옮긴다. 공백은 다듬는다.
fun homeQueryForSearchQuery(searchQuery: String): String = searchQuery.trim()

// 홈 검색창 글자: 검색 화면과 공유하는 값이 있으면 그것을, 없으면 홈 자체 입력을 쓴다.
fun effectiveHomeQuery(hoisted: String?, internal: String): String = hoisted ?: internal

enum class HomeHeroActionId {
    BrowseProducts,
    ViewCompany,
}
