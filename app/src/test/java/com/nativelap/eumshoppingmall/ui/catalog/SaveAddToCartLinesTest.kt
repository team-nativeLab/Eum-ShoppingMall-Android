package com.nativelap.eumshoppingmall.ui.catalog

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class SaveAddToCartLinesTest {
    @Test
    fun returnsTrueAndPassesCountWhenSaveSucceeds() = runBlocking {
        var savedCount = -1

        val saved = saveAddToCartLines(addedCount = 3) { count -> savedCount = count }

        assertTrue(saved)
        assertEquals(3, savedCount)
    }

    @Test
    fun returnsFalseWhenSaveFails() = runBlocking {
        val saved = saveAddToCartLines(addedCount = 1) { throw IOException("disk full") }

        assertFalse(saved)
    }

    @Test(expected = CancellationException::class)
    fun rethrowsCancellationInsteadOfReportingFailure() = runBlocking {
        saveAddToCartLines(addedCount = 1) { throw CancellationException("cancelled") }
        Unit
    }
}
