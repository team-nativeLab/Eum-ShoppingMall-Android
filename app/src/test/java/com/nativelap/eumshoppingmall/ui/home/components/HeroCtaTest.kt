package com.nativelap.eumshoppingmall.ui.home.components

import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.home.HomeScreenEvent
import com.nativelap.eumshoppingmall.ui.home.heroQuoteCtaEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HeroCtaTest {
    @Test
    fun heroQuoteCtaUsesExistingQuoteEntryEvent() {
        assertEquals(HomeScreenEvent.QuoteClicked, heroQuoteCtaEvent())
    }

    @Test
    fun secondaryQuoteCtaHiddenOnSlideWhosePrimaryActionIsAlreadyQuote() {
        assertFalse(showsHeroSecondaryQuoteCta(R.string.home_hero_quote_action))
        assertTrue(showsHeroSecondaryQuoteCta(R.string.home_hero_browse_products))
    }

    @Test
    fun heroPrimaryButtonsMapToProductsQuoteAndCompanyIntroInSlideOrder() {
        assertEquals(HomeHeroTarget.Products, homeHeroTargetForSlide(0))
        assertEquals(HomeHeroTarget.Quote, homeHeroTargetForSlide(1))
        assertEquals(HomeHeroTarget.CompanyIntro, homeHeroTargetForSlide(2))
    }

    @Test
    fun slideWithoutTargetGivesNoTargetSoItsButtonIsHidden() {
        assertNull(homeHeroTargetForSlide(3))
        assertNull(homeHeroTargetForAction(R.string.home_hero_safe_eyebrow))
    }
}
