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

    @Test
    fun soldOutProductShowsUnavailableAndHidesPriceLines() {
        assertEquals(
            CatalogPriceSlot(CatalogPriceKind.Unavailable, originalLabel = null, discountLabel = null, currentLabel = null),
            resolveCatalogPriceSlot(
                currentLabel = "₩18,000",
                originalLabel = "₩22,000",
                discountLabel = "18%",
                isInquiry = false,
                isSoldOut = true,
            ),
        )
    }

    @Test
    fun quoteProductShowsInquiryKind() {
        assertEquals(
            CatalogPriceKind.Inquiry,
            resolveCatalogPriceSlot(currentLabel = null, originalLabel = null, discountLabel = null, isInquiry = true, isSoldOut = false).kind,
        )
    }

    @Test
    fun missingCurrentLabelFallsBackToInquiry() {
        assertEquals(
            CatalogPriceKind.Inquiry,
            resolveCatalogPriceSlot(currentLabel = " ", originalLabel = null, discountLabel = null, isInquiry = false, isSoldOut = false).kind,
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
                isInquiry = false,
                isSoldOut = false,
            ),
        )
    }

    @Test
    fun plainPriceHasNoOriginalOrDiscountLine() {
        assertEquals(
            CatalogPriceSlot(CatalogPriceKind.Price, originalLabel = null, discountLabel = null, currentLabel = "₩54,000"),
            resolveCatalogPriceSlot(currentLabel = "₩54,000", originalLabel = null, discountLabel = null, isInquiry = false, isSoldOut = false),
        )
    }

    @Test
    fun blankDiscountIsTreatedAsMissing() {
        val slot = resolveCatalogPriceSlot(
            currentLabel = "₩54,000",
            originalLabel = "₩60,000",
            discountLabel = " ",
            isInquiry = false,
            isSoldOut = false,
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
            isInquiry = false,
            isSoldOut = false,
        )

        assertNull(slot.discountLabel)
    }

    @Test
    fun nonZeroDiscountIsKept() {
        val slot = resolveCatalogPriceSlot(
            currentLabel = "₩18,000",
            originalLabel = "₩22,000",
            discountLabel = "18%",
            isInquiry = false,
            isSoldOut = false,
        )

        assertEquals("18%", slot.discountLabel)
    }
}
