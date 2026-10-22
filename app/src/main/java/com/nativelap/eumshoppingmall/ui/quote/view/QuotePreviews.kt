package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.catalog.CatalogPriceUiModel
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteAttachmentUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteCompleteUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteDetailItemUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryFilter
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteHistoryUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryDetailUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryListItemUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteInquiryStatus
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestField
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestType
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewQuoteProducts = listOf(
    CatalogProductUiModel(
        id = "preview-quote-helmet",
        name = "산업용 안전모 기본형",
        subtitle = "흰색 · 조절형",
        price = CatalogPriceUiModel.QuoteRequired,
    ),
    CatalogProductUiModel(
        id = "preview-quote-gloves",
        name = "내절단 기능성 보호 장갑",
        subtitle = "L 사이즈 · 2개 묶음",
        price = CatalogPriceUiModel.QuoteRequired,
    ),
)

private val previewQuoteAttachment = QuoteAttachmentUiModel(
    id = "preview-attachment-spec-sheet",
    fileName = "제품_사양서.pdf",
    formatLabel = "PDF",
    sizeLabel = "1.2MB",
)

private val previewQuoteRequest = QuoteRequestUiModel(
    products = previewQuoteProducts,
    requestType = QuoteRequestType.BulkPurchase,
    companyName = "예시산업 주식회사",
    contactName = "홍길동 (예시)",
    phoneNumber = "010-0000-0000",
    email = "preview@example.com",
    preferredDeliveryDate = "2026.11.15",
    deliveryAddress = "서울시 예시구 테스트로 100 (프리뷰 주소)",
    requestDetails = "현장 작업자 30명에게 지급할 예정입니다. 납기와 대량 구매 단가를 안내해 주세요.",
    attachments = listOf(previewQuoteAttachment),
    isPrivacyConsentChecked = true,
    isSubmitEnabled = true,
)

