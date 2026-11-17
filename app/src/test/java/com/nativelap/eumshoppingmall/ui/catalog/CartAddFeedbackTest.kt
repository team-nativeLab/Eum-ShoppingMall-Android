package com.nativelap.eumshoppingmall.ui.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CartAddFeedbackTest {
    @Test
    fun leavingScreenClearsOnlyTheOutcomeItShowed() {
        assertTrue(shouldClearCartOutcomeOnLeave(pendingSerial = 7L, shownSerial = 7L))
    }

    @Test
    fun newerOutcomeAfterLeavingIsKept() {
        assertFalse(shouldClearCartOutcomeOnLeave(pendingSerial = 8L, shownSerial = 7L))
    }

    @Test
    fun nothingToClearWhenNoOutcomeIsPending() {
        assertFalse(shouldClearCartOutcomeOnLeave(pendingSerial = null, shownSerial = null))
        assertFalse(shouldClearCartOutcomeOnLeave(pendingSerial = null, shownSerial = 7L))
    }
}
