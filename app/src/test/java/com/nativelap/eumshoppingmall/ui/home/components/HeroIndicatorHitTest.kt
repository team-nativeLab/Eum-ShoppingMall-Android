package com.nativelap.eumshoppingmall.ui.home.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HeroIndicatorHitTest {
    private val centers = listOf(0f, 10f, 20f)

    @Test
    fun tapNearDotCenterSelectsThatDot() {
        assertEquals(0, nearestHeroIndicatorIndex(tapX = 1f, centersX = centers))
        assertEquals(1, nearestHeroIndicatorIndex(tapX = 9f, centersX = centers))
        assertEquals(2, nearestHeroIndicatorIndex(tapX = 19f, centersX = centers))
    }

    @Test
    fun tapBetweenDotsGoesToNearestDotAndFarTapsClampToEnds() {
        assertEquals(0, nearestHeroIndicatorIndex(tapX = 4f, centersX = centers))
        assertEquals(2, nearestHeroIndicatorIndex(tapX = 100f, centersX = centers))
    }

    @Test
    fun noMeasuredCentersMeansNoSelection() {
        assertNull(nearestHeroIndicatorIndex(tapX = 5f, centersX = emptyList()))
    }
}
