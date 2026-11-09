package com.nativelap.eumshoppingmall.ui.quote

import com.nativelap.eumshoppingmall.ui.quote.view.withAddedQuoteProduct
import org.junit.Assert.assertEquals
import org.junit.Test

class QuoteProductIdsTest {
    @Test
    fun pickedProductIsAppendedToQuoteProducts() {
        assertEquals(listOf("p1", "p2"), listOf("p1").withAddedQuoteProduct("p2"))
    }

    @Test
    fun pickedProductAlreadyInQuoteIsNotDuplicated() {
        assertEquals(listOf("p1", "p2"), listOf("p1", "p2").withAddedQuoteProduct("p1"))
    }

    @Test
    fun firstPickedProductStartsEmptyQuoteList() {
        assertEquals(listOf("p1"), emptyList<String>().withAddedQuoteProduct("p1"))
    }
}
