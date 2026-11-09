package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.company.CompanyBusinessAreaUiModel
import com.nativelap.eumshoppingmall.ui.company.CompanyCertificationUiModel
import com.nativelap.eumshoppingmall.ui.company.CompanyHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroScreen
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroUiModel
import com.nativelap.eumshoppingmall.ui.policy.PolicyDocumentScreen
import com.nativelap.eumshoppingmall.ui.policy.PolicyDocumentSectionUiModel
import com.nativelap.eumshoppingmall.ui.policy.PolicyDocumentStepUiModel
import com.nativelap.eumshoppingmall.ui.policy.PolicyDocumentUiModel
import com.nativelap.eumshoppingmall.ui.policy.PrivacyConsentDetailScreen
import com.nativelap.eumshoppingmall.ui.policy.PrivacyConsentUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewNotice = listOf(
    NoticeItemUiModel("pinned-privacy", "[공지] 개인정보 처리방침 개정 안내", "0000.00.00", true),
    NoticeItemUiModel("pinned-holiday", "[공지] 설 연휴 배송 일정 안내", "0000.00.00", true),
    NoticeItemUiModel("normal-1", "공지사항 제목이 들어갑니다", "0000.00.00"),
    NoticeItemUiModel("normal-2", "공지사항 제목이 들어갑니다. 제목이 길면 두 줄까지 보여줍니다", "0000.00.00"),
    NoticeItemUiModel("normal-3", "공지사항 제목이 들어갑니다", "0000.00.00"),
    NoticeItemUiModel("normal-4", "공지사항 제목이 들어갑니다", "0000.00.00"),
    NoticeItemUiModel("normal-5", "공지사항 제목이 들어갑니다", "0000.00.00"),
)

@ResponsiveUiPreview
@Composable
private fun NoticeListPreview() = EumShoppingMallTheme { NoticeListScreen(previewNotice, onEvent = {}) }

@ResponsiveUiPreview
@Composable
private fun NoticeEmptyPreview() = EumShoppingMallTheme { NoticeListScreen(emptyList(), onEvent = {}) }

@ResponsiveUiPreview
@Composable
private fun NoticeDetailPreview() = EumShoppingMallTheme {
    NoticeDetailScreen(NoticeDetailUiModel("공지사항", "0000.00.00", "등록된 공지가 없어요. 새로운 소식이 등록되면 여기에서 확인할 수 있어요.", true), onBack = {})
}

