package com.nativelap.eumshoppingmall.ui.home.components

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeHeroActionId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HeroCtaTest {
    @Test
    fun heroSlidesOpenApprovedActionsInSlideOrder() {
        assertEquals(
            listOf(HomeHeroActionId.BrowseProducts, HomeHeroActionId.BrowseProducts, HomeHeroActionId.BrowseProducts),
            homeHeroSlideActionIds(),
        )
    }

    @Test
    fun productBrowsingIsPrimaryOnEverySlideAndCompanyLabelIsDefined() {
        assertEquals(HomeHeroActionId.BrowseProducts, homeHeroPrimaryActionId())
        assertEquals(R.string.home_hero_browse_products, homeHeroActionLabelResId(HomeHeroActionId.BrowseProducts))
        assertEquals(R.string.home_hero_company_action, homeHeroActionLabelResId(HomeHeroActionId.ViewCompany))
    }

    @Test
    fun resumedHeroAutoAdvancesUnlessDraggedBackgroundedOrReducedMotion() {
        assertTrue(heroAutoAdvanceEnabled(isResumed = true, isDragged = false, isReducedMotion = false))
        assertFalse(heroAutoAdvanceEnabled(isResumed = false, isDragged = false, isReducedMotion = false))
        assertFalse(heroAutoAdvanceEnabled(isResumed = true, isDragged = true, isReducedMotion = false))
        assertFalse(heroAutoAdvanceEnabled(isResumed = true, isDragged = false, isReducedMotion = true))
    }

}
