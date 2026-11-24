package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.mutableStateOf
import com.nativelap.eumshoppingmall.ui.productfilter.ProductFilterPageSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductListEmptyStateTest {
    @Test
    fun emptyAfterFilteringOnlyWhenSourceHasProducts() {
        assertTrue(isEmptyByFilter(sourceCount = 5, filteredCount = 0))
        assertFalse(isEmptyByFilter(sourceCount = 0, filteredCount = 0))
        assertFalse(isEmptyByFilter(sourceCount = 5, filteredCount = 2))
    }

    @Test
    fun resetRestoresDefaultCriteriaDraftsAndFirstPage() {
        val criteriaState = mutableStateOf(ProductFilterCriteria(brandIds = setOf("brand-a"), hideSoldOut = true, minPriceManwon = 3))
        val minDraft = mutableStateOf("3")
        val maxDraft = mutableStateOf("9")
        val visibleCount = mutableStateOf(72)
        val state = ProductFilterState(
            isVisibleState = mutableStateOf(false),
            criteriaState = criteriaState,
            minPriceDraftState = minDraft,
            maxPriceDraftState = maxDraft,
            pageSizeState = mutableStateOf(ProductFilterPageSize.TwentyFour),
            visibleCountState = visibleCount,
        )

        state.reset()

        assertEquals(ProductFilterCriteria(), criteriaState.value)
        assertEquals("", minDraft.value)
        assertEquals("", maxDraft.value)
        assertEquals(ProductFilterPageSize.TwentyFour.count, visibleCount.value)
    }
}
