package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.runtime.Composable
import com.nativelap.eumshoppingmall.ui.company.CompanyBusinessAreaUiModel
import com.nativelap.eumshoppingmall.ui.company.CompanyCertificationUiModel
import com.nativelap.eumshoppingmall.ui.company.CompanyHistoryItemUiModel
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroScreen
import com.nativelap.eumshoppingmall.ui.company.CompanyIntroUiModel
import com.nativelap.eumshoppingmall.ui.policy.PolicyDocumentScreen
import com.nativelap.eumshoppingmall.ui.policy.PolicyDocumentSectionUiModel
import com.nativelap.eumshoppingmall.ui.policy.PolicyDocumentUiModel
import com.nativelap.eumshoppingmall.ui.policy.PrivacyConsentDetailScreen
import com.nativelap.eumshoppingmall.ui.policy.PrivacyConsentUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewNotice = listOf(
    NoticeItemUiModel("pinned", "배송 지연 안내가 길어질 때 두 줄까지 표시되는 공지", "2026.11.01", true),
    NoticeItemUiModel("normal", "이음산업건설 쇼핑몰 오픈 안내", "2026.10.20"),
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
    NoticeDetailScreen(NoticeDetailUiModel(previewNotice.first().title, "2026.11.01", "공지 내용이 긴 경우에도 화면 높이에 맞춰 스크롤되고 끝까지 확인할 수 있습니다.\n\n배송 및 구매 관련 상세 안내입니다.", true), onBack = {})
}

