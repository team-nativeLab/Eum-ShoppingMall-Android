package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.common.MainNavigationTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavTransitionDecisionTest {
    @Test
    fun pushAndPopUseTheirOwnKinds() {
        assertEquals(NavTransitionKind.Push, navTransitionKind(reducedMotion = false, pop = false, tabSwitch = false))
        assertEquals(NavTransitionKind.Pop, navTransitionKind(reducedMotion = false, pop = true, tabSwitch = false))
    }

    @Test
    fun tabSwitchWinsOverPopWhenNotReduced() {
        assertEquals(NavTransitionKind.TabSwitch, navTransitionKind(reducedMotion = false, pop = true, tabSwitch = true))
    }

    @Test
    fun reducedMotionUsesFadeForStackChangesAndInstantForTabSwitch() {
        assertEquals(NavTransitionKind.ReducedFade, navTransitionKind(reducedMotion = true, pop = false, tabSwitch = false))
        assertEquals(NavTransitionKind.ReducedFade, navTransitionKind(reducedMotion = true, pop = true, tabSwitch = false))
        assertEquals(NavTransitionKind.ReducedInstant, navTransitionKind(reducedMotion = true, pop = false, tabSwitch = true))
    }

    @Test
    fun tabSwitchNeedsTwoKnownDifferentTabs() {
        assertTrue(isTabSwitch(MainNavigationTab.Home, MainNavigationTab.Cart))
        assertFalse(isTabSwitch(MainNavigationTab.Home, MainNavigationTab.Home))
        assertFalse(isTabSwitch(null, MainNavigationTab.Cart))
        assertFalse(isTabSwitch(MainNavigationTab.Home, null))
    }
}
