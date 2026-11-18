package com.nativelap.eumshoppingmall.ui.checkout.viewmodel

import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class CheckoutOrderSnapshotTest {
    @Test fun selectedLinesKeepQuantityOptionPriceAndSavedIdsOnly() {
        val lines = listOf(
            cartLine("helmet", 2, 18_000L, 1_500L),
            cartLine("gloves", 1, 10_000L, 0L),
        )
        val order = checkoutOrderSnapshot(lines.take(1), CheckoutOrderSource.SavedCart)!!
        assertEquals(setOf("helmet/default"), order.cartLineIdsToRemove)
        assertEquals(39_000L, order.productTotalWon)
        assertEquals(3_000L, order.shippingFeeWon)
        assertEquals(42_000L, order.finalTotalWon)
        assertEquals("옵션", order.lines.single().optionLabel)
    }

    @Test fun fiftyThousandWonGetsFreeShippingAndDirectBuyRemovesNothing() {
        val order = checkoutOrderSnapshot(listOf(cartLine("bulk", 2, 25_000L, 0L)), CheckoutOrderSource.BuyNow)!!
        assertEquals(0L, order.shippingFeeWon)
        assertEquals(50_000L, order.finalTotalWon)
        assertTrue(order.cartLineIdsToRemove.isEmpty())
    }

    @Test fun unknownPriceAndOverflowNeverProduceConfirmedTotal() {
        val unknown = checkoutOrderSnapshot(listOf(cartLine("unknown", 1, null, 0L)), CheckoutOrderSource.SavedCart)!!
        assertNull(unknown.productTotalWon)
        assertNull(unknown.finalTotalWon)
        assertFalse(canCompleteCheckoutPayment(unknown))
        val overflow = checkoutOrderSnapshot(listOf(cartLine("overflow", 2, Long.MAX_VALUE, 1L)), CheckoutOrderSource.SavedCart)!!
        assertNull(overflow.lines.single().lineTotalWon)
        assertNull(overflow.finalTotalWon)
        assertFalse(canCompleteCheckoutPayment(overflow))
    }

    @Test fun confirmedAmountIsRequiredBeforePaymentCompletion() {
        val confirmed = checkoutOrderSnapshot(listOf(cartLine("known", 1, 18_000L, 0L)), CheckoutOrderSource.SavedCart)
        assertTrue(canCompleteCheckoutPayment(confirmed))
    }

    @Test fun orderSnapshotCanBeRestoredFromSaveableState() {
        val order = checkoutOrderSnapshot(listOf(cartLine("helmet", 2, 18_000L, 0L)), CheckoutOrderSource.SavedCart)!!

        val restored = decodeCheckoutOrderSnapshot(encodeCheckoutOrderSnapshot(order))

        assertEquals(order, restored)
        assertEquals(39_000L, restored?.finalTotalWon)
        assertEquals(setOf("helmet/default"), restored?.cartLineIdsToRemove)
    }

    @Test fun quoteOnlyAndNonPositiveQuantityAreRejected() {
        assertNull(checkoutOrderSnapshot(listOf(cartLine("quote", 1, 1L, 0L).copy(isQuoteRequired = true)), CheckoutOrderSource.SavedCart))
        assertNull(checkoutOrderSnapshot(listOf(cartLine("invalid", 0, 1L, 0L)), CheckoutOrderSource.BuyNow))
    }

    @Test fun loadingAndUnresolvedIdsNeverFallBackToSampleLines() {
        val sample = listOf(CheckoutOrderLine("sample", "샘플", "기본", 1, 10_000L))
        assertNull(resolveCheckoutOrderForSelection(null, listOf("sample"), sample))
        assertNull(resolveCheckoutOrderForSelection(emptyList(), listOf("missing"), sample))
        assertNull(resolveCheckoutOrderForSelection(listOf(cartLine("saved", 1, 10_000L, 0L)), listOf("missing"), sample))
    }

    @Test fun debugFallbackUsesCurrentlyDisplayedQuantityAndAmount() {
        val order = checkoutOrderSnapshotFromCartItems(
            listOf(
                CartItemUiModel(
                    id = "helmet",
                    name = "안전모",
                    optionLabel = "기본",
                    quantity = 2,
                    unitPriceLabel = "가격 라벨은 사용하지 않음",
                    totalPriceLabel = "",
                    isSelected = true,
                    lineTotalWon = 36_000L,
                ),
            ),
        )!!

        assertEquals(2, order.lines.single().quantity)
        assertEquals(36_000L, order.productTotalWon)
        assertEquals(39_000L, order.finalTotalWon)
        assertTrue(order.cartLineIdsToRemove.isEmpty())
    }

    private fun cartLine(id: String, quantity: Int, price: Long?, extra: Long?) = CartLine(
        productId = id,
        productName = id,
        optionId = "default",
        optionLabel = "옵션",
        quantity = quantity,
        optionExtraWon = extra,
        productPriceLabel = "label ignored",
        isQuoteRequired = false,
        productUnitPriceWon = price,
    )
}
