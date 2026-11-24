package com.nativelap.eumshoppingmall.ui.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogQuickCartTest {
    private val plain = product(price = CatalogPriceUiModel.Available("₩18,000", unitPriceWon = 18_000L), hasOptions = false)

    @Test
    fun plainPurchasableProductWithExplicitNoOptionsCanBeAddedFromCard() {
        assertTrue(canQuickAddToCart(plain))
    }

    @Test
    fun unknownOptionStateDefaultsToCannotAddFromCard() {
        assertFalse(canQuickAddToCart(plain.copy(hasOptions = null)))
    }

    @Test
    fun optionProductUndisclosedProductAndSoldOutProductCannotBeAddedFromCard() {
        assertFalse(canQuickAddToCart(plain.copy(hasOptions = true)))
        assertFalse(canQuickAddToCart(plain.copy(price = CatalogPriceUiModel.Undisclosed)))
        assertFalse(canQuickAddToCart(plain.copy(isSoldOut = true)))
    }

    @Test
    fun cardButtonHiddenWhenCallerPassesNoQuickCartAction() {
        // 담기 동작을 넘기지 않는 화면에서는 담을 수 있는 상품이어도 버튼이 없다.
        assertFalse(quickCartButtonVisible(plain, hasQuickCartAction = false))
        assertTrue(quickCartButtonVisible(plain, hasQuickCartAction = true))
    }

    @Test
    fun quickCartLineUsesDefaultOptionAndProductPrice() {
        val line = resolveQuickCartLine(plain, defaultOptionLabel = "기본 구성")

        assertEquals("sample", line?.productId)
        assertEquals(CATALOG_DEFAULT_OPTION_ID, line?.optionId)
        assertEquals("기본 구성", line?.optionLabel)
        assertEquals(1, line?.quantity)
        assertEquals("₩18,000", line?.productPriceLabel)
        assertEquals(18_000L, line?.productUnitPriceWon)
        assertFalse(line?.isPriceUndisclosed ?: true)
    }

    @Test
    fun noQuickCartLineForIneligibleProduct() {
        assertNull(resolveQuickCartLine(plain.copy(hasOptions = null), defaultOptionLabel = "기본 구성"))
    }

    @Test
    fun rapidDoubleTapAddsOnlyOnceUntilFirstSaveFinishes() {
        val gate = QuickCartGate()
        var addedCount = 0
        // 담기 저장이 끝나기 전 세 번 눌러도 한 번만 처리된다.
        repeat(3) { if (gate.tryEnter()) addedCount++ }
        assertEquals(1, addedCount)

        gate.exit()
        assertTrue(gate.tryEnter())
    }

    private fun product(
        price: CatalogPriceUiModel,
        hasOptions: Boolean? = null,
        isSoldOut: Boolean = false,
    ) = CatalogProductUiModel(
        id = "sample",
        name = "샘플",
        price = price,
        hasOptions = hasOptions,
        isSoldOut = isSoldOut,
    )

    private fun CatalogProductUiModel.copy(
        hasOptions: Boolean? = this.hasOptions,
        isSoldOut: Boolean = this.isSoldOut,
        price: CatalogPriceUiModel = this.price,
    ) = product(price = price, hasOptions = hasOptions, isSoldOut = isSoldOut)
}
