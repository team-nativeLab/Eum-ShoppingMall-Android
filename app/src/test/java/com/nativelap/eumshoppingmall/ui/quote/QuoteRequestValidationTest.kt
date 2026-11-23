package com.nativelap.eumshoppingmall.ui.quote

import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteDeliveryDateStatus
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestField
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.fieldErrors
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.isQuoteProductErrorVisible
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.isSubmittable
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.nextIsProductErrorRequested
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.quoteDeliveryDateStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class QuoteRequestValidationTest {
    private val today = LocalDate.of(2026, 11, 10)

    @Test
    fun emptyDateIsEmptyStatusNotAnError() {
        assertEquals(QuoteDeliveryDateStatus.Empty, quoteDeliveryDateStatus("  ", today))
    }

    @Test
    fun dateWithDotsOrMissingZeroPaddingIsInvalidFormat() {
        assertEquals(QuoteDeliveryDateStatus.InvalidFormat, quoteDeliveryDateStatus("2026.12.31", today))
        assertEquals(QuoteDeliveryDateStatus.InvalidFormat, quoteDeliveryDateStatus("2026-1-5", today))
        assertEquals(QuoteDeliveryDateStatus.InvalidFormat, quoteDeliveryDateStatus("2026-02-30", today))
    }

    @Test
    fun leapDayIsValidOnlyInLeapYears() {
        val leapToday = LocalDate.of(2028, 1, 1)

        assertEquals(QuoteDeliveryDateStatus.Valid, quoteDeliveryDateStatus("2028-02-29", leapToday))
        assertEquals(QuoteDeliveryDateStatus.InvalidFormat, quoteDeliveryDateStatus("2027-02-29", today))
    }

    @Test
    fun pastDateIsBeforeToday() {
        assertEquals(QuoteDeliveryDateStatus.BeforeToday, quoteDeliveryDateStatus("2026-11-09", today))
    }

    @Test
    fun todayAndLaterDatesAreValid() {
        assertEquals(QuoteDeliveryDateStatus.Valid, quoteDeliveryDateStatus("2026-11-10", today))
        assertEquals(QuoteDeliveryDateStatus.Valid, quoteDeliveryDateStatus("2026-12-31", today))
    }

    @Test
    fun submitRequiresDeliveryAddressAndValidDate() {
        assertTrue(completeModel(deliveryAddress = "서울시 강남구", date = "2026-12-31").isSubmittable(today))
        assertFalse(completeModel(deliveryAddress = "", date = "2026-12-31").isSubmittable(today))
        assertFalse(completeModel(deliveryAddress = "서울", date = "").isSubmittable(today))
        assertFalse(completeModel(deliveryAddress = "서울", date = "2026.12.31").isSubmittable(today))
        assertFalse(completeModel(deliveryAddress = "서울", date = "2026-11-09").isSubmittable(today))
    }

    @Test
    fun submitStillRequiresContactAndConsent() {
        assertFalse(completeModel(deliveryAddress = "서울", date = "2026-12-31", consent = false).isSubmittable(today))
    }

    @Test
    fun submitRequiresAtLeastOneQuoteProductAndMarksTheField() {
        val noProducts = completeModel(deliveryAddress = "서울", date = "2026-12-31").copy(products = emptyList())
        val withProduct = noProducts.copy(
            products = listOf(CatalogProductUiModel("p1", "안전모", CatalogPriceUiModel.QuoteRequired)),
        )

        assertFalse(noProducts.isSubmittable(today))
        assertTrue(QuoteRequestField.Products in noProducts.fieldErrors(today))
        assertTrue(withProduct.isSubmittable(today))
    }

    @Test
    fun productErrorIsHiddenWhenEmptyBeforeAnySubmitAttempt() {
        assertFalse(isQuoteProductErrorVisible(isErrorRequested = false, productCount = 0))
    }

    @Test
    fun productErrorShowsAfterSubmitAttemptWhenEmpty() {
        assertTrue(isQuoteProductErrorVisible(isErrorRequested = true, productCount = 0))
    }

    @Test
    fun productErrorHidesOnceAProductIsAdded() {
        assertFalse(isQuoteProductErrorVisible(isErrorRequested = true, productCount = 1))
    }

    @Test
    fun productEventsOtherThanSubmitDoNotSetTheAttemptFlag() {
        assertFalse(nextIsProductErrorRequested(current = false, event = QuoteRequestEvent.AddProductClicked))
        assertFalse(nextIsProductErrorRequested(current = false, event = QuoteRequestEvent.ProductRemoved("p1")))
    }

    @Test
    fun productErrorFollowsAttemptAddAndRemoveAsRouteTransitions() {
        var isErrorRequested = false
        var productCount = 0

        // 진입: 시도 전 빈 목록은 숨김
        assertFalse(isQuoteProductErrorVisible(isErrorRequested, productCount))

        // 비활성 영역 탭(SubmitClicked): 빈 목록이면 표시
        isErrorRequested = nextIsProductErrorRequested(isErrorRequested, QuoteRequestEvent.SubmitClicked)
        assertTrue(isQuoteProductErrorVisible(isErrorRequested, productCount))

        // 상품 추가: 숨김
        productCount = 1
        isErrorRequested = nextIsProductErrorRequested(isErrorRequested, QuoteRequestEvent.AddProductClicked)
        assertFalse(isQuoteProductErrorVisible(isErrorRequested, productCount))

        // 마지막 상품 삭제: 시도 이력이 남아 있으므로 다시 표시
        productCount = 0
        isErrorRequested = nextIsProductErrorRequested(isErrorRequested, QuoteRequestEvent.ProductRemoved("p1"))
        assertTrue(isQuoteProductErrorVisible(isErrorRequested, productCount))
    }

    @Test
    fun invalidDateShowsFieldErrorButEmptyDateDoesNot() {
        assertEquals(setOf(QuoteRequestField.DeliveryDate), completeModel(deliveryAddress = "서울", date = "2026.12.31").fieldErrors(today))
        assertEquals(setOf(QuoteRequestField.DeliveryDate), completeModel(deliveryAddress = "서울", date = "2026-11-09").fieldErrors(today))
        assertTrue(completeModel(deliveryAddress = "서울", date = "").fieldErrors(today).isEmpty())
    }

    private fun completeModel(
        deliveryAddress: String,
        date: String,
        consent: Boolean = true,
    ): QuoteRequestUiModel = QuoteRequestUiModel(
        products = listOf(CatalogProductUiModel("p1", "안전모", CatalogPriceUiModel.QuoteRequired)),
        companyName = "회사",
        contactName = "담당자",
        phoneNumber = "010-0000-0000",
        email = "buyer@example.com",
        preferredDeliveryDate = date,
        deliveryAddress = deliveryAddress,
        isPrivacyConsentChecked = consent,
    )
}
