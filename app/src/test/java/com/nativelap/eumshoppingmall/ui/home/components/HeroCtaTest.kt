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
    fun productBrowsingIsPrimaryOnEverySlideAndSecondaryActionIsContextual() {
        assertEquals(HomeHeroActionId.BrowseProducts, homeHeroPrimaryActionId())
        assertEquals(R.string.home_hero_browse_products, homeHeroActionLabelResId(HomeHeroActionId.BrowseProducts))
        assertEquals(R.string.home_hero_quote_action, homeHeroActionLabelResId(HomeHeroActionId.RequestQuote))
        assertEquals(R.string.home_hero_company_action, homeHeroActionLabelResId(HomeHeroActionId.ViewCompany))
        assertEquals(R.string.home_hero_quote_action, heroSecondaryActionLabelResId(HomeHeroActionId.RequestQuote))
        assertEquals(R.string.home_hero_company_action, heroSecondaryActionLabelResId(HomeHeroActionId.ViewCompany))
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
    fun quoteRemainsSecondaryActionAndCompanyLinkAppearsOnlyOnCompanySlide() {
        assertTrue(showsHeroSecondaryQuoteCta(HomeHeroActionId.RequestQuote))
        assertTrue(showsHeroSecondaryQuoteCta(HomeHeroActionId.BrowseProducts))
        assertFalse(showsHeroSecondaryQuoteCta(HomeHeroActionId.ViewCompany))
    }
}
