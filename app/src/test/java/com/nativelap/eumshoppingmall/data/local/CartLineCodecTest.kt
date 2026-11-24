package com.nativelap.eumshoppingmall.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class CartLineCodecTest {
    @Test
    fun encodedLinesDecodeToTheSameValues() {
        val lines = listOf(line(productId = "a", optionId = "o1"), line(productId = "b", optionId = "o2", optionExtraWon = null, isPriceUndisclosed = true))

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
    fun mergeCapsQuantityAtLineLimit() {
        val merged = listOf(line(quantity = 998)).mergedWith(listOf(line(quantity = 5)))

        assertEquals(MAX_CART_LINE_QUANTITY, merged.single().quantity)
    }

    @Test
    fun storedQuantityAboveLimitIsReadAtTheLimit() {
        val stored = "p|%EC%83%81%ED%92%88|o|%EC%98%B5%EC%85%98|5000||%EA%B0%80%EA%B2%A9|0|1000"

        assertEquals(MAX_CART_LINE_QUANTITY, decodeCartLines(stored).single().quantity)
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
    fun storedLegacyFlaggedLinesReadAsPriceUndisclosedLinesWithoutUnitPrice() {
        // 플래그 필드(8번째)가 1인 기존 저장 줄은 가격 미표시 문의 줄로 읽는다. 단가가 저장돼 있어도 쓰지 않는다.
        val eightFields = "p|%EC%83%81%ED%92%88|o|%EC%98%B5%EC%85%98|2||%EA%B0%80%EA%B2%A9|1"
        val nineFields = "p|%EC%83%81%ED%92%88|o|%EC%98%B5%EC%85%98|2||%EA%B0%80%EA%B2%A9|1|18000"

        val fromEight = decodeCartLines(eightFields).single()
        val fromNine = decodeCartLines(nineFields).single()

        assertEquals(true, fromEight.isPriceUndisclosed)
        assertEquals(true, fromNine.isPriceUndisclosed)
        assertEquals(null, fromNine.productUnitPriceWon)
        assertEquals(2, fromNine.quantity)
    }

    @Test
    fun priceUndisclosedLineKeepsNineFieldFormatWhenRewritten() {
        val rewritten = encodeCartLines(listOf(line(isPriceUndisclosed = true)))

        assertEquals(9, rewritten.split("|").size)
        assertEquals("1", rewritten.split("|")[7])
        assertEquals(true, decodeCartLines(rewritten).single().isPriceUndisclosed)
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

    @Test
    fun replacementKeepsSourceQuantityAndMergesWithExistingTarget() {
        val source = line(productId = "p", optionId = "old", quantity = 4)
        val target = line(productId = "p", optionId = "new", quantity = 1).copy(productName = "현재 이름")
        val existingTarget = line(productId = "p", optionId = "new", quantity = 3)

        val (raw, replaced) = replaceCartLineRaw(encodeCartLines(listOf(source, existingTarget)), source.lineId(), target)

        assertEquals(true, replaced)
        assertEquals(listOf("new"), decodeCartLines(raw).map { it.optionId })
        assertEquals(7, decodeCartLines(raw).single().quantity)
        assertEquals("현재 이름", decodeCartLines(raw).single().productName)
    }

    @Test
    fun replacementWithSameOptionPreservesQuantityOnce() {
        val source = line(productId = "p", optionId = "same", quantity = 4)
        val replacement = source.copy(productName = "현재 이름", quantity = 1)

        val (raw, replaced) = replaceCartLineRaw(encodeCartLines(listOf(source)), source.lineId(), replacement)

        assertEquals(true, replaced)
        assertEquals(4, decodeCartLines(raw).single().quantity)
    }

    @Test
    fun replacementWithSameOptionKeepsItsListPosition() {
        val first = line(productId = "first", optionId = "o1")
        val source = line(productId = "p", optionId = "same", quantity = 4)
        val last = line(productId = "last", optionId = "o2")
        val replacement = source.copy(productName = "현재 이름", quantity = 1)

        val (raw, replaced) = replaceCartLineRaw(encodeCartLines(listOf(first, source, last)), source.lineId(), replacement)

        val decoded = decodeCartLines(raw)
        assertEquals(true, replaced)
        assertEquals(listOf(first.lineId(), source.lineId(), last.lineId()), decoded.map { it.lineId() })
        assertEquals(4, decoded[1].quantity)
    }

    @Test
    fun optionChangeKeepsTheSourceLineInItsListPosition() {
        val first = line(productId = "first", optionId = "o1")
        val source = line(productId = "p", optionId = "old", quantity = 2)
        val last = line(productId = "last", optionId = "o2")
        val target = line(productId = "p", optionId = "new", quantity = 1)

        val (raw, replaced) = replaceCartLineRaw(encodeCartLines(listOf(first, source, last)), source.lineId(), target)

        val decoded = decodeCartLines(raw)
        assertEquals(true, replaced)
        assertEquals(listOf(first.lineId(), target.lineId(), last.lineId()), decoded.map { it.lineId() })
        assertEquals(2, decoded[1].quantity)
    }

    @Test
    fun mergingIntoAnExistingTargetPlacesTheMergedLineAtSourceAndDropsTheDuplicate() {
        val source = line(productId = "p", optionId = "old", quantity = 4)
        val first = line(productId = "first", optionId = "o1")
        val existingTarget = line(productId = "p", optionId = "new", quantity = 2)
        val last = line(productId = "last", optionId = "o2")
        val target = line(productId = "p", optionId = "new", quantity = 1)

        val (raw, replaced) = replaceCartLineRaw(encodeCartLines(listOf(source, first, existingTarget, last)), source.lineId(), target)

        val decoded = decodeCartLines(raw)
        assertEquals(true, replaced)
        assertEquals(listOf(target.lineId(), first.lineId(), last.lineId()), decoded.map { it.lineId() })
        assertEquals(6, decoded.first().quantity)
    }

    @Test
    fun brokenRowsKeepTheirOriginalTextAcrossAReplacement() {
        val broken = "malformed|row"
        val source = line(productId = "p", optionId = "old", quantity = 2)
        val last = line(productId = "last", optionId = "o2")
        val target = line(productId = "p", optionId = "new", quantity = 1)

        val (raw, _) = replaceCartLineRaw(listOf(broken, encodeCartLines(listOf(source, last))).joinToString("\n"), source.lineId(), target)

        val lines = raw.lineSequence().toList()
        assertEquals(broken, lines[0])
        assertEquals(listOf(target.lineId(), last.lineId()), decodeCartLines(lines.drop(1).joinToString("\n")).map { it.lineId() })
    }

    @Test
    fun otherRowsKeepTheirOrderAroundTheReplacedRow() {
        val a = line(productId = "a", optionId = "o1")
        val b = line(productId = "b", optionId = "o2")
        val c = line(productId = "c", optionId = "o3")
        val source = line(productId = "p", optionId = "old")
        val target = line(productId = "p", optionId = "new")

        val (raw, _) = replaceCartLineRaw(encodeCartLines(listOf(a, b, source, c)), source.lineId(), target)

        assertEquals(listOf("a/o1", "b/o2", "p/new", "c/o3"), decodeCartLines(raw).map { it.lineId() })
    }

    @Test
    fun replacementMissingSourceDoesNotAddTarget() {
        val originalRaw = encodeCartLines(listOf(line(productId = "p", optionId = "old")))

        val (raw, replaced) = replaceCartLineRaw(originalRaw, "missing", line(productId = "p", optionId = "new"))

        assertEquals(false, replaced)
        assertEquals(originalRaw, raw)
    }

    @Test
    fun replacementSaturatesQuantityAndPreservesMalformedRows() {
        val source = line(productId = "p", optionId = "old", quantity = 20)
        val existing = line(productId = "p", optionId = "new", quantity = Int.MAX_VALUE)
        val broken = "malformed|row"

        val (raw, _) = replaceCartLineRaw(listOf(broken, encodeCartLines(listOf(source, existing))).joinToString("\n"), source.lineId(), existing)

        assertEquals(broken, raw.lineSequence().first())
        assertEquals(MAX_CART_LINE_QUANTITY, decodeCartLines(raw).single().quantity)
    }

    private fun line(
        productId: String = "p",
        optionId: String = "o",
        quantity: Int = 1,
        optionExtraWon: Long? = 0L,
        isPriceUndisclosed: Boolean = false,
    ): CartLine = CartLine(
        productId = productId,
        productName = "상품 $productId",
        optionId = optionId,
        optionLabel = "옵션 $optionId",
        quantity = quantity,
        optionExtraWon = optionExtraWon,
        productPriceLabel = "₩18,000",
        isPriceUndisclosed = isPriceUndisclosed,
    )
}
