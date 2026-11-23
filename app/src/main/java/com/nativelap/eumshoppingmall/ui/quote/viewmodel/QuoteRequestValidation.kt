package com.nativelap.eumshoppingmall.ui.quote.viewmodel

import java.time.LocalDate
import java.time.format.DateTimeParseException

/** 희망 납기 입력 상태. 비어 있으면 필수 표시만 두고 오류로 치지 않는다. */
internal enum class QuoteDeliveryDateStatus {
    Empty,
    InvalidFormat,
    BeforeToday,
    Valid,
}

private val DELIVERY_DATE_PATTERN = Regex("""\d{4}-\d{2}-\d{2}""")

/**
 * 희망 납기를 검사한다. 형식은 YYYY-MM-DD이고, 오늘 이후(오늘 포함)여야 한다.
 * 오늘 포함 여부는 가정이다(보고서 참고). 날짜는 호출부가 넘긴 [today]로 판단해 테스트에서 고정한다.
 */
internal fun quoteDeliveryDateStatus(text: String, today: LocalDate): QuoteDeliveryDateStatus {
    val trimmed = text.trim()
    if (trimmed.isEmpty()) return QuoteDeliveryDateStatus.Empty
    if (!DELIVERY_DATE_PATTERN.matches(trimmed)) return QuoteDeliveryDateStatus.InvalidFormat
    val date = try {
        LocalDate.parse(trimmed)
    } catch (exception: DateTimeParseException) {
        return QuoteDeliveryDateStatus.InvalidFormat
    }
    return if (date.isBefore(today)) QuoteDeliveryDateStatus.BeforeToday else QuoteDeliveryDateStatus.Valid
}

/**
 * 견적 접수에 필요한 입력이 모두 유효한지 판단한다.
 * 상품 1개 이상, 연락처 4개와 개인정보 동의, 납품 지역(자유 입력 1자 이상), 희망 납기(유효한 날짜)가 필요하다.
 */
internal fun QuoteRequestUiModel.isSubmittable(today: LocalDate): Boolean = companyName.isNotBlank() &&
    products.isNotEmpty() &&
    contactName.isNotBlank() &&
    phoneNumber.isNotBlank() &&
    email.isNotBlank() &&
    deliveryAddress.isNotBlank() &&
    quoteDeliveryDateStatus(preferredDeliveryDate, today) == QuoteDeliveryDateStatus.Valid &&
    isPrivacyConsentChecked

/**
 * 상품 필수 오류를 보일지. 사용자가 제출을 시도한 뒤 상품이 비어 있을 때만 보여 준다.
 * 제출 전 빈 화면에서 바로 빨간 오류를 띄우지 않으려고, 시도 여부([isErrorRequested])를 따로 받는다.
 */
internal fun isQuoteProductErrorVisible(
    isErrorRequested: Boolean,
    productCount: Int,
): Boolean = isErrorRequested && productCount == 0

/**
 * 상품 오류 시도 플래그의 다음 값. 제출 시도(비활성 영역 탭 포함)만 켠다.
 * 상품 추가·삭제는 플래그를 바꾸지 않는다. 표시 여부는 [isQuoteProductErrorVisible]이 상품 수로 매번 계산한다.
 */
internal fun nextIsProductErrorRequested(
    current: Boolean,
    event: QuoteRequestEvent,
): Boolean = when (event) {
    QuoteRequestEvent.SubmitClicked -> true
    else -> current
}

/** 날짜 형식과 상품 필수 여부를 필드 근처에 표시한다. */
internal fun QuoteRequestUiModel.fieldErrors(today: LocalDate): Set<QuoteRequestField> {
    val deliveryDateStatus = quoteDeliveryDateStatus(preferredDeliveryDate, today)
    return buildSet {
        if (deliveryDateStatus == QuoteDeliveryDateStatus.InvalidFormat || deliveryDateStatus == QuoteDeliveryDateStatus.BeforeToday) {
            add(QuoteRequestField.DeliveryDate)
        }
        if (products.isEmpty()) add(QuoteRequestField.Products)
    }
}
