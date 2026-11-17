package com.nativelap.eumshoppingmall.ui.home.components

import org.junit.Assert.assertEquals
import org.junit.Test

class HeroAutoAdvanceTest {
    private val normal = 5_000L
    private val tapAt = 100_000L

    @Test
    fun noUserInteractionKeepsNormalCadence() {
        assertEquals(normal, heroAutoAdvanceDelayMillis(null, nowMillis = tapAt, normalDelayMillis = normal))
    }

    @Test
    fun rightAfterInteractionWaitsFullPauseThenNormalCadence() {
        assertEquals(HERO_INTERACTION_PAUSE_MILLIS + normal, heroAutoAdvanceDelayMillis(tapAt, tapAt, normal))
    }

    @Test
    fun midPauseWaitsOnlyTheRemainingPause() {
        assertEquals(1_000L + normal, heroAutoAdvanceDelayMillis(tapAt, tapAt + 14_000L, normal))
    }

    @Test
    fun afterPauseEndsNormalCadenceResumes() {
        assertEquals(normal, heroAutoAdvanceDelayMillis(tapAt, tapAt + HERO_INTERACTION_PAUSE_MILLIS, normal))
    }
}
