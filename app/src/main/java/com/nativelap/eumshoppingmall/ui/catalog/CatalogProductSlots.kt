package com.nativelap.eumshoppingmall.ui.catalog

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

sealed interface CatalogProductBadge {
    data class BestRank(val rank: Int) : CatalogProductBadge

    data class Label(val text: String) : CatalogProductBadge
}

// 순위 배지와 라벨 배지는 이미지 좌상단 같은 자리를 쓰므로, 순위가 있으면 라벨은 표시하지 않는다(임시 정책).
fun resolveCatalogProductBadge(bestRank: Int?, badgeLabel: String?): CatalogProductBadge? {
    if (bestRank != null && bestRank >= 1) return CatalogProductBadge.BestRank(bestRank)
    val label = badgeLabel?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return CatalogProductBadge.Label(label)
}

enum class CatalogPriceKind {
    Price,
    Inquiry,
    Unavailable,
}

data class CatalogPriceSlot(
    val kind: CatalogPriceKind,
    val originalLabel: String?,
    val discountLabel: String?,
    val currentLabel: String?,
)

// 가격 슬롯이 같은 높이를 유지하도록, 품절·가격 문의 상품은 원가·할인 줄을 비운다.
fun resolveCatalogPriceSlot(
    currentLabel: String?,
    originalLabel: String?,
    discountLabel: String?,
    isInquiry: Boolean,
    isSoldOut: Boolean,
): CatalogPriceSlot {
    if (isSoldOut) {
        return CatalogPriceSlot(
            kind = CatalogPriceKind.Unavailable,
            originalLabel = null,
            discountLabel = null,
            currentLabel = null,
        )
    }
    if (isInquiry || currentLabel.isNullOrBlank()) {
        return CatalogPriceSlot(
            kind = CatalogPriceKind.Inquiry,
            originalLabel = null,
            discountLabel = null,
            currentLabel = null,
        )
    }
    return CatalogPriceSlot(
        kind = CatalogPriceKind.Price,
        originalLabel = originalLabel?.takeIf { it.isNotBlank() },
        discountLabel = discountLabel?.takeIf { it.isNotBlank() && !isZeroDiscountLabel(it) },
        currentLabel = currentLabel,
    )
}

// "0%"처럼 할인이 없는 값은 할인 칩을 그리지 않는다.
internal fun isZeroDiscountLabel(discountLabel: String): Boolean = discountLabel.trim().removeSuffix("%").trim().toIntOrNull() == 0

// 검색어가 상품명에 나오는 모든 위치를 강조한다. 대소문자를 무시하고 정규식이 아닌 문자열로 비교한다.
fun highlightCatalogQuery(
    text: String,
    query: String?,
    highlightStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold),
): AnnotatedString {
    val keyword = query?.trim().orEmpty()
    if (keyword.isEmpty()) return AnnotatedString(text)
    return buildAnnotatedString {
        append(text)
        var matchStart = text.indexOf(keyword, startIndex = 0, ignoreCase = true)
        while (matchStart >= 0) {
            val matchEnd = matchStart + keyword.length
            addStyle(highlightStyle, matchStart, matchEnd)
            // 겹치지 않는 다음 등장 위치부터 다시 찾는다.
            matchStart = text.indexOf(keyword, startIndex = matchEnd, ignoreCase = true)
        }
    }
}
