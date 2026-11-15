package com.nativelap.eumshoppingmall.ui.cart

import com.nativelap.eumshoppingmall.ui.cart.view.orderedCartLineIds
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderedCartLineIdsTest {
    @Test
    fun noSelectedItemsGiveEmptyList() {
        val items = listOf(lineOf("a"), lineOf("b"))

        assertEquals(emptyList<String>(), orderedCartLineIds(items))
    }

    @Test
    fun onlySelectedOrderableItemsAreOrdered() {
        val items = listOf(lineOf("a", isSelected = true), lineOf("b"), lineOf("c", isSelected = true))

        assertEquals(listOf("a", "c"), orderedCartLineIds(items))
    }

    @Test
    fun soldOutAndQuoteItemsAreExcludedEvenWhenSelected() {
        val items = listOf(
            lineOf("sold-out", isSelected = true, isSoldOut = true),
            lineOf("quote", isSelected = true, type = CartItemType.QuoteRequired),
            lineOf("normal", isSelected = true),
        )

        assertEquals(listOf("normal"), orderedCartLineIds(items))
    }

    private fun lineOf(
        id: String,
        isSelected: Boolean = false,
        isSoldOut: Boolean = false,
        type: CartItemType = CartItemType.Orderable,
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
    )
}
