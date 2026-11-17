package com.nativelap.eumshoppingmall.ui.cart

import androidx.compose.runtime.saveable.SaverScope
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartPriceLabels
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.FallbackQuantitySaver
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartProductAmountWon
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.withFallbackQuantities
import org.junit.Assert.assertEquals
import org.junit.Test

class CartFallbackQuantitiesTest {
    private val labels = CartPriceLabels(priceCheck = "가격 확인 필요", quotePrice = "가격 문의")
    private val saverScope = object : SaverScope {
        override fun canBeSaved(value: Any): Boolean = true
    }

    private fun roundTrip(quantities: Map<String, Int>): Map<String, Int>? {
        val saved = with(FallbackQuantitySaver) { saverScope.save(quantities) }
        return saved?.let { FallbackQuantitySaver.restore(it) }
    }

    private val helmet = CartItemUiModel(
        id = "cart-helmet",
        name = "안전모",
        optionLabel = "사이즈 L",
        quantity = 1,
        unitPriceLabel = "₩18,000",
        totalPriceLabel = "₩18,000",
        isSelected = true,
        lineTotalWon = 18_000L,
        unitPriceWon = 18_000L,
    )

    @Test
    fun savedSampleQuantitySurvivesARestoreAndUpdatesTheSampleTotals() {
        val restored = roundTrip(mapOf("cart-helmet" to 3))

        val items = listOf(helmet).withFallbackQuantities(restored!!, labels)

        assertEquals(3, items.single().quantity)
        assertEquals(54_000L, cartProductAmountWon(items))
    }

    @Test
    fun noSavedQuantityLeavesTheSampleLineUnchanged() {
        assertEquals(listOf(helmet), listOf(helmet).withFallbackQuantities(emptyMap(), labels))
    }

    @Test
    fun savedQuantityOnlyChangesItsOwnLine() {
        val boots = helmet.copy(id = "cart-boots", unitPriceWon = 54_000L, lineTotalWon = 54_000L)

        val items = listOf(helmet, boots).withFallbackQuantities(mapOf("cart-boots" to 2), labels)

        assertEquals(1, items[0].quantity)
        assertEquals(2, items[1].quantity)
        assertEquals(108_000L, items[1].lineTotalWon)
    }

    @Test
    fun emptyMapRestoresToNullWhichRememberSaveableReplacesWithItsEmptyInitialValue() {
        // 빈 목록 저장은 복원에서 null이 된다. rememberSaveable은 null 복원을 초기값(빈 맵)으로 바꾸므로 동작은 같다.
        assertEquals(emptyMap<String, Int>(), roundTrip(emptyMap()).orEmpty())
    }
}
