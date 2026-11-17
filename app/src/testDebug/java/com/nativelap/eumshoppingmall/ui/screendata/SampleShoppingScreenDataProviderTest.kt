package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.ui.catalog.CATALOG_ALL_CATEGORY_ID
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryChipUiModel
import com.nativelap.eumshoppingmall.ui.home.filterByBestCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SampleShoppingScreenDataProviderTest {
    private val provider = SampleShoppingScreenDataProvider

    @Test
    fun mockProductsCarryNoProductRankBecauseRankIsSectionOrder() {
        assertTrue(provider.homeBestSellerProducts().all { product -> product.bestRank == null })
    }

    @Test
    fun recommendedAndTodayProductsCarryNoSectionRank() {
        assertTrue(provider.homeRecommendedProducts().all { product -> product.bestRank == null })
        assertTrue(provider.homeTodayProducts().all { product -> product.bestRank == null })
    }

    @Test
    fun todayProductsAreDisjointFromRecommendedProducts() {
        val recommendedIds = provider.homeRecommendedProducts().map { product -> product.id }.toSet()
        val todayIds = provider.homeTodayProducts().map { product -> product.id }

        assertTrue(todayIds.isNotEmpty())
        assertFalse(todayIds.any { id -> id in recommendedIds })
    }

    @Test
    fun ratedProductKeepsRatingValueAndReviewCountLabel() {
        val boots = provider.homeBestSellerProducts().first { product -> product.id == "sample-boots" }

        assertEquals(4.7f, boots.ratingValue)
        assertEquals("4.7 (6)", boots.ratingLabel)
    }

    @Test
    fun unratedProductHasNullRatingFields() {
        val extinguisher = provider.homeBestSellerProducts().first { product -> product.id == "sample-extinguisher" }

        assertNull(extinguisher.ratingValue)
        assertNull(extinguisher.ratingLabel)
    }

    @Test
    fun quoteOnlyProductIsMarkedPurchaseUnavailable() {
        val belt = provider.homeBestSellerProducts().first { product -> product.id == "sample-belt" }

        assertTrue(belt.isPurchaseUnavailable)
    }

    @Test
    fun categoryChipCountsSumToAllCountForEveryTopCategory() {
        listOf(null, "protective", "fire").forEach { categoryId ->
            val listing = provider.productList(categoryId)
            val all = listing.categoryChips.single { chip -> chip.id == CATALOG_ALL_CATEGORY_ID }
            val subcategoryChips: List<CatalogCategoryChipUiModel> = listing.categoryChips.filterNot { chip -> chip.id == CATALOG_ALL_CATEGORY_ID }

            assertEquals(listing.products.size, all.count)
            assertEquals(all.count, subcategoryChips.sumOf { chip -> chip.count ?: 0 })
        }
    }

    @Test
    fun productListFiltersByTopCategory() {
        assertEquals(listOf("sample-extinguisher"), provider.productList("fire").products.map { product -> product.id })
        assertTrue(provider.productList("uniform").products.isEmpty())
        assertEquals(4, provider.productList(null).products.size)
    }

    @Test
    fun bestTabFiltersMockProductsByTopCategory() {
        val protective = provider.homeBestSellerProducts().filterByBestCategory("protective")

        assertEquals(listOf("sample-helmet", "sample-boots", "sample-belt"), protective.map { product -> product.id })
    }

    @Test
    fun onlyExplicitNoOptionMockProductsCanBeQuickAddedFromHomeCards() {
        val byId = provider.homeBestSellerProducts().associateBy { product -> product.id }

        assertTrue(com.nativelap.eumshoppingmall.ui.catalog.canQuickAddToHomeProduct(byId.getValue("sample-helmet")))
        assertFalse(com.nativelap.eumshoppingmall.ui.catalog.canQuickAddToHomeProduct(byId.getValue("sample-boots")))
        assertFalse(com.nativelap.eumshoppingmall.ui.catalog.canQuickAddToHomeProduct(byId.getValue("sample-belt")))
    }
}
