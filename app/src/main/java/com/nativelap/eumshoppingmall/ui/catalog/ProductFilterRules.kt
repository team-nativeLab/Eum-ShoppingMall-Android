package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterBrandUiModel
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterCertificationUiModel
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPriceRange
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterSort

private const val PRICE_LOWER_BOUND_WON = 10_000L
private const val PRICE_UPPER_BOUND_WON = 30_000L
private const val WON_PER_MANWON = 10_000L

/**
 * 목록에 적용된 필터 값(draft 제외). 가격 칩과 직접 입력은 마지막에 고른 쪽만 남으므로 동시에 값을 가지지 않는다.
 * 브랜드·인증 종류는 여러 개를 고를 수 있다. 각 축 안에서는 고른 값 중 하나라도 맞으면 되고(OR), 축 사이와 가격·품절 조건은 모두 맞아야 한다(AND).
 */
internal data class ProductFilterCriteria(
    val priceRange: ProductFilterPriceRange? = null,
    // 직접 입력 가격(만원). 최소·최대 모두 해당 값을 포함한다.
    val minPriceManwon: Int? = null,
    val maxPriceManwon: Int? = null,
    // 고른 브랜드 id 집합. 비어 있으면 브랜드 조건을 쓰지 않는다.
    val brandIds: Set<String> = emptySet(),
    // 고른 안전인증 종류. 비어 있으면 인증 조건을 쓰지 않는다.
    val safetyCertifications: Set<SafetyCertificationType> = emptySet(),
    val hideSoldOut: Boolean = false,
    val sort: ProductFilterSort = ProductFilterSort.Recommended,
)

// 회전·프로세스 복원에서 필터 값을 잃지 않도록 enum은 이름으로, 집합은 ArrayList로 저장한다. null 값도 보존하려고 map으로 저장한다.
internal val ProductFilterCriteriaSaver: Saver<ProductFilterCriteria, Any> = mapSaver(
    save = { criteria ->
        mapOf<String, Any?>(
            "priceRange" to criteria.priceRange?.name,
            "minPriceManwon" to criteria.minPriceManwon,
            "maxPriceManwon" to criteria.maxPriceManwon,
            "brandIds" to ArrayList(criteria.brandIds),
            "safetyCertifications" to ArrayList(criteria.safetyCertifications.map { certification -> certification.name }),
            "hideSoldOut" to criteria.hideSoldOut,
            "sort" to criteria.sort.name,
        )
    },
    restore = { saved ->
        ProductFilterCriteria(
            priceRange = (saved["priceRange"] as String?)?.let { name -> ProductFilterPriceRange.valueOf(name) },
            minPriceManwon = saved["minPriceManwon"] as Int?,
            maxPriceManwon = saved["maxPriceManwon"] as Int?,
            brandIds = (saved["brandIds"] as List<*>).filterIsInstance<String>().toSet(),
            safetyCertifications = (saved["safetyCertifications"] as List<*>)
                .filterIsInstance<String>()
                .map { name -> SafetyCertificationType.valueOf(name) }
                .toSet(),
            hideSoldOut = saved["hideSoldOut"] as Boolean,
            sort = ProductFilterSort.valueOf(saved["sort"] as String),
        )
    },
)

/** 상품 목록에서 필터 옵션을 만드는 데 쓰는 값. 필터 칩과 토글은 데이터에 근거가 있을 때만 보인다. */
internal data class ProductFilterOptions(
    val brands: List<ProductFilterBrandUiModel>,
    val hasPricedProducts: Boolean,
    // 안전인증 상품이 하나도 없으면 빈 목록이다(섹션을 숨긴다). 종류별 개수는 조건 적용 전 기준 상품 수다.
    val safetyCertificationOptions: List<ProductFilterCertificationUiModel>,
    val hasSoldOutProducts: Boolean,
)

internal fun productFilterOptions(products: List<CatalogProductUiModel>): ProductFilterOptions = ProductFilterOptions(
    brands = productBrands(products),
    hasPricedProducts = products.any { product -> product.unitPriceWon() != null },
    safetyCertificationOptions = safetyCertificationOptions(products),
    hasSoldOutProducts = products.any { product -> product.isSoldOut },
)

// 브랜드 id와 이름이 모두 있는 상품만 센다. 같은 브랜드는 한 번만 보이고, 개수는 필터 적용 전 상품 기준이다.
private fun productBrands(products: List<CatalogProductUiModel>): List<ProductFilterBrandUiModel> {
    val namedBrands = products.mapNotNull { product ->
        val brandId = product.brandId
        val brandName = product.brandName
        if (brandId != null && brandName != null) brandId to brandName else null
    }
    val productCountByBrandId = namedBrands.groupingBy { brand -> brand.first }.eachCount()
    return namedBrands.distinctBy { brand -> brand.first }.map { (brandId, brandName) ->
        ProductFilterBrandUiModel(
            id = brandId,
            name = brandName,
            productCount = productCountByBrandId.getValue(brandId),
        )
    }
}

