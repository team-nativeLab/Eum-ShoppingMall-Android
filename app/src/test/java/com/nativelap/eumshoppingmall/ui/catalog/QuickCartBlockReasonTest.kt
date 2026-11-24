package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.ui.home.HomeProductUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuickCartBlockReasonTest {
    private val plain = CatalogProductUiModel(
        id = "sample",
        name = "샘플",
        price = CatalogPriceUiModel.Available("₩18,000"),
        hasOptions = false,
    )

    @Test
    fun soldOutProductShowsSoldOutReasonEvenWhenItAlsoNeedsOptions() {
        assertEquals(QuickCartBlockReason.SoldOut, quickCartBlockReason(plain.copy(isSoldOut = true)))
        assertEquals(QuickCartBlockReason.SoldOut, quickCartBlockReason(plain.copy(isSoldOut = true, hasOptions = true)))
    }

    @Test
    fun optionProductShowsOptionReasonAndPlainPurchasableHasNoReason() {
        assertEquals(QuickCartBlockReason.OptionRequired, quickCartBlockReason(plain.copy(hasOptions = true)))
        assertNull(quickCartBlockReason(plain))
    }

    @Test
    fun undisclosedProductHasNoReasonChipSoButtonStaysAbsent() {
        assertNull(quickCartBlockReason(plain.copy(price = CatalogPriceUiModel.Undisclosed)))
    }

    @Test
    fun homeCardUsesSameReasonRules() {
        val home = HomeProductUiModel(id = "h", name = "홈", priceLabel = "₩18,000", unitPriceWon = 18_000L, hasOptions = true)

        assertEquals(QuickCartBlockReason.OptionRequired, homeQuickCartBlockReason(home))
        assertEquals(QuickCartBlockReason.SoldOut, homeQuickCartBlockReason(home.copy(hasOptions = false, isSoldOut = true)))
        assertNull(homeQuickCartBlockReason(home.copy(hasOptions = false)))
        assertNull(homeQuickCartBlockReason(home.copy(hasOptions = false, isPurchaseUnavailable = true)))
    }

    private fun CatalogProductUiModel.copy(
        isSoldOut: Boolean = this.isSoldOut,
        hasOptions: Boolean? = this.hasOptions,
        price: CatalogPriceUiModel = this.price,
    ) = CatalogProductUiModel(id = id, name = name, price = price, hasOptions = hasOptions, isSoldOut = isSoldOut)

    private fun HomeProductUiModel.copy(
        hasOptions: Boolean? = this.hasOptions,
        isSoldOut: Boolean = this.isSoldOut,
        isPurchaseUnavailable: Boolean = this.isPurchaseUnavailable,
    ) = HomeProductUiModel(
        id = id,
        name = name,
        priceLabel = priceLabel,
        unitPriceWon = unitPriceWon,
        hasOptions = hasOptions,
        isSoldOut = isSoldOut,
        isPurchaseUnavailable = isPurchaseUnavailable,
    )
}
