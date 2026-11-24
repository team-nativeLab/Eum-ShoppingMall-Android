package com.nativelap.eumshoppingmall.ui.home

import com.nativelap.eumshoppingmall.ui.catalog.CATALOG_DEFAULT_OPTION_ID
import com.nativelap.eumshoppingmall.ui.catalog.canQuickAddToHomeProduct
import com.nativelap.eumshoppingmall.ui.catalog.homeQuickCartButtonVisible
import com.nativelap.eumshoppingmall.ui.catalog.resolveHomeQuickCartLine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeQuickCartTest {
    private val plain = HomeProductUiModel(
        id = "sample",
        name = "샘플",
        priceLabel = "₩18,000",
        unitPriceWon = 18_000L,
        hasOptions = false,
    )

    @Test
    fun explicitNoOptionPurchasableHomeProductCanBeAddedFromCard() {
        assertTrue(canQuickAddToHomeProduct(plain))
    }

    @Test
    fun unknownOptionOptionProductPriceUndisclosedSoldOutAndMissingPriceCannotBeAdded() {
        assertFalse(canQuickAddToHomeProduct(plain.copy(hasOptions = null)))
        assertFalse(canQuickAddToHomeProduct(plain.copy(hasOptions = true)))
        assertFalse(canQuickAddToHomeProduct(plain.copy(isPurchaseUnavailable = true)))
        assertFalse(canQuickAddToHomeProduct(plain.copy(isSoldOut = true)))
        assertFalse(canQuickAddToHomeProduct(plain.copy(priceLabel = null)))
    }

    @Test
    fun homeCardButtonHiddenWithoutCallerAction() {
        assertFalse(homeQuickCartButtonVisible(plain, hasQuickCartAction = false))
        assertTrue(homeQuickCartButtonVisible(plain, hasQuickCartAction = true))
    }

    @Test
    fun homeCartLineUsesDefaultOptionAndUnitPrice() {
        val line = resolveHomeQuickCartLine(plain, defaultOptionLabel = "기본 구성")

        assertEquals("sample", line?.productId)
        assertEquals(CATALOG_DEFAULT_OPTION_ID, line?.optionId)
        assertEquals(18_000L, line?.productUnitPriceWon)
        assertEquals(1, line?.quantity)
        assertNull(resolveHomeQuickCartLine(plain.copy(hasOptions = null), defaultOptionLabel = "기본 구성"))
    }
}
