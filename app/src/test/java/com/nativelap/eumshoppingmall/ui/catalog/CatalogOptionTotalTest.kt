package com.nativelap.eumshoppingmall.ui.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CatalogOptionTotalTest {
    @Test
    fun totalIsExtraAmountTimesQuantitySummedWithoutBasePrice() {
        val selected = listOf(
            selectedOption("size-l", unitPriceWon = 3_000L, quantity = 2),
            selectedOption("size-xl", unitPriceWon = 1_000L, quantity = 1),
        )

        assertEquals(7_000L, calculateSelectedOptionTotalWon(selected))
    }

    @Test
    fun totalIsNullWhenNothingIsSelected() {
        assertNull(calculateSelectedOptionTotalWon(emptyList()))
    }

    @Test
    fun totalIsNullWhenAnyOptionExtraIsUnknown() {
        val selected = listOf(
            selectedOption("size-l", unitPriceWon = 3_000L),
            selectedOption("size-xl", unitPriceWon = null),
        )

        assertNull(calculateSelectedOptionTotalWon(selected))
    }

    @Test
    fun zeroWonOptionsHideTheTotal() {
        val selected = listOf(selectedOption("size-l", unitPriceWon = 0L))

        assertNull(calculateSelectedOptionTotalWon(selected))
    }

    @Test
    fun zeroExtraSumAcrossOptionsHidesTheTotal() {
        val selected = listOf(
            selectedOption("size-l", unitPriceWon = 2_000L),
            selectedOption("size-xl", unitPriceWon = -2_000L),
        )

        assertNull(calculateSelectedOptionTotalWon(selected))
    }

    @Test
    fun quantityBelowOneIsCountedAsOne() {
        val selected = listOf(selectedOption("size-l", unitPriceWon = 5_000L, quantity = 0))

        assertEquals(5_000L, calculateSelectedOptionTotalWon(selected))
    }

    @Test
    fun overflowHidesTheTotalInsteadOfWrapping() {
        val selected = listOf(selectedOption("size-l", unitPriceWon = Long.MAX_VALUE, quantity = 2))

        assertNull(calculateSelectedOptionTotalWon(selected))
    }

    @Test
    fun addingToMaximumHidesTheTotal() {
        val selected = listOf(
            selectedOption("size-l", unitPriceWon = Long.MAX_VALUE),
            selectedOption("size-xl", unitPriceWon = 1L),
        )

        assertNull(calculateSelectedOptionTotalWon(selected))
    }

    @Test
    fun formatsWonWithThousandsSeparator() {
        assertEquals("₩18,000", formatWonLabel(18_000L))
    }

    private fun selectedOption(
        optionId: String,
        unitPriceWon: Long?,
        quantity: Int = 1,
    ): CatalogSelectedOptionUiModel = CatalogSelectedOptionUiModel(
        groupId = "size",
        option = CatalogOptionUiModel(
            id = optionId,
            label = optionId,
            quantity = quantity,
            priceLabel = null,
            unitPriceWon = unitPriceWon,
        ),
    )
}
