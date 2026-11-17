package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.ui.text.font.FontWeight
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogHighlightTest {
    private fun highlightedRanges(text: String, query: String?): List<Pair<Int, Int>> = highlightCatalogQuery(text, query).spanStyles.map { range -> range.start to range.end }

    @Test
    fun nullQueryLeavesNameWithoutHighlight() {
        assertEquals(emptyList<Pair<Int, Int>>(), highlightedRanges("세이프가드 ABS 안전모", null))
    }

    @Test
    fun blankOrWhitespaceQueryIsIgnored() {
        assertEquals(emptyList<Pair<Int, Int>>(), highlightedRanges("안전모", ""))
        assertEquals(emptyList<Pair<Int, Int>>(), highlightedRanges("안전모", "   "))
    }

    @Test
    fun queryMatchesIgnoringCase() {
        assertEquals(listOf(0 to 6), highlightedRanges("Safety Helmet", "safety"))
    }

    @Test
    fun everyOccurrenceIsHighlightedWithoutOverlap() {
        assertEquals(listOf(0 to 2, 2 to 4), highlightedRanges("aaaa", "aa"))
    }

    @Test
    fun queryIsTrimmedBeforeMatching() {
        assertEquals(listOf(0 to 2), highlightedRanges("안전모", "  안전 "))
    }

    @Test
    fun regexMetacharactersMatchLiterally() {
        assertEquals(listOf(0 to 4), highlightedRanges("a.*b", "a.*b"))
        assertEquals(emptyList<Pair<Int, Int>>(), highlightedRanges("axxb", "a.*b"))
        assertEquals(listOf(7 to 11), highlightedRanges("분말 소화기 (3.3kg)", "(3.3"))
    }

    @Test
    fun textWithoutMatchHasNoHighlight() {
        assertEquals(emptyList<Pair<Int, Int>>(), highlightedRanges("안전벨트", "안전모"))
    }

    @Test
    fun highlightKeepsOriginalTextAndUsesBoldByDefault() {
        val highlighted = highlightCatalogQuery("분말 소화기", "소화")

        assertEquals("분말 소화기", highlighted.text)
        assertEquals(FontWeight.Bold, highlighted.spanStyles.single().item.fontWeight)
    }
}
