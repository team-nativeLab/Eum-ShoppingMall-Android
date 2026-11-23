package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterBrandUiModel
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterCertificationUiModel
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPriceRange
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterSort
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductFilterRulesTest {
    private val cheapProduct = product("cheap", 9_999L)
    private val lowerBoundProduct = product("lower", 10_000L)
    private val midProduct = product("mid", 29_999L)
    private val upperBoundProduct = product("upper", 30_000L)
    private val quoteProduct = CatalogProductUiModel(id = "quote", name = "quote", price = CatalogPriceUiModel.QuoteRequired)
    private val allProducts = listOf(upperBoundProduct, quoteProduct, cheapProduct, midProduct, lowerBoundProduct)

    @Test
    fun recommendedSortWithoutConditionsKeepsOriginalOrder() {
        assertEquals(allProducts, applyProductFilter(allProducts, ProductFilterCriteria(sort = ProductFilterSort.Recommended)))
    }

    // 기존에는 최신순이 추천순과 같은 no-op이었다. 등록 순서 내림차순으로 바뀌었으므로 기대값을 바꿨다.
    @Test
    fun latestSortPutsHighestRegisteredOrderFirst() {
        val products = listOf(
            product("old", 1_000L, registeredOrder = 1L),
            product("new", 1_000L, registeredOrder = 3L),
            product("middle", 1_000L, registeredOrder = 2L),
        )

        assertEquals(
            listOf("new", "middle", "old"),
            ids(applyProductFilter(products, ProductFilterCriteria(sort = ProductFilterSort.Latest))),
        )
    }

    @Test
    fun ratingSortPutsHigherRatingFirstAndUnratedProductsLast() {
        val products = listOf(
            product("unrated", 1_000L),
            product("low", 1_000L, rating = 3.5f),
            product("high", 1_000L, rating = 4.8f),
        )

        assertEquals(
            listOf("high", "low", "unrated"),
            ids(applyProductFilter(products, ProductFilterCriteria(sort = ProductFilterSort.Rating))),
        )
    }

    @Test
    fun priceRangeChipUsesLowerBoundInclusiveAndUpperBoundExclusive() {
        assertEquals(
            listOf("cheap"),
            ids(applyProductFilter(allProducts, ProductFilterCriteria(priceRange = ProductFilterPriceRange.UnderTenThousand))),
        )
        assertEquals(
            listOf("mid", "lower"),
            ids(applyProductFilter(allProducts, ProductFilterCriteria(priceRange = ProductFilterPriceRange.TenToThirtyThousand))),
        )
        assertEquals(
            listOf("upper"),
            ids(applyProductFilter(allProducts, ProductFilterCriteria(priceRange = ProductFilterPriceRange.OverThirtyThousand))),
        )
    }

    @Test
    fun directPriceBoundsIncludeBothEndsInManwonAndExcludeQuoteRequiredProducts() {
        // 1만원 이상 3만원 이하: 9,999원은 빠지고 10,000원·30,000원은 들어가며, 금액 없는 상품은 제외한다.
        val criteria = ProductFilterCriteria(minPriceManwon = 1, maxPriceManwon = 3)

        assertEquals(listOf("upper", "mid", "lower"), ids(applyProductFilter(allProducts, criteria)))
    }

    @Test
    fun quoteRequiredProductIsExcludedWhenPriceConditionIsSet() {
        val filteredProducts = applyProductFilter(
            allProducts,
            ProductFilterCriteria(priceRange = ProductFilterPriceRange.OverThirtyThousand),
        )

        assertFalse(filteredProducts.any { product -> product.id == "quote" })
    }

    @Test
    fun priceSortPutsQuoteRequiredProductLastInBothDirections() {
        assertEquals(
            listOf("cheap", "lower", "mid", "upper", "quote"),
            ids(applyProductFilter(allProducts, ProductFilterCriteria(sort = ProductFilterSort.PriceAscending))),
        )
        assertEquals(
            listOf("upper", "mid", "lower", "cheap", "quote"),
            ids(applyProductFilter(allProducts, ProductFilterCriteria(sort = ProductFilterSort.PriceDescending))),
        )
    }

    // 브랜드는 여러 개를 고를 수 있고, 고른 브랜드 중 하나에 속하면 보인다(OR).
    @Test
    fun brandFilterKeepsProductsOfAnySelectedBrand() {
        val products = listOf(
            product("a-1", 1_000L, brandId = "brand-a", brandName = "A"),
            product("b-1", 1_000L, brandId = "brand-b", brandName = "B"),
            product("c-1", 1_000L, brandId = "brand-c", brandName = "C"),
            product("none", 1_000L),
        )

        assertEquals(
            listOf("a-1", "b-1"),
            ids(applyProductFilter(products, ProductFilterCriteria(brandIds = setOf("brand-a", "brand-b")))),
        )
    }

    // 인증 종류는 여러 개를 고를 수 있고, 고른 종류 중 하나를 가지면 보인다(OR).
    @Test
    fun safetyCertificationFilterKeepsProductsWithAnySelectedType() {
        val products = listOf(
            product("kcs", 1_000L, safetyCertifications = setOf(SafetyCertificationType.KcSafety)),
            product("type", 1_000L, safetyCertifications = setOf(SafetyCertificationType.TypeApproval)),
            product("self", 1_000L, safetyCertifications = setOf(SafetyCertificationType.SelfSafetyCheck)),
            product("plain", 1_000L),
        )

        assertEquals(
            listOf("kcs", "type"),
            ids(
                applyProductFilter(
                    products,
                    ProductFilterCriteria(
                        safetyCertifications = setOf(SafetyCertificationType.KcSafety, SafetyCertificationType.TypeApproval),
                    ),
                ),
            ),
        )
    }

    // 브랜드 축과 인증 축은 AND다. 각 축 안에서 고른 값 중 하나만 맞으면 된다.
    @Test
    fun brandAxisAndSafetyCertificationAxisMustBothMatch() {
        val products = listOf(
            product("a-kcs", 1_000L, brandId = "brand-a", brandName = "A", safetyCertifications = setOf(SafetyCertificationType.KcSafety)),
            product("a-plain", 1_000L, brandId = "brand-a", brandName = "A"),
            product("b-kcs", 1_000L, brandId = "brand-b", brandName = "B", safetyCertifications = setOf(SafetyCertificationType.KcSafety)),
        )

        assertEquals(
            listOf("a-kcs"),
            ids(
                applyProductFilter(
                    products,
                    ProductFilterCriteria(
                        brandIds = setOf("brand-a"),
                        safetyCertifications = setOf(SafetyCertificationType.KcSafety),
                    ),
                ),
            ),
        )
    }

    @Test
    fun hideSoldOutRemovesSoldOutProducts() {
        val products = listOf(
            product("sold-out", 1_000L, isSoldOut = true),
            product("on-sale", 1_000L),
        )

        assertEquals(listOf("on-sale"), ids(applyProductFilter(products, ProductFilterCriteria(hideSoldOut = true))))
    }

    @Test
    fun optionsListRealBrandsOnceWithProductCountBeforeFiltering() {
        val products = listOf(
            product("a-1", 1_000L, brandId = "brand-a", brandName = "A"),
            product("a-2", 2_000L, brandId = "brand-a", brandName = "A"),
            product("no-brand", 3_000L),
            quoteProduct,
        )

        val options = productFilterOptions(products)

        assertEquals(listOf(ProductFilterBrandUiModel("brand-a", "A", productCount = 2)), options.brands)
        assertTrue(options.hasPricedProducts)
        assertFalse(options.hasSoldOutProducts)
    }

    @Test
    fun safetyCertificationOptionsCountEachTypeFromSourceProducts() {
        val products = listOf(
            product("kcs-1", 1_000L, safetyCertifications = setOf(SafetyCertificationType.KcSafety)),
            product("kcs-type", 1_000L, safetyCertifications = setOf(SafetyCertificationType.KcSafety, SafetyCertificationType.TypeApproval)),
            product("plain", 1_000L),
        )

        assertEquals(
            listOf(
                ProductFilterCertificationUiModel(SafetyCertificationType.KcSafety, productCount = 2),
                ProductFilterCertificationUiModel(SafetyCertificationType.SelfSafetyCheck, productCount = 0),
                ProductFilterCertificationUiModel(SafetyCertificationType.TypeApproval, productCount = 1),
            ),
            productFilterOptions(products).safetyCertificationOptions,
        )
    }

    // 안전인증 상품이 하나도 없으면 인증 섹션을 숨기도록 빈 목록을 준다.
    @Test
    fun safetyCertificationOptionsAreEmptyWhenNoProductIsCertified() {
        val options = productFilterOptions(listOf(product("plain", 1_000L), quoteProduct))

        assertTrue(options.safetyCertificationOptions.isEmpty())
    }

    @Test
    fun optionsHidePriceSectionWhenEveryProductNeedsQuote() {
        val options = productFilterOptions(listOf(quoteProduct))

        assertFalse(options.hasPricedProducts)
        assertTrue(options.brands.isEmpty())
    }

    private fun ids(products: List<CatalogProductUiModel>): List<String> = products.map { product -> product.id }

    private fun product(
        productId: String,
        priceWon: Long,
        brandId: String? = null,
        brandName: String? = null,
        safetyCertifications: Set<SafetyCertificationType> = emptySet(),
        isSoldOut: Boolean = false,
        rating: Float? = null,
        registeredOrder: Long = 0L,
    ) = CatalogProductUiModel(
        id = productId,
        name = productId,
        price = CatalogPriceUiModel.Available(currentLabel = "$priceWon", unitPriceWon = priceWon),
        brandId = brandId,
        brandName = brandName,
        safetyCertifications = safetyCertifications,
        isSoldOut = isSoldOut,
        ratingValue = rating,
        registeredOrder = registeredOrder,
    )
}
