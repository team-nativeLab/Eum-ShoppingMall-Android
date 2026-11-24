package com.nativelap.eumshoppingmall.ui.catalog

// 전체 칩은 모든 상품을 보여 주고, 그 밖의 칩은 세부 카테고리 id가 같은 상품만 남긴다.
fun filterCatalogProductsBySubcategory(
    products: List<CatalogProductUiModel>,
    selectedCategoryId: String,
): List<CatalogProductUiModel> = if (selectedCategoryId == CATALOG_ALL_CATEGORY_ID) {
    products
} else {
    products.filter { product -> product.subcategoryId == selectedCategoryId }
}

// 검색창을 비우면 결과 화면을 떠나 최근·인기 검색어 화면으로 돌아간다.
fun isClearedSearchQuery(query: String): Boolean = query.isBlank()
