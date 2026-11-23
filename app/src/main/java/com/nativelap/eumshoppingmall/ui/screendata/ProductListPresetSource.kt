package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.ProductListPreset

/**
 * 상품 목록의 기본 원본을 고른다. preset이 정한 원본에서 categoryId가 있으면 그 카테고리 상품만 남긴다.
 *
 * 칩·정렬 같은 목록 필터는 이 결과 위에서 동작하므로 preset은 기본 목록 소스만 바꾼다.
 */
fun productListSourceFor(
    preset: ProductListPreset,
    categoryId: String?,
    provider: ShoppingScreenDataProvider,
): List<CatalogProductUiModel> = provider.presetProducts(preset).filter { product ->
    categoryId == null || product.categoryId == categoryId
}
