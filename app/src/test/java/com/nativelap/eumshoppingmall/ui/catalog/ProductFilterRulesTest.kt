package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPriceRange
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterSort
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductFilterRulesTest {
    private val cheapProduct = product("cheap", 9_999L)
    private val lowerBoundProduct = product("lower", 10_000L)
    private val midProduct = product("mid", 29_999L)
    private val upperBoundProduct = product("upper", 30_000L)
    private val quoteProduct = CatalogProductUiModel(id = "quote", name = "quote", price = CatalogPriceUiModel.QuoteRequired)
    private val allProducts = listOf(upperBoundProduct, quoteProduct, cheapProduct, midProduct, lowerBoundProduct)

    @Test
    fun noFilterAndRecommendedSortKeepOriginalOrder() {
        assertEquals(allProducts, applyProductFilter(allProducts, null, ProductFilterSort.Recommended))
        assertEquals(allProducts, applyProductFilter(allProducts, null, ProductFilterSort.Latest))
    }

    @Test
    fun priceRangeUsesLowerBoundInclusiveAndUpperBoundExclusive() {
        assertEquals(
            listOf("cheap"),
            ids(applyProductFilter(allProducts, ProductFilterPriceRange.UnderTenThousand, ProductFilterSort.Recommended)),
        )
        assertEquals(
            listOf("mid", "lower"),
            ids(applyProductFilter(allProducts, ProductFilterPriceRange.TenToThirtyThousand, ProductFilterSort.Recommended)),
        )
        assertEquals(
            listOf("upper"),
            ids(applyProductFilter(allProducts, ProductFilterPriceRange.OverThirtyThousand, ProductFilterSort.Recommended)),
        )
    }

    @Test
    fun quoteRequiredProductIsExcludedWhenPriceRangeIsChosen() {
        val filteredProducts = applyProductFilter(allProducts, ProductFilterPriceRange.OverThirtyThousand, ProductFilterSort.Recommended)

        assertEquals(false, filteredProducts.any { filteredProduct -> filteredProduct.id == "quote" })
    }

    @Test
    fun priceSortPutsQuoteRequiredProductLastInBothDirections() {
        assertEquals(
            listOf("cheap", "lower", "mid", "upper", "quote"),
            ids(applyProductFilter(allProducts, null, ProductFilterSort.PriceAscending)),
        )
        assertEquals(
            listOf("upper", "mid", "lower", "cheap", "quote"),
            ids(applyProductFilter(allProducts, null, ProductFilterSort.PriceDescending)),
        )
    }

    private fun ids(products: List<CatalogProductUiModel>): List<String> = products.map { product -> product.id }

    private fun product(
        productId: String,
        priceWon: Long,
    ) = CatalogProductUiModel(
        id = productId,
        name = productId,
        price = CatalogPriceUiModel.Available(currentLabel = "$priceWon", unitPriceWon = priceWon),
    )
}
