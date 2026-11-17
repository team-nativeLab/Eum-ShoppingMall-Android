package com.nativelap.eumshoppingmall.ui.catalog

import com.nativelap.eumshoppingmall.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NoOptionGuidanceTest {
    @Test
    fun productWithoutOptionGroupsSaysItIsAddedAsTheDefaultConfiguration() {
        assertEquals(R.string.catalog_option_default_label, noOptionGuidanceRes(isQuoteRequired = false, hasOptionGroups = false))
    }

    @Test
    fun productWithOptionGroupsShowsNoEmptyOptionGuidance() {
        assertNull(noOptionGuidanceRes(isQuoteRequired = false, hasOptionGroups = true))
    }

    @Test
    fun quoteProductShowsNoOptionGuidanceEvenWithoutGroups() {
        assertNull(noOptionGuidanceRes(isQuoteRequired = true, hasOptionGroups = false))
    }
}
