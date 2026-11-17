package com.nativelap.eumshoppingmall.ui.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeQueryTest {
    @Test
    fun homeOwnInputIsUsedWhenNoSharedQueryIsHoisted() {
        assertEquals("안전", effectiveHomeQuery(hoisted = null, internal = "안전"))
    }

    @Test
    fun sharedQueryWinsSoClearingInSearchResultsEmptiesTheHomeField() {
        // 검색 결과에서 지우면 공유 검색어가 ""가 되고, 홈 자체 입력("ABS")보다 우선해 빈 값으로 보인다.
        assertEquals("", effectiveHomeQuery(hoisted = "", internal = "ABS"))
        assertEquals("ABS", effectiveHomeQuery(hoisted = "ABS", internal = ""))
    }

    @Test
    fun reSearchFromResultsMovesSharedHomeQueryToThatTerm() {
        assertEquals("ABS", homeQueryForSearchQuery("  ABS "))
        assertEquals("", homeQueryForSearchQuery("   "))
    }
}
