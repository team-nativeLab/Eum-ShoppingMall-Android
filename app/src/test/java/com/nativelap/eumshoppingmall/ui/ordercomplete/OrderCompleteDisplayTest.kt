package com.nativelap.eumshoppingmall.ui.ordercomplete

import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.isOrderDetailsAvailable
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.orderCompleteValueOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OrderCompleteDisplayTest {
    @Test
    fun emptyOrderNumberDisablesOrderDetails() {
        assertFalse(isOrderDetailsAvailable(""))
    }

    @Test
    fun whitespaceOnlyOrderNumberDisablesOrderDetails() {
        assertFalse(isOrderDetailsAvailable("   "))
    }

    @Test
    fun normalOrderNumberKeepsOrderDetailsEnabled() {
        assertTrue(isOrderDetailsAvailable("2026110001"))
    }

    @Test
    fun emptyOrPaddedBlankValueHasNoDisplayValue() {
        assertNull(orderCompleteValueOrNull(""))
        assertNull(orderCompleteValueOrNull(" "))
    }

    @Test
    fun normalValueIsKeptAsIs() {
        assertEquals("₩72,000", orderCompleteValueOrNull("₩72,000"))
    }
}
