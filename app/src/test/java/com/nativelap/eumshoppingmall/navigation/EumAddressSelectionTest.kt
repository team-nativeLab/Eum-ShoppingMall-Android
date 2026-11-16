package com.nativelap.eumshoppingmall.navigation

import androidx.compose.runtime.mutableStateOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class EumAddressSelectionTest {
    private val addressSelection = EumAddressSelection(mutableStateOf(null))

    @Test
    fun startsEmpty() {
        assertNull(addressSelection.pendingAddressId)
    }

    @Test
    fun selectKeepsOnlyLatestAddressIdUntilConsumed() {
        addressSelection.select("addr-1")
        addressSelection.select("addr-2")

        assertEquals("addr-2", addressSelection.pendingAddressId)

        addressSelection.consume()

        assertNull(addressSelection.pendingAddressId)
    }
}
