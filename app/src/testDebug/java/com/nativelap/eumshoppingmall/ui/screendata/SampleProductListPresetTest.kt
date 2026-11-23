package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.ProductListPreset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SampleProductListPresetTest {
    private val provider = SampleShoppingScreenDataProvider

    @Test
    fun homeDiscountSectionShowsThreeOrFourDiscountedMockProducts() {
        val discounted = provider.homeDiscountProducts()

        assertTrue(discounted.size in 3..4)
        assertTrue(discounted.all { product -> product.discountRateLabel != null })
    }

    @Test
    fun discountPresetListsTheSameProductsAsTheHomeDiscountSection() {
        assertEquals(
            provider.homeDiscountProducts().map { product -> product.id },
            productListSourceFor(ProductListPreset.Discount, null, provider).map { product -> product.id },
        )
    }

    @Test
    fun homeSectionsAndTheirPresetListsShareTheSameProducts() {
        assertEquals(
            provider.homeRecommendedProducts().map { product -> product.id },
            productListSourceFor(ProductListPreset.Recommended, null, provider).map { product -> product.id },
        )
        assertEquals(
            provider.homeTodayProducts().map { product -> product.id },
            productListSourceFor(ProductListPreset.Today, null, provider).map { product -> product.id },
        )
        assertEquals(
            provider.homeBestSellerProducts().map { product -> product.id },
            productListSourceFor(ProductListPreset.Best, null, provider).map { product -> product.id },
        )
    }

    @Test
    fun presetListingUsesThePresetTitleResource() {
        assertEquals(R.string.product_list_title_discount, provider.productList(null, ProductListPreset.Discount).titleResId)
        assertEquals(R.string.product_list_title_best, provider.productList(null, ProductListPreset.Best).titleResId)
    }
}
