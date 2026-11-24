package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListUiModel
import com.nativelap.eumshoppingmall.ui.home.HomeHeroActionId

fun destinationForHomeHeroAction(action: HomeHeroActionId): EumDestination = when (action) {
    HomeHeroActionId.BrowseProducts -> EumDestination.ProductList(categoryId = null)
    HomeHeroActionId.ViewCompany -> EumDestination.CompanyIntro
}

fun destinationForCategory(categoryId: String?): EumDestination = EumDestination.ProductList(categoryId)

/**
 * 카테고리 목록 목적지를 만든다. 전체 보기(null)는 항상 이동하고, 카테고리는 상품이 있을 때만 이동한다.
 * 상품이 없으면 null이라 호출부는 이동하지 않는다(빈 목록 화면으로 가지 않음).
 * 카테고리 탭과 홈 카테고리 행이 이 함수를 함께 거친다.
 */
fun destinationForCategoryIfAvailable(categoryId: String?, productList: CatalogProductListUiModel): EumDestination? = destinationForCategory(categoryId).takeIf { categoryId == null || productList.products.isNotEmpty() }
