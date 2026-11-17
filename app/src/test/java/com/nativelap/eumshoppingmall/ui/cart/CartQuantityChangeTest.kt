package com.nativelap.eumshoppingmall.ui.cart

import com.nativelap.eumshoppingmall.data.local.CartLine
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartPriceLabels
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartProductAmountWon
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartUiModelOf
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.withQuantity
import com.nativelap.eumshoppingmall.ui.catalog.cartShippingFeeWon
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CartQuantityChangeTest {
    private val labels = CartPriceLabels(priceCheck = "가격 확인 필요", quotePrice = "가격 문의")

    @Test
    fun quantityChangeDoublesTheLineTotalAndRewritesItsLabels() {
        val helmet = orderableItem(unitPriceWon = 18_000L)

        val doubled = helmet.withQuantity(2, labels)

        assertEquals(2, doubled.quantity)
        assertEquals(36_000L, doubled.lineTotalWon)
        assertEquals("₩36,000", doubled.totalPriceLabel)
        assertEquals("₩18,000 × 2", doubled.unitPriceLabel)
    }

    @Test
    fun threeHelmetsCrossTheFreeShippingThresholdAndTheSummaryFollows() {
        val helmet = orderableItem(unitPriceWon = 18_000L).withQuantity(3, labels)
        val items = listOf(helmet)

        assertEquals(54_000L, cartProductAmountWon(items))
        assertEquals(0L, cartShippingFeeWon(cartProductAmountWon(items)!!))
    }

    @Test
    fun shippingBoundaryIsFiftyThousandOnTheSelectedProductTotal() {
        // 합계 49,999원: 기준 미만이라 배송비 3,000원
        assertEquals(3_000L, cartShippingFeeWon(49_999L))
        // 합계 50,000원: 기준 이상이라 무료
        assertEquals(0L, cartShippingFeeWon(50_000L))
        // 단가 24,999원 × 2 = 49,998원: 기준 미만
        val justBelow = orderableItem(unitPriceWon = 24_999L).withQuantity(2, labels)
        assertEquals(49_998L, cartProductAmountWon(listOf(justBelow)))
        assertEquals(3_000L, cartShippingFeeWon(cartProductAmountWon(listOf(justBelow))!!))
        // 단가 25,000원 × 2 = 50,000원: 기준 이상
        val atThreshold = orderableItem(unitPriceWon = 25_000L).withQuantity(2, labels)
        assertEquals(50_000L, cartProductAmountWon(listOf(atThreshold)))
        assertEquals(0L, cartShippingFeeWon(cartProductAmountWon(listOf(atThreshold))!!))
    }

    @Test
    fun savedLineQuantityChangeGivesTheSameTotalsThroughTheStoredLine() {
        val saved = cartUiModelOf(listOf(savedLine(unitPriceWon = 18_000L, quantity = 3)), labels) { CartUiModel() }.items.single()

        assertEquals(54_000L, saved.lineTotalWon)
        assertEquals("₩54,000", saved.totalPriceLabel)
        assertEquals(0L, cartShippingFeeWon(cartProductAmountWon(listOf(saved))!!))
    }

    @Test
    fun unknownUnitPriceKeepsPriceCheckAndNoLineTotalWhenQuantityChanges() {
        val unknown = orderableItem(unitPriceWon = null).withQuantity(2, labels)

        assertEquals(2, unknown.quantity)
        assertNull(unknown.lineTotalWon)
        assertEquals("가격 확인 필요", unknown.totalPriceLabel)
    }

    @Test
    fun quoteLineQuantityChangeKeepsQuoteLabels() {
        val quote = CartItemUiModel(
            id = "quote",
            name = "견적 상품",
            optionLabel = "기본",
            quantity = 1,
            unitPriceLabel = "가격 문의",
            totalPriceLabel = "가격 문의",
            type = CartItemType.QuoteRequired,
        ).withQuantity(2, labels)

        assertEquals(2, quote.quantity)
        assertEquals("가격 문의", quote.totalPriceLabel)
        assertNull(quote.lineTotalWon)
    }

    private fun orderableItem(unitPriceWon: Long?) = CartItemUiModel(
        id = "helmet/size-l",
        name = "안전모",
        optionLabel = "사이즈 L",
        quantity = 1,
        unitPriceLabel = if (unitPriceWon == null) "가격 확인 필요" else "₩18,000",
        totalPriceLabel = if (unitPriceWon == null) "가격 확인 필요" else "₩18,000",
        isSelected = true,
        lineTotalWon = unitPriceWon,
        unitPriceWon = unitPriceWon,
    )

    private fun savedLine(unitPriceWon: Long, quantity: Int) = CartLine(
        productId = "helmet",
        productName = "안전모",
        optionId = "size-l",
        optionLabel = "사이즈 L",
        quantity = quantity,
        optionExtraWon = 0L,
        productPriceLabel = "₩18,000",
        isQuoteRequired = false,
        productUnitPriceWon = unitPriceWon,
    )
}
