package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.navigation.EumDestination
import com.nativelap.eumshoppingmall.navigation.destinationForCategoryIfAvailable
import com.nativelap.eumshoppingmall.ui.catalog.CATALOG_ALL_CATEGORY_ID
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryChipUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryDefinitions
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
    fun priceUndisclosedOnlyProductIsMarkedPurchaseUnavailable() {
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
        assertEquals(listOf("sample-workwear"), provider.productList("uniform").products.map { product -> product.id })
        assertEquals(10, provider.productList(null).products.size)
    }

    @Test
    fun productListTitleIsTheSameLabelResourceAsItsCategoryTile() {
        CatalogCategoryDefinitions.all.forEach { category ->
            assertEquals(category.labelResId, provider.productList(category.id).titleResId)
        }
    }

    @Test
    fun debugProductShippingLabelsFollowTheFreeShippingRule() {
        val byId = provider.productList(null).products.associateBy { product -> product.id }

        assertEquals("무료배송", byId.getValue("sample-boots").shippingLabel)
        assertEquals("배송비 ₩3,000", byId.getValue("sample-workwear").shippingLabel)
        assertEquals("배송비 ₩3,000", byId.getValue("sample-extinguisher").shippingLabel)
        assertEquals("배송비 ₩3,000", byId.getValue("sample-helmet").shippingLabel)
        byId.values.forEach { product -> assertFalse(product.shippingLabel.orEmpty().contains("당일 출고")) }
        assertEquals("₩3,000", provider.productDetail("sample-helmet").shippingRows.single().value)
    }

    @Test
    fun everyDebugCategoryHasAtLeastOneProduct() {
        CatalogCategoryDefinitions.all.forEach { category ->
            assertTrue(category.id, provider.productList(category.id).products.isNotEmpty())
        }
    }

    @Test
    fun everyDebugCategoryNavigatesToItsProductList() {
        CatalogCategoryDefinitions.all.forEach { category ->
            assertEquals(
                EumDestination.ProductList(category.id),
                destinationForCategoryIfAvailable(category.id, provider.productList(category.id)),
            )
        }
    }

    @Test
    fun homeSectionsKeepTheSameMockProductsAfterAddingCategoryProducts() {
        assertEquals(listOf("sample-helmet", "sample-boots"), provider.homeRecommendedProducts().map { product -> product.id })
        assertEquals(listOf("sample-belt", "sample-extinguisher"), provider.homeTodayProducts().map { product -> product.id })
        assertEquals(
            listOf("sample-helmet", "sample-boots", "sample-belt", "sample-extinguisher"),
            provider.homeBestSellerProducts().map { product -> product.id },
        )
    }

    @Test
    fun categoryOnlyProductOpensItsOwnDetail() {
        assertEquals("sample-cctv", provider.productDetail("sample-cctv").product.id)
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
