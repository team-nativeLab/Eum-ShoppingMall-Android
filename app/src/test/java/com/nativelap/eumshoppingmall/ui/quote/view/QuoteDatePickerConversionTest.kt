package com.nativelap.eumshoppingmall.ui.quote.view

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuoteDatePickerConversionTest {
    @Test fun convertsIsoDatesUsingUtcWithoutDayShift() {
        listOf("2026-01-01", "2026-11-18", "2026-12-31").forEach { date ->
            assertEquals(date, date.toDatePickerUtcMillis()?.let(::datePickerUtcMillisToIsoDate))
        }
    }

    @Test fun invalidManualValueDoesNotInitializePicker() {
        assertNull("2026-02-30".toDatePickerUtcMillis())
        assertNull("18/11/2026".toDatePickerUtcMillis())
    }

    @Test fun cancelAndEmptyConfirmKeepCurrentValue() {
        val current = "2026-11-18"
        val selected = "2026-12-31".toDatePickerUtcMillis()
        assertEquals(current, datePickerResult(current, selected, confirmed = false))
        assertEquals(current, datePickerResult(current, null, confirmed = true))
        assertEquals("2026-12-31", datePickerResult(current, selected, confirmed = true))
    }
}
