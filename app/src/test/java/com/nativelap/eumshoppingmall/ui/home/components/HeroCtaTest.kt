package com.nativelap.eumshoppingmall.ui.home.components

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeHeroActionId
import com.nativelap.eumshoppingmall.ui.home.HomeScreenEvent
import com.nativelap.eumshoppingmall.ui.home.heroQuoteCtaEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HeroCtaTest {
    @Test
    fun heroQuoteCtaUsesExistingQuoteEntryEvent() {
        assertEquals(HomeScreenEvent.QuoteClicked, heroQuoteCtaEvent())
    }

    @Test
    fun heroSlidesOpenApprovedActionsInSlideOrder() {
        assertEquals(
            listOf(HomeHeroActionId.BrowseProducts, HomeHeroActionId.RequestQuote, HomeHeroActionId.ViewCompany),
            homeHeroSlideActionIds(),
        )
    }

    @Test
    fun heroPrimaryLabelFollowsItsAction() {
        assertEquals(R.string.home_hero_browse_products, homeHeroActionLabelResId(HomeHeroActionId.BrowseProducts))
        assertEquals(R.string.home_hero_quote_action, homeHeroActionLabelResId(HomeHeroActionId.RequestQuote))
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
    fun secondaryQuoteCtaHiddenOnSlideWhosePrimaryActionIsAlreadyQuote() {
        assertFalse(showsHeroSecondaryQuoteCta(R.string.home_hero_quote_action))
        assertTrue(showsHeroSecondaryQuoteCta(R.string.home_hero_browse_products))
    }
}
