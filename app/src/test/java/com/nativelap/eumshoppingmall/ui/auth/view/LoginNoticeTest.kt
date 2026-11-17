package com.nativelap.eumshoppingmall.ui.auth.view

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginNoticeTest {
    @Test
    fun noticeShowsOnceWhenPresentAndNotYetShown() {
        assertTrue(shouldShowLoginNotice("장바구니에 담으려면 로그인이 필요해요", alreadyShown = false))
    }

    @Test
    fun noticeIsNotShownAgainAfterRecomposeOrRotation() {
        assertFalse(shouldShowLoginNotice("장바구니에 담으려면 로그인이 필요해요", alreadyShown = true))
    }

    @Test
    fun noNoticeMeansNothingToShow() {
        assertFalse(shouldShowLoginNotice(null, alreadyShown = false))
    }
}
