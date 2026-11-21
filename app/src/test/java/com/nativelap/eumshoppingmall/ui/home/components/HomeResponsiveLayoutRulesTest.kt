package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeResponsiveLayoutRulesTest {
    @Test
    fun heroActionsStackWhenSpaceOrTextScaleIsConstrained() {
        assertTrue(shouldStackHeroActions(279.dp, 1f))
        assertFalse(shouldStackHeroActions(280.dp, 1f))
        assertFalse(shouldStackHeroActions(390.dp, 1.29f))
        assertTrue(shouldStackHeroActions(390.dp, 1.3f))
        assertFalse(shouldStackHeroActions(390.dp, 1f))
    }

    @Test
    fun heroHeightUsesTheSameStackingDecisionAtCompactWidthsAndLargeText() {
        val compactDefaultTextStacks = shouldStackHeroActions(heroActionContentWidth(320.dp), 1f)
        val compactLargeTextStacks = shouldStackHeroActions(heroActionContentWidth(320.dp), 1.3f)
        val regularLargeTextStacks = shouldStackHeroActions(heroActionContentWidth(360.dp), 1.3f)
        val regularTextBeforeThresholdStacks = shouldStackHeroActions(heroActionContentWidth(390.dp), 1.29f)

        assertEquals(240.dp, heroActionContentWidth(320.dp))
        assertEquals(280.dp, heroActionContentWidth(360.dp))
        assertEquals(272f, homeHeroHeight(compactDefaultTextStacks, 1f).value, 0.01f)
        assertEquals(353.6f, homeHeroHeight(compactLargeTextStacks, 1.3f).value, 0.01f)
        assertEquals(353.6f, homeHeroHeight(regularLargeTextStacks, 1.3f).value, 0.01f)
        assertEquals(288.96f, homeHeroHeight(regularTextBeforeThresholdStacks, 1.29f).value, 0.01f)
    }

    @Test
    fun serviceBandUsesTwoRowsOnNarrowScreensAndLargeText() {
        assertTrue(shouldUseTwoRowHomeServiceBand(359.dp, 1f))
        assertFalse(shouldUseTwoRowHomeServiceBand(360.dp, 1f))
        assertFalse(shouldUseTwoRowHomeServiceBand(390.dp, 1.29f))
        assertTrue(shouldUseTwoRowHomeServiceBand(390.dp, 1.3f))
        assertFalse(shouldUseTwoRowHomeServiceBand(390.dp, 1f))
    }
}
