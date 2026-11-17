package com.nativelap.eumshoppingmall.ui.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ShippingPolicyTest {
    @Test
    fun productBelowThresholdIsNotFreeShippingAndShowsTheStandardFee() {
        assertEquals("배송비 ₩3,000", productShippingFeeLabel(49_999L))
        assertEquals("₩3,000", productShippingFeeValue(49_999L))
    }

    @Test
    fun productAtThresholdIsFreeShipping() {
        assertEquals("무료배송", productShippingFeeLabel(50_000L))
        assertEquals("무료", productShippingFeeValue(50_000L))
    }

    @Test
    fun productWithUnknownPriceHasNoShippingLabel() {
        assertNull(productShippingFeeLabel(null))
        assertNull(productShippingFeeValue(null))
    }

    @Test
    fun cartShippingUsesTheSelectedProductTotalSoItMayDifferFromProductChips() {
        // 상품별로 보면 두 상품 모두 기준 미만이라 칩은 "배송비 ₩3,000"이다.
        // 장바구니는 선택 상품금액 합계로 판정하므로 합계 99,998원은 기준 이상이라 배송비가 0원이다. 이 차이는 의도다.
        assertEquals("배송비 ₩3,000", productShippingFeeLabel(49_999L))
        assertEquals(0L, cartShippingFeeWon(49_999L + 49_999L))
    }

    @Test
    fun cartBelowThresholdChargesTheStandardFee() {
        assertEquals(3_000L, cartShippingFeeWon(49_999L))
        assertEquals(0L, cartShippingFeeWon(50_000L))
    }
}
