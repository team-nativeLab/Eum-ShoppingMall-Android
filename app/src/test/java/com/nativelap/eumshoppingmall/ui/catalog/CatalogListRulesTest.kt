package com.nativelap.eumshoppingmall.ui.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogListRulesTest {
    private val helmet = product("helmet", "protective", "helmet")
    private val boots = product("boots", "protective", "boots")
    private val extinguisher = product("extinguisher", "fire", "extinguisher")
    private val products = listOf(helmet, boots, extinguisher)

    @Test
    fun allChipKeepsEveryProduct() {
        assertEquals(products, filterCatalogProductsBySubcategory(products, CATALOG_ALL_CATEGORY_ID))
    }

    @Test
    fun subcategoryChipKeepsOnlyMatchingProducts() {
        assertEquals(listOf(boots), filterCatalogProductsBySubcategory(products, "boots"))
    }

    @Test
    fun unknownSubcategoryGivesEmptyList() {
        assertTrue(filterCatalogProductsBySubcategory(products, "missing").isEmpty())
    }

    @Test
    fun clearedSearchQueryIsBlankOnlyNotNonEmptyText() {
        assertTrue(isClearedSearchQuery(""))
        assertTrue(isClearedSearchQuery("   "))
        assertFalse(isClearedSearchQuery("안전모"))
    }

    private fun product(id: String, categoryId: String, subcategoryId: String) = CatalogProductUiModel(
        id = id,
        name = id,
        price = CatalogPriceUiModel.QuoteRequired,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
    )

    @Test
    fun quotePickerHidesFavoriteToggleButOtherListsKeepIt() {
        assertFalse(showsFavoriteToggle(isQuotePicker = true))
        assertTrue(showsFavoriteToggle(isQuotePicker = false))
    }
}

class CatalogListRulesCartTest {
    @Test
    fun quotePickerHidesBlockReasonChipsButCartListsKeepThem() {
        assertFalse(showsBlockReasonChip(isQuotePicker = true))
        assertTrue(showsBlockReasonChip(isQuotePicker = false))
    }

    @Test
    fun listRowCartButtonNeedsEligibleProductAndCallerAction() {
        val eligible = CatalogProductUiModel(
            id = "p",
            name = "p",
            price = CatalogPriceUiModel.Available("₩18,000"),
            hasOptions = false,
        )

        assertTrue(quickCartButtonVisible(eligible, hasQuickCartAction = true))
        assertFalse(quickCartButtonVisible(eligible, hasQuickCartAction = false))
        assertFalse(quickCartButtonVisible(eligible.copy(hasOptions = true), hasQuickCartAction = true))
    }

    private fun CatalogProductUiModel.copy(hasOptions: Boolean?) = CatalogProductUiModel(id = id, name = name, price = price, hasOptions = hasOptions)
}
