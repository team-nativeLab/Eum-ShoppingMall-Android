package com.nativelap.eumshoppingmall.ui.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CatalogProductSlotsTest {
    // 순위 배지가 있으면 라벨 배지는 같은 자리에서 가린다.
    @Test
    fun bestRankWinsOverBadgeLabel() {
        assertEquals(CatalogProductBadge.BestRank(1), resolveCatalogProductBadge(bestRank = 1, badgeLabel = "가격 미표시"))
    }

    @Test
    fun badgeLabelIsUsedWhenThereIsNoRank() {
        assertEquals(CatalogProductBadge.Label("가격 미표시"), resolveCatalogProductBadge(bestRank = null, badgeLabel = "가격 미표시"))
    }

    @Test
    fun noBadgeWhenRankAndLabelAreMissingOrBlank() {
        assertNull(resolveCatalogProductBadge(bestRank = null, badgeLabel = null))
        assertNull(resolveCatalogProductBadge(bestRank = null, badgeLabel = "  "))
    }

    @Test
    fun nonPositiveRankIsIgnored() {
        assertEquals(CatalogProductBadge.Label("가격 미표시"), resolveCatalogProductBadge(bestRank = 0, badgeLabel = "가격 미표시"))
        assertNull(resolveCatalogProductBadge(bestRank = -1, badgeLabel = null))
    }

    // 품절은 가격 슬롯의 입력이 아니다. 판매가가 있으면 품절이어도 가격 줄을 그대로 쓰고, 품절 표시는 "품절" 배지가 맡는다.
    @Test
    fun soldOutProductKeepsPriceLinesBecauseSoldOutIsNotAPriceInput() {
        assertEquals(
            CatalogPriceSlot(CatalogPriceKind.Price, originalLabel = "₩22,000", discountLabel = "18%", currentLabel = "₩18,000"),
            resolveCatalogPriceSlot(
                currentLabel = "₩18,000",
                originalLabel = "₩22,000",
                discountLabel = "18%",
                isPriceUndisclosed = false,
                isPurchaseUnavailable = false,
            ),
        )
    }

    // 가격 미표시 상품(금액 미표시)은 가격 줄을 비우고 "금액 미표시" 종류를 쓴다.
    @Test
    fun undisclosedProductShowsUndisclosedAndHidesPriceLines() {
        assertEquals(
            CatalogPriceSlot(CatalogPriceKind.Undisclosed, originalLabel = null, discountLabel = null, currentLabel = null),
            resolveCatalogPriceSlot(
                currentLabel = null,
                originalLabel = null,
                discountLabel = null,
                isPriceUndisclosed = true,
                isPurchaseUnavailable = false,
            ),
        )
    }

    // 가격 없이 장바구니가 막힌 상품은 "구매 불가" 종류를 쓰고 가격 줄을 비운다.
    @Test
    fun purchaseUnavailableProductShowsUnavailableAndHidesPriceLines() {
        assertEquals(
            CatalogPriceSlot(CatalogPriceKind.Unavailable, originalLabel = null, discountLabel = null, currentLabel = null),
            resolveCatalogPriceSlot(
                currentLabel = null,
                originalLabel = null,
                discountLabel = null,
                isPriceUndisclosed = false,
                isPurchaseUnavailable = true,
            ),
        )
    }

    // 가격 미표시 상품이면서 구매 불가 플래그가 함께 와도 문의 연결(금액 미표시)이 우선이다.
    @Test
    fun undisclosedProductWinsOverPurchaseUnavailable() {
        assertEquals(
            CatalogPriceKind.Undisclosed,
            resolveCatalogPriceSlot(
                currentLabel = null,
                originalLabel = null,
                discountLabel = null,
                isPriceUndisclosed = true,
                isPurchaseUnavailable = true,
            ).kind,
        )
    }

    @Test
    fun missingCurrentLabelFallsBackToUndisclosed() {
        assertEquals(
            CatalogPriceKind.Undisclosed,
            resolveCatalogPriceSlot(
                currentLabel = " ",
                originalLabel = null,
                discountLabel = null,
                isPriceUndisclosed = false,
                isPurchaseUnavailable = false,
            ).kind,
        )
    }

    @Test
    fun discountedPriceKeepsOriginalAndDiscountLines() {
        assertEquals(
            CatalogPriceSlot(CatalogPriceKind.Price, originalLabel = "₩22,000", discountLabel = "18%", currentLabel = "₩18,000"),
            resolveCatalogPriceSlot(
                currentLabel = "₩18,000",
                originalLabel = "₩22,000",
                discountLabel = "18%",
                isPriceUndisclosed = false,
                isPurchaseUnavailable = false,
            ),
        )
    }

    @Test
    fun plainPriceHasNoOriginalOrDiscountLine() {
        assertEquals(
            CatalogPriceSlot(CatalogPriceKind.Price, originalLabel = null, discountLabel = null, currentLabel = "₩54,000"),
            resolveCatalogPriceSlot(
                currentLabel = "₩54,000",
                originalLabel = null,
                discountLabel = null,
                isPriceUndisclosed = false,
                isPurchaseUnavailable = false,
            ),
        )
    }

    @Test
    fun blankDiscountIsTreatedAsMissing() {
        val slot = resolveCatalogPriceSlot(
            currentLabel = "₩54,000",
            originalLabel = "₩60,000",
            discountLabel = " ",
            isPriceUndisclosed = false,
            isPurchaseUnavailable = false,
        )

        assertEquals("₩60,000", slot.originalLabel)
        assertNull(slot.discountLabel)
    }

    @Test
    fun zeroPercentDiscountDoesNotShowChip() {
        val slot = resolveCatalogPriceSlot(
            currentLabel = "₩54,000",
            originalLabel = "₩54,000",
            discountLabel = "0%",
            isPriceUndisclosed = false,
            isPurchaseUnavailable = false,
        )

        assertNull(slot.discountLabel)
    }

    @Test
    fun nonZeroDiscountIsKept() {
        val slot = resolveCatalogPriceSlot(
            currentLabel = "₩18,000",
            originalLabel = "₩22,000",
            discountLabel = "18%",
            isPriceUndisclosed = false,
            isPurchaseUnavailable = false,
        )

        assertEquals("18%", slot.discountLabel)
    }
}
