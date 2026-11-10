package com.nativelap.eumshoppingmall.ui.cart

import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartExpectedPaymentText
import com.nativelap.eumshoppingmall.ui.catalog.formatWonLabel
import org.junit.Assert.assertEquals
import org.junit.Test

class CartExpectedPaymentTest {
    private val priceCheck = "가격 확인 필요"
    private val atOrder = "배송비는 주문 단계에서 계산돼요"

    @Test
    fun unknownProductAmountShowsPriceCheck() {
        assertEquals(priceCheck, text(productWon = null, shippingWon = 3_000L))
        assertEquals(priceCheck, text(productWon = null, shippingWon = null))
    }

    @Test
    fun knownProductAmountWithUnknownShippingShowsShippingNotice() {
        assertEquals(atOrder, text(productWon = 42_000L, shippingWon = null))
    }

    @Test
    fun knownProductAmountAndShippingShowsTheSum() {
        assertEquals("₩45,000", text(productWon = 42_000L, shippingWon = 3_000L))
        assertEquals("₩42,000", text(productWon = 42_000L, shippingWon = 0L))
    }

    @Test
    fun overflowingSumShowsPriceCheck() {
        assertEquals(priceCheck, text(productWon = Long.MAX_VALUE, shippingWon = 1L))
    }

    private fun text(productWon: Long?, shippingWon: Long?): String = cartExpectedPaymentText(
        productWon = productWon,
        shippingWon = shippingWon,
        priceCheck = priceCheck,
        shippingAtOrder = atOrder,
        formatWon = ::formatWonLabel,
    )
}
