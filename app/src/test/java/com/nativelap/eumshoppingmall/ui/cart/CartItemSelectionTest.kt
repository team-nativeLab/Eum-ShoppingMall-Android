package com.nativelap.eumshoppingmall.ui.cart

import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.orderableItems
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.selectableItems
import org.junit.Assert.assertEquals
import org.junit.Test

class CartItemSelectionTest {
    @Test
    fun orderableItemsKeepSoldOutItemsButDropQuoteRequiredItems() {
        val items = listOf(
            cartItem(id = "normal"),
            cartItem(id = "sold-out", isSoldOut = true),
            cartItem(id = "quote", type = CartItemType.QuoteRequired),
        )

        assertEquals(listOf("normal", "sold-out"), items.orderableItems().map { it.id })
    }

    @Test
    fun selectableItemsExcludeSoldOutAndQuoteRequiredItems() {
        val items = listOf(
            cartItem(id = "normal"),
            cartItem(id = "sold-out", isSoldOut = true),
            cartItem(id = "quote", type = CartItemType.QuoteRequired),
        )

        assertEquals(listOf("normal"), items.selectableItems().map { it.id })
    }

    @Test
    fun selectableItemsAreEmptyWhenEveryOrderableItemIsSoldOut() {
        val items = listOf(cartItem(id = "sold-out-1", isSoldOut = true), cartItem(id = "sold-out-2", isSoldOut = true))

        assertEquals(0, items.selectableItems().size)
    }

    @Test
    fun selectableItemsAreEmptyForEmptyCart() {
        assertEquals(0, emptyList<CartItemUiModel>().selectableItems().size)
    }

    private fun cartItem(
        id: String,
        type: CartItemType = CartItemType.Orderable,
        isSoldOut: Boolean = false,
    ): CartItemUiModel = CartItemUiModel(
        id = id,
        name = id,
        optionLabel = "",
        quantity = 1,
        unitPriceLabel = "₩1,000",
        totalPriceLabel = "₩1,000",
        type = type,
        isSoldOut = isSoldOut,
    )
}
