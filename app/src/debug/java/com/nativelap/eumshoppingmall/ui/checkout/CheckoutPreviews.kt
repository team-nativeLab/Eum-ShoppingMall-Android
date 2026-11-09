package com.nativelap.eumshoppingmall.ui.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutAddressScreen
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutPaymentScreen
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutReceiptDetailsCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutReceiptTypeSelector
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutSectionHeader
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentMethod
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutProductUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptMode
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing

// Figma 샘플 기준: 쿠폰 0장, 적립금 0원(빈 값이면 화면이 0원을 표시), 결제수단은 프레임별로 지정한다.
private val previewCheckoutModel = CheckoutUiModel(
    address = CheckoutAddressUiModel("홍길동", "010-0000-0000", "서울특별시 00구 00로 00\n00빌딩 000호", true),
    deliveryRequestOptions = listOf("문 앞에 놓아 주세요", "배송 전 연락 바랍니다", "경비실에 맡겨 주세요"),
    deliveryMethodLabel = "일반 배송 (2~3일)",
    products = listOf(
        CheckoutProductUiModel("helmet", "세이프가드 ABS 안전모", "옵션: 사이즈 L · 1개", "₩18,000"),
        CheckoutProductUiModel("shoes", "경량 안전화 4인치", "옵션: 사이즈 L · 1개", "₩54,000"),
    ),
    couponCount = 0,
    pointsAvailableLabel = "",
    paymentMethod = CheckoutPaymentMethod.CreditCard,
    paymentSummary = CheckoutPaymentSummaryUiModel("₩72,000", "₩0", "-₩0", "₩72,000"),
)

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "주문 배송지")
@Composable
private fun CheckoutAddressPreview() {
    EumShoppingMallTheme { CheckoutAddressScreen(previewCheckoutModel, onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "주문 결제 기본")
@Composable
private fun CheckoutPaymentPreview() {
    EumShoppingMallTheme { CheckoutPaymentScreen(previewCheckoutModel, onEvent = {}) }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "세금계산서 직접 입력")
@Composable
private fun CheckoutTaxInvoicePreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(
            previewCheckoutModel.copy(
                paymentMethod = CheckoutPaymentMethod.BankTransfer,
                receiptType = CheckoutReceiptType.TaxInvoice,
                receiptDetails = CheckoutReceiptDetailsUiModel(
                    type = CheckoutReceiptDetailsType.ManualBusinessInformation,
                    registrationNumber = "",
                    businessName = "",
                    representativeName = "",
                    email = "",
                ),
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 320, heightDp = 720, fontScale = 1.2f, name = "현금영수증 입력 좁은 화면")
@Composable
private fun CheckoutCashReceiptNarrowPreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(
            previewCheckoutModel.copy(
                paymentMethod = CheckoutPaymentMethod.BankTransfer,
                receiptDetails = CheckoutReceiptDetailsUiModel(
                    type = CheckoutReceiptDetailsType.CashReceiptPhone,
                    phoneNumber = "010-0000-0000",
                    receiptMode = CheckoutReceiptMode.ExpenseProof,
                ),
            ),
            onEvent = {},
        )
    }
}

// Figma A-09c-p '증빙 서류' 영역만 캡처해 Figma와 비교하기 위한 Preview(카드 위 헤더·라디오·입력 카드).
@Preview(showBackground = true, widthDp = 390, heightDp = 560, name = "증빙 서류 영역 A-09c-p")
@Composable
private fun CheckoutReceiptDocumentSectionPreview() {
    val receiptDetails = CheckoutReceiptDetailsUiModel(
        type = CheckoutReceiptDetailsType.CashReceiptPhone,
        phoneNumber = "010-0000-0000",
        receiptMode = CheckoutReceiptMode.ExpenseProof,
    )
    EumShoppingMallTheme {
        Column(Modifier.fillMaxWidth()) {
            CheckoutSectionHeader(R.string.checkout_receipt_document)
            CheckoutReceiptTypeSelector(
                selectedType = CheckoutReceiptType.CashReceipt,
                onTypeSelected = {},
                modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x4),
            )
            CheckoutReceiptDetailsCard(
                details = receiptDetails,
                onEvent = {},
                modifier = Modifier.padding(horizontal = EumSpacing.x20),
            )
        }
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "현금영수증 입력")
@Composable
private fun CheckoutCashReceiptReadyPreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(
            previewCheckoutModel.copy(
                paymentMethod = CheckoutPaymentMethod.BankTransfer,
                receiptType = CheckoutReceiptType.CashReceipt,
                receiptDetails = CheckoutReceiptDetailsUiModel(
                    type = CheckoutReceiptDetailsType.CashReceiptPhone,
                    phoneNumber = "010-0000-0000",
                    receiptMode = CheckoutReceiptMode.IncomeDeduction,
                ),
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "세금계산서 저장 사업자 정보")
@Composable
private fun CheckoutTaxInvoiceSavedPreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(
            previewCheckoutModel.copy(
                paymentMethod = CheckoutPaymentMethod.BankTransfer,
                receiptType = CheckoutReceiptType.TaxInvoice,
                receiptDetails = CheckoutReceiptDetailsUiModel(
                    type = CheckoutReceiptDetailsType.SavedBusinessInformation,
                    registrationNumber = "000-00-00000",
                    businessName = "회사명",
                    representativeName = "홍길동",
                    email = "example@email.com",
                ),
            ),
            onEvent = {},
        )
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "가상 계좌 결제 선택")
@Composable
private fun CheckoutVirtualAccountPreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(
            previewCheckoutModel.copy(paymentMethod = CheckoutPaymentMethod.VirtualAccount),
            onEvent = {},
        )
    }
}
