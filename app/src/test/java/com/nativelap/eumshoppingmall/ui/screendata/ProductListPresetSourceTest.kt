package com.nativelap.eumshoppingmall.ui.screendata

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.catalog.ProductListPreset
import com.nativelap.eumshoppingmall.ui.catalog.titleResId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductListPresetSourceTest {
    private val allProducts = listOf(product("all-1", "protective"), product("all-2", "fire"))
    private val bestProducts = listOf(product("best-1", "protective"), product("best-2", "fire"))
    private val discountProducts = listOf(product("discount-1", "fire"), product("discount-2", "protective"))
    private val todayProducts = listOf(product("today-1", "fire"))
    private val recommendedProducts = listOf(product("recommended-1", "protective"))

    private val provider = FakePresetProvider(
        mapOf(
            ProductListPreset.All to allProducts,
            ProductListPreset.Best to bestProducts,
            ProductListPreset.Discount to discountProducts,
            ProductListPreset.Today to todayProducts,
            ProductListPreset.Recommended to recommendedProducts,
        ),
    )

    @Test
    fun eachPresetReadsItsOwnSourceList() {
        assertEquals(ids(bestProducts), ids(productListSourceFor(ProductListPreset.Best, null, provider)))
        assertEquals(ids(discountProducts), ids(productListSourceFor(ProductListPreset.Discount, null, provider)))
        assertEquals(ids(todayProducts), ids(productListSourceFor(ProductListPreset.Today, null, provider)))
        assertEquals(ids(recommendedProducts), ids(productListSourceFor(ProductListPreset.Recommended, null, provider)))
    }

    @Test
    fun allPresetWithoutCategoryReturnsTheWholeCatalogSource() {
        assertEquals(ids(allProducts), ids(productListSourceFor(ProductListPreset.All, null, provider)))
    }

    @Test
    fun categoryIdNarrowsThePresetSourceToThatCategory() {
        assertEquals(
            listOf("discount-2"),
            ids(productListSourceFor(ProductListPreset.Discount, "protective", provider)),
        )
    }

    @Test
    fun presetWithoutProductsInTheCategoryReturnsEmptyList() {
        assertTrue(productListSourceFor(ProductListPreset.Today, "protective", provider).isEmpty())
    }

    @Test
    fun presetTitleResourcesMatchEachPreset() {
        assertNull(ProductListPreset.All.titleResId())
        assertEquals(R.string.product_list_title_best, ProductListPreset.Best.titleResId())
        assertEquals(R.string.product_list_title_discount, ProductListPreset.Discount.titleResId())
        assertEquals(R.string.product_list_title_today, ProductListPreset.Today.titleResId())
        assertEquals(R.string.product_list_title_recommended, ProductListPreset.Recommended.titleResId())
    }

    // 기본 productList 구현이 preset 원본과 카테고리 필터를 실제로 적용하는지 확인한다.
    @Test
    fun defaultProductListUsesPresetSourceWithCategoryFilter() {
        assertEquals(
            listOf("best-1", "best-2"),
            ids(provider.productList(categoryId = null, preset = ProductListPreset.Best).products),
        )
        assertEquals(
            listOf("discount-2"),
            ids(provider.productList(categoryId = "protective", preset = ProductListPreset.Discount).products),
        )
    }

    private fun ids(products: List<CatalogProductUiModel>): List<String> = products.map { product -> product.id }

    private fun product(id: String, categoryId: String) = CatalogProductUiModel(
        id = id,
        name = id,
        price = CatalogPriceUiModel.Undisclosed,
        categoryId = categoryId,
    )

    private class FakePresetProvider(
        private val products: Map<ProductListPreset, List<CatalogProductUiModel>>,
    ) : ShoppingScreenDataProvider {
        override fun presetProducts(preset: ProductListPreset): List<CatalogProductUiModel> = products.getValue(preset)
    }
}
