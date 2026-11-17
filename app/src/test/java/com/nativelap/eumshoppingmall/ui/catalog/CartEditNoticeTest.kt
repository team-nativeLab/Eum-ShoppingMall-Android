package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.runtime.saveable.SaverScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CartEditNoticeTest {
    @Test
    fun freshOutcomeIsShownByTheCartOnce() {
        val outcome = CartEditOutcome(serial = 1L, notice = CartEditNotice.InvalidEdit)

        assertEquals(CartEditNotice.InvalidEdit, cartEditNoticeToShow(outcome))
        // 보여 준 뒤 표시를 남기면 같은 결과는 다시 뜨지 않는다.
        assertNull(cartEditNoticeToShow(outcome.markedShown()))
    }

    @Test
    fun rotationOrReentryAfterShowingDoesNotShowTheSameNoticeAgain() {
        val shownBeforeRotation = CartEditOutcome(serial = 2L, notice = CartEditNotice.SaveFailed, shown = true)

        assertNull(cartEditNoticeToShow(shownBeforeRotation))
    }

    @Test
    fun noOutcomeMeansNoNotice() {
        assertNull(cartEditNoticeToShow(null))
    }

    @Test
    fun eachFailureKindKeepsItsOwnNotice() {
        assertEquals(CartEditNotice.SourceMissing, cartEditNoticeToShow(CartEditOutcome(3L, CartEditNotice.SourceMissing)))
        assertEquals(CartEditNotice.SaveFailed, cartEditNoticeToShow(CartEditOutcome(4L, CartEditNotice.SaveFailed)))
    }

    private val saverScope = object : SaverScope {
        override fun canBeSaved(value: Any): Boolean = true
    }

    /** 화면 재구성(회전·프로세스 복원)을 흉내 낸다: 저장 후 복원한 값을 돌려준다. */
    private fun roundTrip(outcome: CartEditOutcome?): CartEditOutcome? {
        val saved = with(CartEditOutcome.Saver) { saverScope.save(outcome) }
        return saved?.let { CartEditOutcome.Saver.restore(it) }
    }

    @Test
    fun saverRoundTripKeepsNullAsNull() {
        assertNull(roundTrip(null))
    }

    @Test
    fun saverRoundTripKeepsEveryNoticeAndTheShownFlag() {
        CartEditNotice.values().forEach { notice ->
            val fresh = CartEditOutcome(serial = 10L, notice = notice, shown = false)
            val shown = CartEditOutcome(serial = 11L, notice = notice, shown = true)

            assertEquals(fresh, roundTrip(fresh))
            assertEquals(shown, roundTrip(shown))
        }
    }

    @Test
    fun restoredOutcomeGivesTheSameNoticeDecisionAsTheOriginal() {
        val fresh = CartEditOutcome(serial = 20L, notice = CartEditNotice.SaveFailed, shown = false)
        val shown = CartEditOutcome(serial = 21L, notice = CartEditNotice.SaveFailed, shown = true)

        assertEquals(cartEditNoticeToShow(fresh), cartEditNoticeToShow(roundTrip(fresh)))
        assertEquals(cartEditNoticeToShow(shown), cartEditNoticeToShow(roundTrip(shown)))
        assertNull(cartEditNoticeToShow(roundTrip(shown)))
    }

    @Test
    fun clearingOnlyRemovesTheOutcomeThatWasShown() {
        val shown = CartEditOutcome(serial = 30L, notice = CartEditNotice.InvalidEdit, shown = true)
        val newer = CartEditOutcome(serial = 31L, notice = CartEditNotice.SourceMissing)

        assertNull(shown.clearedIfShown(30L))
        assertEquals(newer, newer.clearedIfShown(30L))
        assertNull(null.clearedIfShown(30L))
    }
}