@ResponsiveUiPreview
@Composable
private fun InquiryHistoryPreview() = EumShoppingMallTheme {
    InquiryHistoryScreen(
        listOf(InquiryHistoryItemUiModel("q1", "현장 납품 가능 일정을 알고 싶습니다", "배송 문의", "2026.11.02", InquiryStatus.Answered, "경기도 현장 납품 일정 문의", "가능한 일정을 확인해 안내드리겠습니다.")),
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
            content = "서울 현장으로 안전모와 안전화를 납품받고 싶습니다. 가능한 일정과 배송 조건을 안내해 주세요.",
            attachments = listOf(InquiryAttachmentUiModel("file-1", "현장_납품_정보.jpg")),
            answerDateLabel = "2026.11.03 10:30",
            answer = "문의하신 지역은 납품 가능합니다. 희망 일정을 알려 주시면 확인하겠습니다.",
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
        groups = listOf(NotificationGroupUiModel("오늘", listOf(NotificationItemUiModel("n1", "주문", "주문이 접수되었습니다", "주문하신 상품의 입금 정보를 확인해 주세요.", "오전 10:30", true))), NotificationGroupUiModel("이전", listOf(NotificationItemUiModel("n2", "공지", "새로운 공지사항", "쇼핑몰 이용 안내가 업데이트되었습니다.", "어제")))),
        unreadCount = 1,
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
        CustomerSupportUiModel("1588-0000", "평일 09:00–18:00 (점심 12:00–13:00)", "", listOf("전체", "주문/결제", "배송", "취소/반품"), "전체", listOf(CustomerSupportFaqUiModel("f1", "현장으로 배송이 가능한가요?", "배송 가능 여부는 지역과 상품에 따라 달라질 수 있습니다.")), setOf("f1")),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun CustomerSupportSearchResultsPreview() = EumShoppingMallTheme {
    CustomerSupportScreen(
        CustomerSupportUiModel("1588-0000", "평일 09:00–18:00", "배송", emptyList(), "전체", listOf(CustomerSupportFaqUiModel("f1", "주문 후 배송까지 얼마나 걸리나요?", "상품과 배송 지역에 따라 다릅니다."), CustomerSupportFaqUiModel("f2", "배송지를 변경할 수 있나요?", "상품 준비 전 고객센터로 문의해 주세요.")), emptySet()),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun CustomerSupportSearchEmptyPreview() = EumShoppingMallTheme {
    CustomerSupportScreen(CustomerSupportUiModel("1588-0000", "평일 09:00–18:00", "없는 검색어", emptyList(), "전체", emptyList(), emptySet()), onEvent = {})
}

private val previewTermsSections = (1..24).map { index ->
    PolicyDocumentSectionUiModel("제${index}조 (${if (index == 1) "목적" else "서비스 이용"})", listOf("서비스 이용과 관련한 제${index}조의 안내 문구입니다.", "이용자는 내용을 충분히 확인한 뒤 서비스를 이용할 수 있습니다."))
}
private val previewPrivacySections = (1..12).map { index ->
    PolicyDocumentSectionUiModel("$index. 개인정보 처리 안내", listOf("개인정보 처리와 관련한 안내 문구입니다.", "세부 내용은 최신 정책을 확인해 주세요."))
}
private val previewGuideSections = (1..6).map { index ->
    PolicyDocumentSectionUiModel("$index. 이용 절차", listOf("상품 탐색부터 배송 확인까지 이용 방법을 안내합니다."))
}

@ResponsiveUiPreview
@Composable
private fun PolicyDocumentPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(PolicyDocumentUiModel("개인정보 처리방침", "2026.11.01", true, previewPrivacySections, previousVersionLabel = "0000.00.00 시행", versionOptions = listOf("0000.00.00 시행 예정", "0000.00.00 시행 (현재)", "0000.00.00 시행")), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun PreviousTermsPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(PolicyDocumentUiModel("이용약관", "0000.00.00", true, previewTermsSections, previousVersionLabel = "0000.00.00 시행", noticeLabel = "이전 약관입니다. 현재 약관은 0000.00.00부터 시행 중입니다."), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun UpcomingTermsPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(PolicyDocumentUiModel("이용약관", "0000.00.00", true, previewTermsSections, previousVersionLabel = "이전 약관 보기", noticeLabel = "0000.00.00부터 개정 약관이 시행됩니다. 개정안을 확인해 주세요."), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun TermsVersionPickerPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(PolicyDocumentUiModel("이용약관", "0000.00.00", true, previewTermsSections, previousVersionLabel = "0000.00.00 시행", versionOptions = listOf("0000.00.00 시행 예정", "0000.00.00 시행 (현재)", "0000.00.00 시행", "0000.00.00 시행"), selectedVersionIndex = 1, isVersionPickerVisible = true), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun TermsDocumentPreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(PolicyDocumentUiModel("이용약관", "2026.11.01", true, previewTermsSections), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun PrivacyConsentDetailPreview() = EumShoppingMallTheme {
    PrivacyConsentDetailScreen(
        PrivacyConsentUiModel(
            effectiveDate = "2026.11.01",
            description = "서비스 이용을 위해 필요한 개인정보 수집 및 이용 내용을 안내합니다.",
            purpose = "회원 식별 및 문의 응대",
            collectedItems = "이름, 연락처, 이메일 주소",
            retentionPeriod = "목적 달성 후 관련 법령에 따라 보관 또는 파기합니다.",
        ),
        onEvent = {},
    )
}

@ResponsiveUiPreview
@Composable
private fun UsageGuidePreview() = EumShoppingMallTheme {
    PolicyDocumentScreen(PolicyDocumentUiModel("이용안내", "서비스 이용 안내", false, previewGuideSections), onEvent = {})
}

@ResponsiveUiPreview
@Composable
private fun CompanyIntroPreview() = EumShoppingMallTheme {
    CompanyIntroScreen(
        CompanyIntroUiModel(
            businessAreas = listOf(CompanyBusinessAreaUiModel("안전용품", "현장 필수 안전용품 공급", com.nativelap.eumshoppingmall.R.drawable.ic_package), CompanyBusinessAreaUiModel("현장 납품", "현장 상황에 맞춘 배송", com.nativelap.eumshoppingmall.R.drawable.ic_truck)),
            history = listOf(CompanyHistoryItemUiModel("2024", "이음산업건설 쇼핑몰 서비스 시작"), CompanyHistoryItemUiModel("2020", "산업 안전용품 공급 사업 확대")),
            certifications = listOf(CompanyCertificationUiModel("품질 인증", "인증 번호 및 상세 정보")),
            partners = listOf("파트너 A", "파트너 B", "파트너 C"),
            address = "서울특별시 00구 00로 00, 00빌딩",
            phone = "1588-0000",
            email = "info@example.com",
        ),
        onEvent = {},
    )
}
