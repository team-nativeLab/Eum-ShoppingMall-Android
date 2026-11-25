package com.nativelap.eumshoppingmall.ui.home.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HeroVirtualPageTest {
    private val slideCount = 3
    private val virtualPageCount = slideCount * HERO_VIRTUAL_CYCLES
    private val basePage = heroCycleBasePage(slideCount)

    @Test
    fun slideIndexWrapsAroundSlideCountInBothDirections() {
        assertEquals(0, heroSlideIndex(0, slideCount))
        assertEquals(2, heroSlideIndex(2, slideCount))
        assertEquals(0, heroSlideIndex(3, slideCount))
        assertEquals(1, heroSlideIndex(4, slideCount))
        assertEquals(2, heroSlideIndex(-1, slideCount))
    }

    @Test
    fun cycleBasePageStartsOnSlideZeroInsideThePageRange() {
        assertEquals(0, heroSlideIndex(basePage, slideCount))
        assertTrue(basePage in 0 until virtualPageCount)
    }

    @Test
    fun dotTargetMovesToTheNearestPageWithThatSlide() {
        assertEquals(basePage + 1, heroTargetVirtualPage(basePage, 1, slideCount, virtualPageCount))
        // 현재 0번일 때 2번 점은 한 칸 앞(-1)이 더 가깝다.
        assertEquals(basePage - 1, heroTargetVirtualPage(basePage, 2, slideCount, virtualPageCount))
        // 현재 2번(페이지 +5)일 때 0번 점은 한 칸 뒤(+1)가 더 가깝다.
        assertEquals(basePage + 6, heroTargetVirtualPage(basePage + 5, 0, slideCount, virtualPageCount))
        // 현재 2번일 때 1번 점은 한 칸 앞(-1)이 더 가깝다.
        assertEquals(basePage + 4, heroTargetVirtualPage(basePage + 5, 1, slideCount, virtualPageCount))
    }

    @Test
    fun dotTargetOfCurrentSlideStaysOnCurrentPage() {
        assertEquals(basePage + 4, heroTargetVirtualPage(basePage + 4, 1, slideCount, virtualPageCount))
    }

    @Test
    fun dotTargetFallsBackToCycleBaseWhenNearTheEndOfThePageRange() {
        // 첫 페이지(0)에서 2번 점은 앞쪽 한 칸이 범위 밖이라 사이클 기준점에서 고른다.
        assertEquals(basePage + 2, heroTargetVirtualPage(0, 2, slideCount, virtualPageCount))
        // 마지막 페이지(2번 슬라이드)에서 0번 점은 뒤쪽 한 칸이 범위 밖이라 사이클 기준점에서 고른다.
        assertEquals(basePage, heroTargetVirtualPage(virtualPageCount - 1, 0, slideCount, virtualPageCount))
    }

    @Test
    fun autoAdvanceMovesOnePageForward() {
        assertEquals(basePage + 2, heroNextVirtualPage(basePage + 1, slideCount, virtualPageCount))
    }

    @Test
    fun autoAdvanceRebasesToCycleBaseAtTheLastPageWithTheSameSlide() {
        assertEquals(basePage, heroNextVirtualPage(virtualPageCount - 1, slideCount, virtualPageCount))
        assertEquals(basePage + 1, heroNextVirtualPage(virtualPageCount, slideCount, virtualPageCount))
    }

    @Test
    fun settledPageInsideTheMiddleIsLeftAlone() {
        assertEquals(basePage, heroEdgeRebasedPage(basePage, slideCount, virtualPageCount))
        assertEquals(basePage + 2, heroEdgeRebasedPage(basePage + 2, slideCount, virtualPageCount))
        // 첫 한 바퀴 바로 뒤(슬라이드 수 번째 페이지)와 마지막 한 바퀴 바로 앞은 아직 범위 안이다.
        assertEquals(slideCount, heroEdgeRebasedPage(slideCount, slideCount, virtualPageCount))
        assertEquals(virtualPageCount - slideCount - 1, heroEdgeRebasedPage(virtualPageCount - slideCount - 1, slideCount, virtualPageCount))
    }

    @Test
    fun settledPageAtTheStartEdgeMovesToTheSameSlideInTheMiddleCycle() {
        assertEquals(basePage, heroEdgeRebasedPage(0, slideCount, virtualPageCount))
        assertEquals(basePage + 2, heroEdgeRebasedPage(slideCount - 1, slideCount, virtualPageCount))
    }

    @Test
    fun settledPageAtTheEndEdgeMovesToTheSameSlideInTheMiddleCycle() {
        assertEquals(basePage, heroEdgeRebasedPage(virtualPageCount - slideCount, slideCount, virtualPageCount))
        assertEquals(basePage + 2, heroEdgeRebasedPage(virtualPageCount - 1, slideCount, virtualPageCount))
    }
}
