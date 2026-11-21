package com.nativelap.eumshoppingmall.ui.quote.viewmodel

import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuotePreviewMappersTest {
    @Test fun completeAndHistoryReflectSubmittedInputAndRemainMarkedAsPreview() {
        val input = QuoteRequestUiModel(
            products = listOf(CatalogProductUiModel("p1", "안전모", CatalogPriceUiModel.QuoteRequired)),
            companyName = "이음건설",
            contactName = "김담당",
            preferredDeliveryDate = "2026-12-31",
        )
        val complete = input.toQuoteCompletePreview("대량 구매")
        val history = input.toQuoteHistoryPreview("preview-1", "현재 세션", "미리보기", "상품 없음", "희망 납기")
        assertEquals("이음건설", complete.companyName)
        assertEquals("김담당", complete.contactName)
        assertEquals("안전모", complete.productsSummary)
        assertEquals("2026-12-31", complete.preferredDeliveryDate)
        assertEquals("미리보기", history.inquiryNumber)
        assertEquals(QuoteInquiryStatus.Preview, history.status)
        assertTrue(history.productsSummary.contains("안전모"))
        assertTrue(history.productsSummary.contains("2026-12-31"))
    }

    @Test fun missingRestoredPreviewUsesExplicitUnavailableModelsWithoutSampleIdentifiers() {
        val complete = unavailableQuoteCompletePreview("미리보기 내용을 복원하지 못했습니다")
        val detail = unavailableQuoteInquiryDetailPreview("미리보기 내용을 복원하지 못했습니다")

        assertTrue(complete.isPreviewUnavailable)
        assertTrue(detail.isPreviewUnavailable)
        assertEquals("", complete.inquiryNumber)
        assertEquals("", detail.inquiryNumber)
        assertEquals("미리보기 내용을 복원하지 못했습니다", complete.productsSummary)
        assertEquals("미리보기 내용을 복원하지 못했습니다", detail.title)
    }
}
