package com.nativelap.eumshoppingmall.ui.cart

import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartPriceLabels
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartProductAmountWon
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartUiModelOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CartLineUiMappingTest {
    private val labels = CartPriceLabels(priceCheck = "가격 확인 필요", quotePrice = "가격 문의")

    @Test
    fun savedLinesAreShownBeforeTheFallbackList() {
        val fallback = CartUiModel(productAmountLabel = "fallback")

        val shown = cartUiModelOf(listOf(line(productId = "helmet")), labels, fallback = { fallback })

        assertEquals(listOf("helmet/size-l"), shown.items.map { it.id })
    }

    @Test
    fun fallbackIsUsedOnlyWhenNothingIsSaved() {
        val fallback = CartUiModel(productAmountLabel = "fallback")

        assertEquals(fallback, cartUiModelOf(emptyList(), labels, fallback = { fallback }))
    }

    @Test
    fun knownUnitPriceAddsExtraAndShowsWonLabels() {
        val item = cartUiModelOf(listOf(line(optionExtraWon = 3_000L, quantity = 2, unitPriceWon = 18_000L)), labels, fallback = { CartUiModel() })
            .items.single()

        assertEquals("₩21,000 × 2", item.unitPriceLabel)
        assertEquals("₩42,000", item.totalPriceLabel)
        assertEquals(42_000L, item.lineTotalWon)
        assertEquals("사이즈 L (+₩3,000)", item.optionLabel)
    }

    @Test
    fun unknownUnitPriceShowsPriceCheckAndNoLineTotal() {
        val item = cartUiModelOf(listOf(line(quantity = 2)), labels, fallback = { CartUiModel() }).items.single()

        assertEquals("가격 확인 필요", item.unitPriceLabel)
        assertEquals("가격 확인 필요", item.totalPriceLabel)
        assertNull(item.lineTotalWon)
        assertTrue(item.isSelected)
    }

    @Test
    fun quoteLinesKeepTheQuoteLabelAndAreNotSelected() {
        val item = cartUiModelOf(listOf(line(isQuoteRequired = true, unitPriceWon = 18_000L)), labels, fallback = { CartUiModel() })
            .items.single()

        assertEquals("가격 문의", item.unitPriceLabel)
        assertEquals(CartItemType.QuoteRequired, item.type)
        assertFalse(item.isSelected)
        assertNull(item.lineTotalWon)
    }

    @Test
    fun selectedKnownLinesSumIntoProductAmountButUnknownSelectedLineMakesItUnknown() {
        val known = cartUiModelOf(
            listOf(line(productId = "a", quantity = 2, optionExtraWon = 0L, unitPriceWon = 10_000L)),
            labels,
            fallback = { CartUiModel() },
        ).items
        val unknown = cartUiModelOf(
            listOf(line(productId = "b", optionExtraWon = 0L, unitPriceWon = null)),
            labels,
            fallback = { CartUiModel() },
        ).items

        assertEquals(20_000L, cartProductAmountWon(known))
        assertNull(cartProductAmountWon(known + unknown))
    }

    @Test
    fun zeroExtraKeepsPlainOptionLabel() {
        val item = cartUiModelOf(listOf(line(optionExtraWon = 0L, unitPriceWon = 5_000L)), labels, fallback = { CartUiModel() }).items.single()

        assertEquals("사이즈 L", item.optionLabel)
        assertEquals("₩5,000", item.unitPriceLabel)
    }

    @Test
    fun overflowingLineTotalIsUnknownAndShowsPriceCheck() {
        val item = cartUiModelOf(
            listOf(line(quantity = 2, optionExtraWon = 0L, unitPriceWon = Long.MAX_VALUE)),
            labels,
            fallback = { CartUiModel() },
        ).items.single()

        assertNull(item.lineTotalWon)
        assertEquals("가격 확인 필요", item.totalPriceLabel)
    }

    @Test
    fun knownBasePriceWithUnknownExtraIsUnknownPrice() {
        val item = cartUiModelOf(
            listOf(line(quantity = 1, optionExtraWon = null, unitPriceWon = 18_000L)),
            labels,
            fallback = { CartUiModel() },
        ).items.single()

        assertNull(item.lineTotalWon)
        assertEquals("가격 확인 필요", item.unitPriceLabel)
        assertEquals("가격 확인 필요", item.totalPriceLabel)
    }

    private fun line(
        productId: String = "helmet",
        quantity: Int = 1,
        optionExtraWon: Long? = null,
        isQuoteRequired: Boolean = false,
        unitPriceWon: Long? = null,
    ) = CartLine(
        productId = productId,
        productName = "안전모",
        optionId = "size-l",
        optionLabel = "사이즈 L",
        quantity = quantity,
        optionExtraWon = optionExtraWon,
        productPriceLabel = "₩18,000",
        isQuoteRequired = isQuoteRequired,
        productUnitPriceWon = unitPriceWon,
    )
}