// 안전인증 상품이 하나도 없으면 빈 목록을 돌려준다. 있으면 세 종류를 모두 주고 종류별 상품 수를 붙인다.
private fun safetyCertificationOptions(products: List<CatalogProductUiModel>): List<ProductFilterCertificationUiModel> {
    if (products.none { product -> product.safetyCertifications.isNotEmpty() }) return emptyList()
    return SafetyCertificationType.entries.map { certification ->
        ProductFilterCertificationUiModel(
            type = certification,
            productCount = products.count { product -> certification in product.safetyCertifications },
        )
    }
}

/**
 * 가격·브랜드·안전인증·품절 조건으로 거르고 정렬한다. 가격 문의 상품(금액 없음)은 가격 조건이 있으면 제외한다.
 * 추천순은 원래 순서를 유지한다. 최신순은 등록 순서 내림차순, 별점순은 별점 높은 순이며 별점이 없는 상품은 뒤로 보낸다.
 */
internal fun applyProductFilter(
    products: List<CatalogProductUiModel>,
    criteria: ProductFilterCriteria,
): List<CatalogProductUiModel> {
    val filteredProducts = products.filter { product -> matchesCriteria(product, criteria) }
    return sortProducts(filteredProducts, criteria.sort)
}

private fun matchesCriteria(
    product: CatalogProductUiModel,
    criteria: ProductFilterCriteria,
): Boolean {
    if (!matchesBrand(product, criteria)) return false
    if (!matchesSafetyCertifications(product, criteria)) return false
    if (criteria.hideSoldOut && product.isSoldOut) return false
    return matchesPrice(product, criteria)
}

// 브랜드 축: 고른 브랜드 중 하나에 속하면 맞다. 브랜드가 없는 상품은 브랜드 조건이 있으면 제외한다.
private fun matchesBrand(
    product: CatalogProductUiModel,
    criteria: ProductFilterCriteria,
): Boolean {
    if (criteria.brandIds.isEmpty()) return true
    val brandId = product.brandId ?: return false
    return brandId in criteria.brandIds
}

// 인증 축: 고른 종류 중 하나라도 가진 상품이면 맞다.
private fun matchesSafetyCertifications(
    product: CatalogProductUiModel,
    criteria: ProductFilterCriteria,
): Boolean {
    if (criteria.safetyCertifications.isEmpty()) return true
    return product.safetyCertifications.any { certification -> certification in criteria.safetyCertifications }
}

private fun matchesPrice(
    product: CatalogProductUiModel,
    criteria: ProductFilterCriteria,
): Boolean {
    val hasPriceCondition = criteria.priceRange != null || criteria.minPriceManwon != null || criteria.maxPriceManwon != null
    if (!hasPriceCondition) return true
    val priceWon = product.unitPriceWon() ?: return false
    val matchesRange = criteria.priceRange?.let { range -> matchesRange(priceWon, range) } ?: true
    val matchesMin = criteria.minPriceManwon?.let { minManwon -> priceWon >= minManwon * WON_PER_MANWON } ?: true
    val matchesMax = criteria.maxPriceManwon?.let { maxManwon -> priceWon <= maxManwon * WON_PER_MANWON } ?: true
    return matchesRange && matchesMin && matchesMax
}

private fun matchesRange(
    priceWon: Long,
    range: ProductFilterPriceRange,
): Boolean = when (range) {
    ProductFilterPriceRange.UnderTenThousand -> priceWon < PRICE_LOWER_BOUND_WON
    ProductFilterPriceRange.TenToThirtyThousand -> priceWon in PRICE_LOWER_BOUND_WON until PRICE_UPPER_BOUND_WON
    ProductFilterPriceRange.OverThirtyThousand -> priceWon >= PRICE_UPPER_BOUND_WON
}

private fun sortProducts(
    products: List<CatalogProductUiModel>,
    sort: ProductFilterSort,
): List<CatalogProductUiModel> = when (sort) {
    ProductFilterSort.Recommended -> products

    ProductFilterSort.Latest -> products.sortedByDescending { product -> product.registeredOrder }

    ProductFilterSort.Rating -> {
        val (ratedProducts, unratedProducts) = products.partition { product -> product.ratingValue != null }
        ratedProducts.sortedByDescending { product -> product.ratingValue } + unratedProducts
    }

    ProductFilterSort.PriceAscending -> sortByPrice(products, descending = false)

    ProductFilterSort.PriceDescending -> sortByPrice(products, descending = true)
}

// 가격 문의 상품은 방향과 상관없이 맨 뒤에 둔다.
private fun sortByPrice(
    products: List<CatalogProductUiModel>,
    descending: Boolean,
): List<CatalogProductUiModel> {
    val (pricedProducts, undisclosedProducts) = products.partition { product -> product.unitPriceWon() != null }
    val sortedPricedProducts = if (descending) {
        pricedProducts.sortedByDescending { product -> product.unitPriceWon() }
    } else {
        pricedProducts.sortedBy { product -> product.unitPriceWon() }
    }
    return sortedPricedProducts + undisclosedProducts
}

private fun CatalogProductUiModel.unitPriceWon(): Long? = (price as? CatalogPriceUiModel.Available)?.unitPriceWon

/** 원본 목록은 있는데 칩·필터 적용 뒤 0건이면 true. 원본 자체가 비었을 때는 false라 "준비 중" 안내를 쓴다. */
internal fun isEmptyByFilter(sourceCount: Int, filteredCount: Int): Boolean = sourceCount > 0 && filteredCount == 0
