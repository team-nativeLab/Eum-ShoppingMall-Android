package com.nativelap.eumshoppingmall.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class CartLineCodecTest {
    @Test
    fun encodedLinesDecodeToTheSameValues() {
        val lines = listOf(line(productId = "a", optionId = "o1"), line(productId = "b", optionId = "o2", optionExtraWon = null, isQuoteRequired = true))

        assertEquals(lines, decodeCartLines(encodeCartLines(lines)))
    }

    @Test
    fun brokenLinesAreSkippedAndGoodLinesKept() {
        val good = encodeCartLines(listOf(line(productId = "ok")))
        val raw = listOf(
            good,
            "only|three|fields",
            "%ZZ|name|opt|label|1||price|0",
            "p|n|o|l|0||price|0",
            "p|n|o|l|x||price|0",
            "p|n|o|l|1|abc|price|0",
            "p|n|o|l|1||price|9",
            "||o|l|1||price|0",
        ).joinToString("\n")

        assertEquals(listOf("ok"), decodeCartLines(raw).map { it.productId })
    }

    @Test
    fun blankStringDecodesToEmptyCart() {
        assertEquals(emptyList<CartLine>(), decodeCartLines(""))
    }

    @Test
    fun mergeAddsQuantityForSameOptionAndAppendsNewLines() {
        val existing = listOf(line(productId = "a", optionId = "o1", quantity = 1))
        val added = listOf(line(productId = "a", optionId = "o1", quantity = 2), line(productId = "a", optionId = "o2", quantity = 1))

        val merged = existing.mergedWith(added)

        assertEquals(listOf(3, 1), merged.map { it.quantity })
        assertEquals(listOf("o1", "o2"), merged.map { it.optionId })
    }

    @Test
    fun badgeCountIsSumOfQuantitiesNotLineCount() {
        val lines = listOf(line(optionId = "o1", quantity = 2), line(optionId = "o2", quantity = 3))

        assertEquals(5, lines.totalQuantity())
        assertEquals(0, emptyList<CartLine>().totalQuantity())
    }

    @Test
    fun badgeCountSaturatesAtIntMax() {
        val lines = listOf(line(optionId = "o1", quantity = Int.MAX_VALUE), line(optionId = "o2", quantity = 10))

        assertEquals(Int.MAX_VALUE, lines.totalQuantity())
    }

    @Test
    fun mergeOverwritesLineDetailsWithTheNewValues() {
        val existing = listOf(line(productId = "a", optionId = "o1", quantity = 1).copy(productName = "예전 이름", productPriceLabel = "₩1,000", optionExtraWon = 500L))
        val added = listOf(line(productId = "a", optionId = "o1", quantity = 2).copy(productName = "새 이름", productPriceLabel = "₩2,000", optionExtraWon = 0L, optionLabel = "새 라벨"))

        val merged = existing.mergedWith(added).single()

        assertEquals(3, merged.quantity)
        assertEquals("새 이름", merged.productName)
        assertEquals("₩2,000", merged.productPriceLabel)
        assertEquals(0L, merged.optionExtraWon)
        assertEquals("새 라벨", merged.optionLabel)
    }

    @Test
    fun rawMergeKeepsBrokenLinesVerbatimAndInPlace() {
        val good = encodeCartLines(listOf(line(productId = "ok", optionId = "o1", quantity = 1)))
        val broken = "only|three|fields"
        val raw = listOf(broken, good).joinToString("\n")

        val written = mergeCartLinesRaw(raw, listOf(line(productId = "new", optionId = "o2", quantity = 4)))
        val lines = written.split("\n")

        assertEquals(broken, lines[0])
        assertEquals(listOf("ok", "new"), decodeCartLines(written).map { it.productId })
        assertEquals(3, lines.size)
    }

    @Test
    fun rawMergeAddsToExistingGoodLineAroundBrokenLines() {
        val good = encodeCartLines(listOf(line(productId = "ok", optionId = "o1", quantity = 1)))
        val raw = listOf("broken-line", good, "%ZZ|bad").joinToString("\n")

        val written = mergeCartLinesRaw(raw, listOf(line(productId = "ok", optionId = "o1", quantity = 2)))
        val lines = written.split("\n")

        assertEquals("broken-line", lines[0])
        assertEquals("%ZZ|bad", lines[2])
        assertEquals(3, decodeCartLines(written).single().quantity)
    }

    @Test
    fun unitPriceRoundTripsAndEightFieldLinesReadAsUnknownPrice() {
        val priced = line(productId = "p", optionId = "o", optionExtraWon = 500L).copy(productUnitPriceWon = 18_000L)
        val oldFormat = "p|%EC%83%81%ED%92%88|o|%EC%98%B5%EC%85%98|2|500|%E2%82%A918%2C000|0"

        assertEquals(18_000L, decodeCartLines(encodeCartLines(listOf(priced))).single().productUnitPriceWon)
        val old = decodeCartLines(oldFormat).single()
        assertEquals(null, old.productUnitPriceWon)
        assertEquals(2, old.quantity)
        assertEquals(500L, old.optionExtraWon)
    }

    @Test
    fun quantityEditKeepsBrokenLinesAndDropsRemovedOnes() {
        val good = listOf(line(productId = "a", optionId = "o1", quantity = 1), line(productId = "b", optionId = "o2", quantity = 1))
        val raw = (listOf("only|three") + encodeCartLines(good).split("\n")).joinToString("\n")

        val edited = editCartLinesRaw(raw) { line ->
            when (line.lineId()) {
                "a/o1" -> line.copy(quantity = 5)
                "b/o2" -> null
                else -> line
            }
        }
        val lines = edited.split("\n")

        assertEquals("only|three", lines[0])
        assertEquals(2, lines.size)
        assertEquals(listOf("a" to 5), decodeCartLines(edited).map { it.productId to it.quantity })
    }

    private fun line(
        productId: String = "p",
        optionId: String = "o",
        quantity: Int = 1,
        optionExtraWon: Long? = 0L,
        isQuoteRequired: Boolean = false,
    ): CartLine = CartLine(
        productId = productId,
        productName = "상품 $productId",
        optionId = optionId,
        optionLabel = "옵션 $optionId",
        quantity = quantity,
        optionExtraWon = optionExtraWon,
        productPriceLabel = "₩18,000",
        isQuoteRequired = isQuoteRequired,
    )
}
