package com.nativelap.eumshoppingmall.data.local

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val LINE_SEPARATOR = "\n"
private const val FIELD_SEPARATOR = "|"

// 현재 형식은 9개 필드다. 8개 필드는 단가 필드를 넣기 전 형식이며, 단가는 미상으로 읽는다.
private const val FIELD_COUNT_WITHOUT_UNIT_PRICE = 8
private const val FIELD_COUNT = 9

/** 장바구니 줄 목록을 한 문자열로 바꾼다. 각 필드는 URL 인코딩해서 구분자와 줄바꿈이 값에 섞이지 않게 한다. */
internal fun encodeCartLines(lines: List<CartLine>): String = lines.joinToString(LINE_SEPARATOR) { line -> encodeCartLine(line) }

private fun encodeCartLine(line: CartLine): String = listOf(
    line.productId,
    line.productName,
    line.optionId,
    line.optionLabel,
    line.quantity.toString(),
    line.optionExtraWon?.toString().orEmpty(),
    line.productPriceLabel,
    if (line.isQuoteRequired) "1" else "0",
    line.productUnitPriceWon?.toString().orEmpty(),
).joinToString(FIELD_SEPARATOR) { field -> URLEncoder.encode(field, StandardCharsets.UTF_8.name()) }

/**
 * 저장 문자열을 장바구니 줄 목록으로 되돌린다.
 * 깨진 줄(필드 수 불일치, 숫자 오류, 빈 식별자, 잘못된 인코딩)은 건너뛴다. 쓰기 때는 [mergeCartLinesRaw]가 원문을 보존한다.
 */
internal fun decodeCartLines(raw: String): List<CartLine> = raw
    .split(LINE_SEPARATOR)
    .filter { text -> text.isNotBlank() }
    .mapNotNull { text -> decodeCartLine(text) }

private fun decodeCartLine(text: String): CartLine? {
    val fields = text.split(FIELD_SEPARATOR)
    if (fields.size != FIELD_COUNT && fields.size != FIELD_COUNT_WITHOUT_UNIT_PRICE) return null
    val decoded = try {
        fields.map { field -> URLDecoder.decode(field, StandardCharsets.UTF_8.name()) }
    } catch (exception: IllegalArgumentException) {
        return null
    }
    val productId = decoded[0]
    val optionId = decoded[2]
    val quantity = decoded[4].toIntOrNull()?.takeIf { it >= 1 } ?: return null
    val optionExtraWon = if (decoded[5].isEmpty()) null else decoded[5].toLongOrNull() ?: return null
    val isQuoteRequired = when (decoded[7]) {
        "1" -> true
        "0" -> false
        else -> return null
    }
    val productUnitPriceWon = if (decoded.size < FIELD_COUNT || decoded[8].isEmpty()) {
        null
    } else {
        decoded[8].toLongOrNull() ?: return null
    }
    if (productId.isEmpty() || optionId.isEmpty()) return null
    return CartLine(
        productId = productId,
        productName = decoded[1],
        optionId = optionId,
        optionLabel = decoded[3],
        quantity = quantity,
        optionExtraWon = optionExtraWon,
        productPriceLabel = decoded[6],
        isQuoteRequired = isQuoteRequired,
        productUnitPriceWon = productUnitPriceWon,
    )
}

/**
 * 같은 상품·같은 옵션 줄은 수량을 더하고, 상품명·가격 문구·추가금·옵션 라벨·단가는 새 값으로 덮어쓴다.
 * 기존 줄의 위치는 유지하고 새 줄은 뒤에 붙인다.
 */
internal fun List<CartLine>.mergedWith(addedLines: List<CartLine>): List<CartLine> {
    val merged = toMutableList()
    addedLines.forEach { added ->
        val index = merged.indexOfFirst { line -> line.lineId() == added.lineId() }
        if (index >= 0) {
            val existing = merged[index]
            merged[index] = added.copy(quantity = saturatedSum(existing.quantity, added.quantity))
        } else {
            merged.add(added)
        }
    }
    return merged
}

/**
 * 저장 문자열에 새 줄을 합쳐 다시 쓴 문자열을 만든다.
 * 디코딩되지 않는 줄은 원문 그대로 남기고 위치도 유지한다. 깨진 줄 때문에 다른 저장 항목이 사라지지 않게 하기 위함이다.
 */
internal fun mergeCartLinesRaw(raw: String, addedLines: List<CartLine>): String {
    val rawLines = raw.split(LINE_SEPARATOR).filter { text -> text.isNotBlank() }
    val decodedLines = rawLines.mapNotNull { text -> decodeCartLine(text) }
    val merged = decodedLines.mergedWith(addedLines)
    return rewriteCartLines(rawLines, merged, decodedCount = decodedLines.size)
}

/**
 * 저장 문자열의 각 줄을 [transform]으로 고쳐 다시 쓴다. null을 돌려주면 그 줄은 지운다.
 * 디코딩되지 않는 줄은 원문 그대로 둔다.
 */
internal fun editCartLinesRaw(raw: String, transform: (CartLine) -> CartLine?): String {
    val rawLines = raw.split(LINE_SEPARATOR).filter { text -> text.isNotBlank() }
    val output = rawLines.mapNotNull { text ->
        val line = decodeCartLine(text)
        if (line == null) {
            text
        } else {
            transform(line)?.let { edited -> encodeCartLine(edited) }
        }
    }
    return output.joinToString(LINE_SEPARATOR)
}

// 디코딩된 줄은 merged의 앞쪽 decodedCount개와 순서대로 대응한다. 나머지는 새 줄이다.
private fun rewriteCartLines(rawLines: List<String>, merged: List<CartLine>, decodedCount: Int): String {
    var nextMergedIndex = 0
    val output = rawLines.map { text ->
        if (decodeCartLine(text) == null) {
            text
        } else {
            encodeCartLine(merged[nextMergedIndex++])
        }
    }.toMutableList()
    merged.drop(decodedCount).forEach { line -> output.add(encodeCartLine(line)) }
    return output.joinToString(LINE_SEPARATOR)
}

/** 배지에 쓰는 개수. 줄 수가 아니라 수량 합이며, Int 범위를 넘으면 최댓값으로 맞춘다. */
internal fun List<CartLine>.totalQuantity(): Int = fold(0L) { total, line -> total + line.quantity }
    .coerceAtMost(Int.MAX_VALUE.toLong())
    .toInt()

private fun saturatedSum(first: Int, second: Int): Int = (first.toLong() + second.toLong()).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
