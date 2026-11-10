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
 * 연락처 4개와 개인정보 동의, 납품 지역(자유 입력 1자 이상), 희망 납기(유효한 날짜)가 모두 필요하다.
 */
internal fun QuoteRequestUiModel.isSubmittable(today: LocalDate): Boolean = companyName.isNotBlank() &&
    contactName.isNotBlank() &&
    phoneNumber.isNotBlank() &&
    email.isNotBlank() &&
    deliveryAddress.isNotBlank() &&
    quoteDeliveryDateStatus(preferredDeliveryDate, today) == QuoteDeliveryDateStatus.Valid &&
    isPrivacyConsentChecked

/** 입력된 값에 형식·날짜 오류가 있을 때만 해당 필드를 오류로 표시한다. 비어 있는 필수 항목은 제출 버튼으로만 막는다. */
internal fun QuoteRequestUiModel.fieldErrors(today: LocalDate): Set<QuoteRequestField> {
    val deliveryDateStatus = quoteDeliveryDateStatus(preferredDeliveryDate, today)
    return if (deliveryDateStatus == QuoteDeliveryDateStatus.InvalidFormat || deliveryDateStatus == QuoteDeliveryDateStatus.BeforeToday) {
        setOf(QuoteRequestField.DeliveryDate)
    } else {
        emptySet()
    }
}
