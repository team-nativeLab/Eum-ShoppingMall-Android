package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryDefinitions
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductListUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.home.HomeHeroActionId
import com.nativelap.eumshoppingmall.ui.screendata.EmptyShoppingScreenDataProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HomeDiscoveryDestinationTest {
    private val sampleProduct = CatalogProductUiModel("product-1", "상품", CatalogPriceUiModel.Available("₩1,000", unitPriceWon = 1_000))

    private fun productListOf(products: List<CatalogProductUiModel>) = CatalogProductListUiModel(
        title = "",
        sortLabel = "",
        products = products,
    )

    @Test
    fun heroActionsResolveToApprovedDestinations() {
        assertEquals(EumDestination.ProductList(null), destinationForHomeHeroAction(HomeHeroActionId.BrowseProducts))
        assertEquals(EumDestination.QuoteRequest(), destinationForHomeHeroAction(HomeHeroActionId.RequestQuote))
        assertEquals(EumDestination.CompanyIntro, destinationForHomeHeroAction(HomeHeroActionId.ViewCompany))
    }

    @Test
    fun sharedCategoriesHaveUniqueStableIdsAndHomeUsesSameDefinitions() {
        val categories = CatalogCategoryDefinitions.all

        assertEquals(
            listOf("protective", "uniform", "traffic", "fire", "industrial", "security", "living", "custom"),
            categories.map { it.id },
        )
        assertEquals(categories.size, categories.map { it.id }.toSet().size)
        val homeItems = CatalogCategoryDefinitions.homeItems()
        assertEquals(categories.map { it.id }, homeItems.map { it.id })
        assertEquals(categories.map { it.labelResId }, homeItems.map { it.labelResId })
        assertEquals(categories.map { it.iconResId }, homeItems.map { it.iconResId })
        assertEquals(categories, EmptyShoppingScreenDataProvider.categoryScreen().categories)
        categories.forEach { category ->
            assertNotEquals(0, category.labelResId)
            assertNotEquals(0, category.iconResId)
            // 부제는 현재 노출하지 않는다. 리소스는 남겨 두고 정의에서만 빼 둔다.
            assertNull(category.descriptionResId)
        }
    }

    @Test
    fun categoryWithoutProductsDoesNotCreateDestination() {
        CatalogCategoryDefinitions.all.forEach { category ->
            assertNull(destinationForCategoryIfAvailable(category.id, productListOf(emptyList())))
        }
        // 전체 보기(null)는 상품 수와 관계없이 기존처럼 이동한다.
        assertEquals(EumDestination.ProductList(null), destinationForCategoryIfAvailable(null, productListOf(emptyList())))
    }

    @Test
    fun eachCategoryResolvesToItsProductListAndRemainsPublic() {
        assertEquals(EumDestination.ProductList(null), destinationForCategory(null))
        CatalogCategoryDefinitions.all.forEach { category ->
            val destination = destinationForCategory(category.id)

            assertEquals(EumDestination.ProductList(category.id), destination)
            // 상품이 있는 카테고리는 같은 목적지로 이동하고, 상품이 없으면 이동하지 않는다.
            assertEquals(destination, destinationForCategoryIfAvailable(category.id, productListOf(listOf(sampleProduct))))
            assertNull(destinationForCategoryIfAvailable(category.id, productListOf(emptyList())))
            assertFalse(requiresLogin(destination))
        }
    }

    @Test
    fun allHeroDestinationsRemainPublic() {
        HomeHeroActionId.entries.forEach { action ->
            assertFalse(requiresLogin(destinationForHomeHeroAction(action)))
        }
    }
}
