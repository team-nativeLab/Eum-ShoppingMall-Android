package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.catalog.CatalogCategoryDefinitions
import com.nativelap.eumshoppingmall.ui.home.HomeHeroActionId
import com.nativelap.eumshoppingmall.ui.screendata.EmptyShoppingScreenDataProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class HomeDiscoveryDestinationTest {
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
            assertNotNull(category.labelResId)
            assertNotNull(category.iconResId)
        }
    }

    @Test
    fun eachCategoryResolvesToItsProductListAndRemainsPublic() {
        assertEquals(EumDestination.ProductList(null), destinationForCategory(null))
        CatalogCategoryDefinitions.all.forEach { category ->
            val destination = destinationForCategory(category.id)

            assertEquals(EumDestination.ProductList(category.id), destination)
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