private val previewQuoteHistory = QuoteHistoryUiModel(
    selectedFilter = QuoteHistoryFilter.All,
    inquiries = listOf(
        QuoteInquiryListItemUiModel(
            id = "preview-inquiry-received",
            dateLabel = "2026.10.20",
            inquiryNumber = "Q-20261020-001",
            title = "현장 안전용품 대량 구매 문의",
            productsSummary = "산업용 안전모 외 2개 상품",
            status = QuoteInquiryStatus.Received,
        ),
        QuoteInquiryListItemUiModel(
            id = "preview-inquiry-consulting",
            dateLabel = "2026.10.18",
            inquiryNumber = "Q-20261018-004",
            title = "보호 장갑 정기 납품 문의",
            productsSummary = "내절단 기능성 보호 장갑 외 1개 상품",
            status = QuoteInquiryStatus.Consulting,
        ),
        QuoteInquiryListItemUiModel(
            id = "preview-inquiry-estimate",
            dateLabel = "2026.10.15",
            inquiryNumber = "Q-20261015-002",
            title = "안전모 100개 견적 요청",
            productsSummary = "산업용 안전모 기본형 · 100개",
            status = QuoteInquiryStatus.EstimateSent,
            estimateAvailable = true,
        ),
        QuoteInquiryListItemUiModel(
            id = "preview-inquiry-complete",
            dateLabel = "2026.10.10",
            inquiryNumber = "Q-20261010-003",
            title = "보호구 정기 납품 상담",
            productsSummary = "보호구 세트 외 3개 상품",
            status = QuoteInquiryStatus.Complete,
        ),
        QuoteInquiryListItemUiModel(
            id = "preview-inquiry-cancelled",
            dateLabel = "2026.10.02",
            inquiryNumber = "Q-20261002-001",
            title = "작업화 단체 구매 문의",
            productsSummary = "미끄럼 방지 작업화 · 20개",
            status = QuoteInquiryStatus.Cancelled,
        ),
    ),
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "견적 문의 입력")
@Composable
private fun QuoteRequestPreview() {
    EumShoppingMallTheme {
        QuoteRequestScreen(
            uiModel = previewQuoteRequest,
            onEvent = {},
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 800,
    fontScale = 1.3f,
    name = "견적 문의 좁은 화면 · 큰 글자 · 오류",
)
@Composable
private fun QuoteRequestNarrowLargeTextPreview() {
    EumShoppingMallTheme {
        QuoteRequestScreen(
            uiModel = previewQuoteRequest.copy(
                products = previewQuoteProducts.take(1),
                companyName = "",
                contactName = "",
                email = "",
                deliveryAddress = "",
                errors = setOf(
                    QuoteRequestField.Company,
                    QuoteRequestField.ContactName,
                    QuoteRequestField.Email,
                    QuoteRequestField.DeliveryAddress,
                ),
                isSubmitEnabled = false,
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "견적 문의 완료")
@Composable
private fun QuoteCompletePreview() {
    EumShoppingMallTheme {
        QuoteCompleteScreen(
            uiModel = QuoteCompleteUiModel(
                inquiryNumber = "Q-20261021-001",
                responseTimeLabel = "영업일 기준 1~2일 이내",
                requestTypeLabel = "대량 구매",
                companyName = "예시산업 주식회사",
                contactName = "홍길동 (예시)",
                productsSummary = "산업용 안전모 외 1개 상품",
                preferredDeliveryDate = "2026.11.15",
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 720, fontScale = 1.3f, name = "견적 문의 완료 · 좁은 화면")
@Composable
private fun QuoteCompleteNarrowLargeTextPreview() {
    EumShoppingMallTheme {
        QuoteCompleteScreen(
            uiModel = QuoteCompleteUiModel(
                inquiryNumber = "Q-20261021-001",
                responseTimeLabel = "영업일 기준 1~2일 이내 담당자가 연락드릴 예정입니다.",
                requestTypeLabel = "대량 구매",
                companyName = "예시산업 주식회사",
                contactName = "홍길동 (예시)",
                productsSummary = "산업용 안전모 기본형 외 현장 작업용 보호 장비 12종",
                preferredDeliveryDate = "2026.11.15",
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "견적 문의 목록")
@Composable
private fun QuoteHistoryPreview() {
    EumShoppingMallTheme {
        QuoteHistoryScreen(
            uiModel = previewQuoteHistory,
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 720, fontScale = 1.3f, name = "견적 문의 목록 · 좁은 화면")
@Composable
private fun QuoteHistoryNarrowLargeTextPreview() {
    EumShoppingMallTheme {
        QuoteHistoryScreen(
            uiModel = previewQuoteHistory,
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "견적 문의 빈 목록")
@Composable
private fun QuoteHistoryEmptyPreview() {
    EumShoppingMallTheme {
        QuoteHistoryScreen(
            uiModel = QuoteHistoryUiModel(selectedFilter = QuoteHistoryFilter.All),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "견적 문의 상세 · 견적 도착")
@Composable
private fun QuoteInquiryDetailPreview() {
    EumShoppingMallTheme {
        QuoteInquiryDetailScreen(
            uiModel = QuoteInquiryDetailUiModel(
                title = "현장 안전용품 대량 구매 문의",
                dateLabel = "2026.10.20",
                inquiryNumber = "Q-20261020-001",
                status = QuoteInquiryStatus.EstimateSent,
                managerReply = "요청하신 제품의 견적을 안내드립니다. 추가 문의가 있으시면 담당자에게 연락해 주세요.",
                amountLabel = "1,250,000원",
                validityLabel = "2026.11.20까지",
                managerName = "담당자 예시",
                managerPhone = "02-0000-0000",
                estimateFile = previewQuoteAttachment,
                companyName = "예시산업 주식회사",
                contactName = "홍길동 (예시)",
                contactPhone = "010-0000-0000",
                contactEmail = "preview@example.com",
                items = listOf(
                    QuoteDetailItemUiModel("preview-detail-helmet", "산업용 안전모 기본형", "100개"),
                    QuoteDetailItemUiModel("preview-detail-gloves", "내절단 기능성 보호 장갑", "30개"),
                ),
                preferredDeliveryDate = "2026.11.15",
                deliveryAddress = "서울시 예시구 테스트로 100 (프리뷰 주소)",
                requestDetails = "현장 작업자 지급용 제품입니다. 납기 일정을 함께 확인해 주세요.",
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 720, fontScale = 1.3f, name = "견적 문의 상세 · 좁은 화면")
@Composable
private fun QuoteInquiryDetailNarrowLargeTextPreview() {
    EumShoppingMallTheme {
        QuoteInquiryDetailScreen(
            uiModel = QuoteInquiryDetailUiModel(
                title = "현장 안전용품 대량 구매 문의",
                dateLabel = "2026.10.20",
                inquiryNumber = "Q-20261020-001",
                status = QuoteInquiryStatus.EstimateSent,
                managerReply = "요청하신 제품의 견적을 안내드립니다.",
                amountLabel = "1,250,000원",
                companyName = "예시산업 주식회사",
                contactName = "홍길동 (예시)",
                contactPhone = "010-0000-0000",
                contactEmail = "preview@example.com",
                requestAttachments = listOf(previewQuoteAttachment),
            ),
            onEvent = {},
        )
    }
}
