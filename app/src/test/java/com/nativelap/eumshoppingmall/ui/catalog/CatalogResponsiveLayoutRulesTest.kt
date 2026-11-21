package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogResponsiveLayoutRulesTest {
    @Test
    fun productColumnsAdaptToWidthAndTextScale() {
        assertEquals(1, catalogProductColumnCount(320.dp, 1f))
        assertEquals(1, catalogProductColumnCount(359.dp, 1f))
        assertEquals(2, catalogProductColumnCount(360.dp, 1f))
        assertEquals(2, catalogProductColumnCount(390.dp, 1f))
        assertEquals(2, catalogProductColumnCount(599.dp, 1f))
        assertEquals(3, catalogProductColumnCount(800.dp, 1f))
        assertEquals(3, catalogProductColumnCount(959.dp, 1f))
        assertEquals(4, catalogProductColumnCount(960.dp, 1f))
        assertEquals(2, catalogProductColumnCount(390.dp, 1.34f))
        assertEquals(1, catalogProductColumnCount(390.dp, 1.35f))
    }

    @Test
    fun catalogControlsStackOnlyWhenConstrained() {
        assertTrue(shouldStackCatalogControls(320.dp, 1f))
        assertTrue(shouldStackCatalogControls(359.dp, 1f))
        assertFalse(shouldStackCatalogControls(360.dp, 1f))
        assertFalse(shouldStackCatalogControls(390.dp, 1.29f))
        assertTrue(shouldStackCatalogControls(390.dp, 1.3f))
        assertFalse(shouldStackCatalogControls(390.dp, 1f))
    }
}
