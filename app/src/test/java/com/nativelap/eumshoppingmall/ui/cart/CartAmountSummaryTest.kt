package com.nativelap.eumshoppingmall.ui.cart

import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartAmountSummary
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemType
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.CartItemUiModel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartAmountLabel
import com.nativelap.eumshoppingmall.ui.cart.viewmodel.cartAmountSummary
import com.nativelap.eumshoppingmall.ui.catalog.formatWonLabel
import org.junit.Assert.assertEquals
import org.junit.Test

class CartAmountSummaryTest {
    private val priceCheck = "가격 확인 필요"
    private val partial = "확인된 금액 %1\$s + 가격 확인 필요 %2\$d건"

    @Test
    fun noSelectedLinesShowsZeroWon() {
        val summary = cartAmountSummary(listOf(item(id = "a", lineTotalWon = 18_000L, isSelected = false)))

        assertEquals(CartAmountSummary(0L, 0, 0), summary)
        assertEquals("₩0", label(summary))
    }

    @Test
    fun allSelectedLinesKnownShowsConfirmedAmount() {
        val summary = cartAmountSummary(listOf(item(id = "a", lineTotalWon = 42_000L), item(id = "b", lineTotalWon = 30_000L)))

        assertEquals("₩72,000", label(summary))
    }

    @Test
    fun partlyUnknownSelectedLinesShowConfirmedAmountPlusCount() {
        val summary = cartAmountSummary(
            listOf(item(id = "a", lineTotalWon = 42_000L), item(id = "b", lineTotalWon = 30_000L), item(id = "c", lineTotalWon = null)),
        )

        assertEquals("확인된 금액 ₩72,000 + 가격 확인 필요 1건", label(summary))
    }

    @Test
    fun onlyUnknownSelectedLinesShowPriceCheckOnly() {
        val summary = cartAmountSummary(listOf(item(id = "a", lineTotalWon = null), item(id = "b", lineTotalWon = null)))

        assertEquals(0, summary.knownLineCount)
        assertEquals(priceCheck, label(summary))
    }

    @Test
    fun unselectedUnknownLinesDoNotCount() {
        val summary = cartAmountSummary(listOf(item(id = "a", lineTotalWon = 10_000L), item(id = "b", lineTotalWon = null, isSelected = false)))

        assertEquals("₩10,000", label(summary))
    }

    @Test
    fun quoteLinesAreNotCountedAsUnknownOrderableLines() {
        val summary = cartAmountSummary(listOf(item(id = "q", type = CartItemType.QuoteRequired, lineTotalWon = null, isSelected = true)))

        assertEquals(CartAmountSummary(0L, 0, 0), summary)
    }

    @Test
    fun overflowingLineCountsAsUnknown() {
        val summary = cartAmountSummary(listOf(item(id = "a", lineTotalWon = Long.MAX_VALUE), item(id = "b", lineTotalWon = 1L)))

        assertEquals(1, summary.knownLineCount)
        assertEquals(1, summary.unknownLineCount)
    }

    private fun label(summary: CartAmountSummary): String = cartAmountLabel(
        summary = summary,
        priceCheck = priceCheck,
        partialTemplate = partial,
        formatWon = ::formatWonLabel,
    )

    private fun item(
        id: String,
        type: CartItemType = CartItemType.Orderable,
        lineTotalWon: Long? = null,
        isSelected: Boolean = true,
    ) = CartItemUiModel(
        id = id,
        name = id,
        optionLabel = "",
        quantity = 1,
        unitPriceLabel = "",
        totalPriceLabel = "",
        type = type,
        isSelected = isSelected,
        lineTotalWon = lineTotalWon,
    )
}