@ResponsiveUiPreview
@Composable
private fun InquiryHistoryPreview() = EumShoppingMallTheme {
    InquiryHistoryScreen(
        listOf(
            InquiryHistoryItemUiModel("q1", "현장 납품 가능 일정을 알고 싶습니다", "배송 문의", "2026.11.05", InquiryStatus.Waiting, "경기도 현장 납품 일정 문의"),
            InquiryHistoryItemUiModel("q2", "견적서 발급 방법을 알고 싶습니다", "견적 문의", "2026.11.04", InquiryStatus.Answered, "사업자 견적서 발급 절차 문의"),
            InquiryHistoryItemUiModel("q3", "배송지 변경이 가능한가요?", "배송 문의", "2026.11.02", InquiryStatus.Waiting, "출고 전 배송지 변경 요청"),
            InquiryHistoryItemUiModel("q4", "결제 수단 변경 방법이 궁금합니다", "결제 문의", "2026.11.01", InquiryStatus.Answered, "카드 결제 변경 방법 문의"),
        ),
        selectedStatus = null,
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun InquiryHistoryEmptyPreview() = EumShoppingMallTheme { InquiryHistoryScreen(emptyList(), selectedStatus = null, onEvent = {}) }

@ResponsiveUiPreview
@Composable
private fun InquiryDetailAnsweredPreview() = EumShoppingMallTheme {
    InquiryDetailScreen(
        InquiryDetailUiModel(
            title = "현장 납품 가능 일정 문의",
            statusLabel = "답변완료",
            dateLabel = "2026.11.02",
            typeLabel = "배송 문의",
            orderNumber = "0000000000",
            content = "현장 납품 가능 일정을 알고 싶습니다.",
            attachments = listOf(InquiryAttachmentUiModel("file-1", "첨부파일.jpg")),
            answerDateLabel = "2026.11.03 10:30",
            answer = "문의하신 지역은 납품 가능합니다.",
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun InquiryDetailWaitingPreview() = EumShoppingMallTheme {
    InquiryDetailScreen(
        InquiryDetailUiModel("배송 일정 문의", "답변대기", "2026.11.05", "배송 문의", "0000000001", "현장 납품 가능한 날짜를 확인하고 싶습니다."),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun NotificationPreview() = EumShoppingMallTheme {
    NotificationScreen(
        groups = listOf(
            NotificationGroupUiModel(
                "오늘",
                listOf(
                    NotificationItemUiModel("n1", "배송", "배송이 시작되었습니다", "주문번호 0000000000 상품이 출발했습니다.", "5분 전", true, com.nativelap.eumshoppingmall.R.drawable.ic_truck),
                    NotificationItemUiModel("n2", "견적 답변", "견적서가 도착했습니다", "'견적 문의 제목'에 견적서가 등록되었습니다.", "10분 전", true, com.nativelap.eumshoppingmall.R.drawable.ic_file_text),
                    NotificationItemUiModel("n3", "문의 답변", "문의에 답변이 등록되었습니다", "'문의 제목'", "20분 전", false, com.nativelap.eumshoppingmall.R.drawable.ic_message_square_text),
                ),
            ),
            NotificationGroupUiModel(
                "이전 알림",
                listOf(
                    NotificationItemUiModel("n4", "주문", "주문이 완료되었습니다", "주문번호 0000000000", "2026.11.01", false, com.nativelap.eumshoppingmall.R.drawable.ic_package),
                    NotificationItemUiModel("n5", "공지", "새로운 공지사항", "쇼핑몰 이용 안내가 업데이트되었습니다.", "2026.11.01", false, com.nativelap.eumshoppingmall.R.drawable.ic_megaphone),
                ),
            ),
        ),
        unreadCount = 2,
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun NotificationEmptyPreview() = EumShoppingMallTheme { NotificationScreen(emptyList(), unreadCount = 0, onEvent = {}) }

@ResponsiveUiPreview
@Composable
private fun CustomerSupportPreview() = EumShoppingMallTheme {
    CustomerSupportScreen(
        CustomerSupportUiModel(
            phoneNumber = "1588-0000",
            businessHours = "평일 09:00 – 18:00 (주말·공휴일 휴무)",
            query = "",
            categories = listOf("전체", "주문·결제", "배송", "교환·반품", "회원"),
            selectedCategory = "전체",
            faqs = listOf(
                CustomerSupportFaqUiModel("f1", "주문 후 배송까지 얼마나 걸리나요?", "결제 완료 후 평균 2~3일(영업일 기준) 이내 출고됩니다. 맞춤 제작 상품은 별도 안내드립니다."),
                CustomerSupportFaqUiModel("f2", "가격 문의 상품은 어떻게 구매하나요?", "답변 내용"),
                CustomerSupportFaqUiModel("f3", "세금계산서는 어떻게 발급받나요?", "답변 내용"),
                CustomerSupportFaqUiModel("f4", "교환·반품은 어떻게 신청하나요?", "답변 내용"),
                CustomerSupportFaqUiModel("f5", "사업자 회원으로 전환할 수 있나요?", "답변 내용"),
            ),
            expandedFaqIds = setOf("f1"),
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun CustomerSupportSearchResultsPreview() = EumShoppingMallTheme {
    CustomerSupportScreen(
        CustomerSupportUiModel("1588-0000", "평일 09:00 – 18:00 (주말·공휴일 휴무)", "배송", emptyList(), "전체", listOf(CustomerSupportFaqUiModel("f1", "주문 후 배송까지 얼마나 걸리나요?", "결제 완료 후 평균 2~3일(영업일 기준) 이내 출고됩니다. 맞춤 제작 상품은 별도 안내드립니다."), CustomerSupportFaqUiModel("f2", "배송지를 변경할 수 있나요?", "상품 준비 전 고객센터로 문의해 주세요.")), emptySet()),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun CustomerSupportSearchEmptyPreview() = EumShoppingMallTheme {
    CustomerSupportScreen(CustomerSupportUiModel("1588-0000", "평일 09:00 – 18:00 (주말·공휴일 휴무)", "검색어", emptyList(), "전체", emptyList(), emptySet()), onEvent = {})
}

private const val PREVIEW_CLAUSE = "조항 내용이 들어갑니다."
private const val PREVIEW_CLAUSE_FIRST_ITEM = "① 항 내용이 들어갑니다."
private const val PREVIEW_CLAUSE_SECOND_ITEM = "② 항 내용이 들어갑니다."

// Figma 약관 조항 제목 24개(제1조~제24조). 목차 개수는 조항 수와 같아야 합니다.
private val previewTermsArticleTitles = listOf(
    "제1조(목적)", "제2조(정의)", "제3조(약관 등의 명시와 설명 및 개정)", "제4조(서비스의 제공 및 변경)", "제5조(서비스의 중단)",
    "제6조(회원가입)", "제7조(사업자 회원)", "제8조(회원 탈퇴 및 자격 상실 등)", "제9조(회원에 대한 통지)",
    "제10조(구매신청)", "제11조(계약의 성립)", "제12조(지급방법)", "제13조(가격 문의 상품과 견적 문의)", "제14조(세금계산서 및 현금영수증)",
    "제15조(재화 등의 공급)", "제16조(환급)", "제17조(청약철회 등)", "제18조(청약철회 등의 효과)",
    "제19조(개인정보보호)", "제20조(회사의 의무)", "제21조(회원의 아이디 및 비밀번호에 대한 의무)", "제22조(이용자의 의무)", "제23조(분쟁해결)", "제24조(재판권 및 준거법)",
)

// 장 제목은 chapterTitle로, 부칙은 제목 없는 섹션으로 둔다. 목차에는 조항만 들어간다.
private val previewTermsSections: List<PolicyDocumentSectionUiModel> = previewTermsArticleTitles.mapIndexed { index, title ->
    val chapterTitle = when (index) {
        0 -> "제1장 총칙"
        5 -> "제2장 회원"
        9 -> "제3장 구매·결제"
        14 -> "제4장 배송·청약철회"
        18 -> "제5장 보칙"
        else -> null
    }
    val paragraphs = if (index == 1 || index == 16) {
        listOf(PREVIEW_CLAUSE, PREVIEW_CLAUSE_FIRST_ITEM, PREVIEW_CLAUSE_SECOND_ITEM)
    } else {
        listOf(PREVIEW_CLAUSE)
    }
    PolicyDocumentSectionUiModel(title, paragraphs, chapterTitle = chapterTitle)
} + PolicyDocumentSectionUiModel("", listOf("이 약관은 0000년 00월 00일부터 시행합니다."), chapterTitle = "부칙")

private val previewPrivacySections = listOf(
    PolicyDocumentSectionUiModel("1. 개인정보의 처리 목적", listOf(PREVIEW_CLAUSE)),
    PolicyDocumentSectionUiModel("2. 처리하는 개인정보의 항목", listOf(PREVIEW_CLAUSE, PREVIEW_CLAUSE_FIRST_ITEM, PREVIEW_CLAUSE_SECOND_ITEM)),
    PolicyDocumentSectionUiModel("3. 개인정보의 처리 및 보유 기간", listOf(PREVIEW_CLAUSE, PREVIEW_CLAUSE_FIRST_ITEM, PREVIEW_CLAUSE_SECOND_ITEM)),
    PolicyDocumentSectionUiModel(
        "4. 개인정보의 제3자 제공",
        listOf(PREVIEW_CLAUSE),
        keyValueRows = listOf("제공받는 자" to "000", "제공 목적" to "000", "제공 항목" to "000", "보유·이용 기간" to "000"),
    ),
    PolicyDocumentSectionUiModel(
        "5. 개인정보 처리업무의 위탁",
        listOf(PREVIEW_CLAUSE),
        keyValueRows = listOf("수탁자" to "000", "위탁 업무" to "000", "위탁 항목" to "000", "보유·이용 기간" to "000"),
    ),
    PolicyDocumentSectionUiModel("6. 개인정보의 파기 절차 및 방법", listOf(PREVIEW_CLAUSE)),
    PolicyDocumentSectionUiModel("7. 정보주체와 법정대리인의 권리·의무 및 행사방법", listOf(PREVIEW_CLAUSE)),
    PolicyDocumentSectionUiModel("8. 개인정보의 안전성 확보조치", listOf(PREVIEW_CLAUSE)),
    PolicyDocumentSectionUiModel("9. 개인정보 자동 수집 장치의 설치·운영 및 거부", listOf(PREVIEW_CLAUSE)),
    PolicyDocumentSectionUiModel(
        "10. 개인정보 보호책임자",
        listOf(PREVIEW_CLAUSE),
        keyValueRows = listOf("성명" to "000", "직책" to "000", "연락처" to "0000-0000", "이메일" to "000@000"),
    ),
    PolicyDocumentSectionUiModel("11. 권익침해 구제방법", listOf(PREVIEW_CLAUSE)),
    PolicyDocumentSectionUiModel("12. 개인정보 처리방침의 변경", listOf(PREVIEW_CLAUSE)),
)

private val previewGuideSections = listOf(
    PolicyDocumentSectionUiModel("1. 회원가입 안내", listOf("안내 내용이 들어갑니다.", PREVIEW_CLAUSE_FIRST_ITEM, PREVIEW_CLAUSE_SECOND_ITEM)),
    PolicyDocumentSectionUiModel(
        "2. 주문·결제 안내",
        listOf("안내 내용이 들어갑니다."),
        steps = listOf(
            PolicyDocumentStepUiModel("상품 선택", "상품과 옵션을 골라 장바구니에 담습니다."),
            PolicyDocumentStepUiModel("주문서 작성", "배송지와 결제수단을 입력합니다."),
            PolicyDocumentStepUiModel("결제 완료", "결제하면 주문이 접수됩니다."),
        ),
    ),
    PolicyDocumentSectionUiModel(
        "3. 가격 문의 상품과 견적 문의",
        listOf("안내 내용이 들어갑니다."),
        steps = listOf(
            PolicyDocumentStepUiModel("견적 문의", "가격 문의 상품을 담아 견적을 요청합니다."),
            PolicyDocumentStepUiModel("견적서 발급", "담당자가 확인한 뒤 견적서를 보내 드립니다."),
            PolicyDocumentStepUiModel("주문 진행", "견적을 확인한 뒤 주문을 진행합니다."),
        ),
    ),
    PolicyDocumentSectionUiModel("4. 배송 안내", listOf("안내 내용이 들어갑니다.")),
    PolicyDocumentSectionUiModel("5. 교환·반품 안내", listOf("안내 내용이 들어갑니다.", PREVIEW_CLAUSE_FIRST_ITEM, PREVIEW_CLAUSE_SECOND_ITEM)),
    PolicyDocumentSectionUiModel("6. 고객센터 안내", listOf("안내 내용이 들어갑니다.")),
)

@ResponsiveUiPreview
@Composable
private fun PolicyDocumentPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(PolicyDocumentUiModel("개인정보처리방침", "0000.00.00", true, previewPrivacySections, previousVersionLabel = "이전 약관 보기", versionOptions = listOf("0000.00.00 시행 예정", "0000.00.00 시행 (현재)", "0000.00.00 시행")), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun PreviousTermsPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(
        PolicyDocumentUiModel(
            "이용약관",
            "0000.00.00",
            true,
            previewTermsSections,
            tableOfContents = previewTermsArticleTitles,
            previousVersionLabel = "0000.00.00 시행",
            noticeLabel = "이전 약관입니다. 현재 약관은 0000.00.00부터 시행 중입니다.",
            isClauseDocument = true,
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun UpcomingTermsPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(
        PolicyDocumentUiModel(
            "이용약관",
            "0000.00.00",
            true,
            previewTermsSections,
            tableOfContents = previewTermsArticleTitles,
            previousVersionLabel = "이전 약관 보기",
            noticeLabel = "0000.00.00부터 개정 약관이 시행됩니다.",
            isClauseDocument = true,
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun TermsVersionPickerPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(
        PolicyDocumentUiModel(
            "이용약관", "0000.00.00", true, previewTermsSections,
            tableOfContents = previewTermsArticleTitles,
            previousVersionLabel = "이전 약관 보기",
            versionOptions = listOf("0000.00.00 시행 예정", "0000.00.00 시행 (현재)", "0000.00.00 시행", "0000.00.00 시행"),
            selectedVersionIndex = 1,
            isVersionPickerVisible = true,
            isClauseDocument = true,
            upcomingVersionIndex = 0,
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun TermsDocumentPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(
        PolicyDocumentUiModel(
            "이용약관",
            "0000.00.00",
            true,
            previewTermsSections,
            tableOfContents = previewTermsArticleTitles,
            previousVersionLabel = "이전 약관 보기",
            isClauseDocument = true,
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun PrivacyConsentDetailPreview() = EumShoppingMallTheme {
    PrivacyConsentDetailScreen(
        PrivacyConsentUiModel(
            effectiveDate = "0000.00.00",
            description = "회사는 회원가입과 서비스 제공을 위해 아래와 같이 개인정보를 수집·이용합니다. 내용이 들어갑니다.",
            purpose = "회원 식별, 주문, 배송, 고객 상담 (내용이 들어갑니다.)",
            collectedItems = "000",
            retentionPeriod = "000",
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun UsageGuidePreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(PolicyDocumentUiModel("이용안내", "0000.00.00", false, previewGuideSections), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun CompanyIntroPreview() = EumShoppingMallTheme {
    CompanyIntroScreen(
        CompanyIntroUiModel(
            businessAreas = listOf(
                CompanyBusinessAreaUiModel("안전용품 유통", "개인보호장비·작업복 등 현장 필수품", com.nativelap.eumshoppingmall.R.drawable.ic_hard_hat),
                CompanyBusinessAreaUiModel("현장 납품", "건설·산업 현장 정기 납품", com.nativelap.eumshoppingmall.R.drawable.ic_truck),
                CompanyBusinessAreaUiModel("맞춤 제작", "로고 인쇄·사양 맞춤 제작", com.nativelap.eumshoppingmall.R.drawable.ic_shirt),
                CompanyBusinessAreaUiModel("안전 컨설팅", "현장 진단과 맞춤 안전 제안", com.nativelap.eumshoppingmall.R.drawable.ic_shield_check),
            ),
            history = listOf(
                CompanyHistoryItemUiModel("0000", "00월 연혁 내용이 들어갑니다."),
                CompanyHistoryItemUiModel("0000", "00월 연혁 내용이 들어갑니다."),
                CompanyHistoryItemUiModel("0000", "00월 연혁 내용이 들어갑니다."),
            ),
            certifications = listOf(
                CompanyCertificationUiModel("인증서", "인증 내용이 들어갑니다"),
                CompanyCertificationUiModel("인증서", "인증 내용이 들어갑니다"),
            ),
            partners = listOf("파트너 로고", "파트너 로고", "파트너 로고"),
            address = "서울특별시 00구 00로 00, 00빌딩",
            phone = "0000-0000 (평일 00:00 – 00:00)",
            email = "info@example.com",
        ),
        onEvent = {},
    )
}
