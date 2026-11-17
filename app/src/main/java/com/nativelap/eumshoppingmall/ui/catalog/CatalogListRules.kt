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

// 견적 상품 선택 화면은 찜 하트를 숨긴다(찜과 견적 선택을 한 화면에서 섞지 않는다).
fun showsFavoriteToggle(isQuotePicker: Boolean): Boolean = !isQuotePicker

// 견적 상품 선택 화면은 사유 칩(품절·옵션 필요)을 숨긴다. 담기 동작이 없는 화면이라 사유를 알릴 이유가 없다.
fun showsBlockReasonChip(isQuotePicker: Boolean): Boolean = !isQuotePicker

// 검색창을 비우면 결과 화면을 떠나 최근·인기 검색어 화면으로 돌아간다.
fun isClearedSearchQuery(query: String): Boolean = query.isBlank()
