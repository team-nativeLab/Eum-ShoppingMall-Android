package com.nativelap.eumshoppingmall.ui.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CatalogProductSlotsTest {
    // 순위 배지가 있으면 견적 등 라벨 배지는 같은 자리에서 가린다.
    @Test
    fun bestRankWinsOverBadgeLabel() {
        assertEquals(CatalogProductBadge.BestRank(1), resolveCatalogProductBadge(bestRank = 1, badgeLabel = "견적 상품"))
    }

    @Test
    fun badgeLabelIsUsedWhenThereIsNoRank() {
        assertEquals(CatalogProductBadge.Label("견적 상품"), resolveCatalogProductBadge(bestRank = null, badgeLabel = "견적 상품"))
    }

    @Test
    fun noBadgeWhenRankAndLabelAreMissingOrBlank() {
        assertNull(resolveCatalogProductBadge(bestRank = null, badgeLabel = null))
        assertNull(resolveCatalogProductBadge(bestRank = null, badgeLabel = "  "))
    }

    @Test
    fun nonPositiveRankIsIgnored() {
        assertEquals(CatalogProductBadge.Label("견적 상품"), resolveCatalogProductBadge(bestRank = 0, badgeLabel = "견적 상품"))
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
                isQuoteTarget = false,
                isPurchaseUnavailable = false,
            ),
        )
    }

    // 견적 대상(금액 미표시)은 가격 줄을 비우고 "금액 미표시" 종류를 쓴다.
    @Test
    fun quoteTargetShowsUndisclosedAndHidesPriceLines() {
        assertEquals(
            CatalogPriceSlot(CatalogPriceKind.Undisclosed, originalLabel = null, discountLabel = null, currentLabel = null),
            resolveCatalogPriceSlot(
                currentLabel = null,
                originalLabel = null,
                discountLabel = null,
                isQuoteTarget = true,
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
                isQuoteTarget = false,
                isPurchaseUnavailable = true,
            ),
        )
    }

    // 견적 대상이면서 구매 불가 플래그가 함께 와도 견적 흐름(금액 미표시)이 우선이다.
    @Test
    fun quoteTargetWinsOverPurchaseUnavailable() {
        assertEquals(
            CatalogPriceKind.Undisclosed,
            resolveCatalogPriceSlot(
                currentLabel = null,
                originalLabel = null,
                discountLabel = null,
                isQuoteTarget = true,
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
                isQuoteTarget = false,
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
                isQuoteTarget = false,
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
                isQuoteTarget = false,
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
            isQuoteTarget = false,
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
            isQuoteTarget = false,
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
            isQuoteTarget = false,
            isPurchaseUnavailable = false,
        )

        assertEquals("18%", slot.discountLabel)
    }
}
