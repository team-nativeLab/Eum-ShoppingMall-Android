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
            listOf(HomeHeroActionId.BrowseProducts, HomeHeroActionId.ViewCompany),
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
    fun userPausedHeroDoesNotAutoAdvanceEvenWhenResumed() {
        assertFalse(heroAutoAdvanceEnabled(isResumed = true, isDragged = false, isUserPaused = true, isReducedMotion = false))
    }

    @Test
    fun resumedHeroAutoAdvancesUnlessDraggedBackgroundedOrReducedMotion() {
        assertTrue(heroAutoAdvanceEnabled(isResumed = true, isDragged = false, isUserPaused = false, isReducedMotion = false))
        assertFalse(heroAutoAdvanceEnabled(isResumed = false, isDragged = false, isUserPaused = false, isReducedMotion = false))
        assertFalse(heroAutoAdvanceEnabled(isResumed = true, isDragged = true, isUserPaused = false, isReducedMotion = false))
        assertFalse(heroAutoAdvanceEnabled(isResumed = true, isDragged = false, isUserPaused = false, isReducedMotion = true))
    }

    @Test
    fun companyLinkAppearsOnlyOnCompanySlide() {
        assertTrue(showsHeroCompanyLink(HomeHeroActionId.ViewCompany))
        assertFalse(showsHeroCompanyLink(HomeHeroActionId.BrowseProducts))
    }
}
