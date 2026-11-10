package com.nativelap.eumshoppingmall.ui.cart

import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartProductAmountWon
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CartSummaryTest {
    @Test
    fun productAmountSumsOnlySelectedOrderableLines() {
        val items = listOf(
            item(id = "a", lineTotalWon = 18_000L, isSelected = true),
            item(id = "b", lineTotalWon = 5_000L, isSelected = false),
        )

        assertEquals(18_000L, cartProductAmountWon(items))
    }

    @Test
    fun nothingSelectedGivesZeroNotUnknown() {
        assertEquals(0L, cartProductAmountWon(listOf(item(id = "a", lineTotalWon = 18_000L, isSelected = false))))
    }

    @Test
    fun selectedLineWithUnknownTotalMakesAmountUnknown() {
        val items = listOf(item(id = "a", lineTotalWon = 18_000L), item(id = "b", lineTotalWon = null))

        assertNull(cartProductAmountWon(items))
    }

    @Test
    fun soldOutAndQuoteLinesDoNotCountAsOrderable() {
        val items = listOf(
            item(id = "sold", lineTotalWon = 9_000L, isSoldOut = true),
            item(id = "quote", type = CartItemType.QuoteRequired, lineTotalWon = null, isSelected = true),
        )

        assertEquals(0L, cartProductAmountWon(items))
    }

    @Test
    fun overflowMakesAmountUnknownInsteadOfWrapping() {
        val items = listOf(item(id = "a", lineTotalWon = Long.MAX_VALUE), item(id = "b", lineTotalWon = 1L))

        assertNull(cartProductAmountWon(items))
    }

    private fun item(
        id: String,
        type: CartItemType = CartItemType.Orderable,
        lineTotalWon: Long? = null,
        isSelected: Boolean = true,
        isSoldOut: Boolean = false,
    ) = CartItemUiModel(
        id = id,
        name = id,
        optionLabel = "",
        quantity = 1,
        unitPriceLabel = "",
        totalPriceLabel = "",
        type = type,
        isSelected = isSelected,
        isSoldOut = isSoldOut,
        lineTotalWon = lineTotalWon,
    )
}
