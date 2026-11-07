package com.nativelap.eumshoppingmall.ui.checkout

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutAddressScreen
import com.nativelap.eumshoppingmall.ui.checkout.view.CheckoutPaymentScreen
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentMethod
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutProductUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptMode
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewCheckoutModel = CheckoutUiModel(
    address = CheckoutAddressUiModel("홍길동", "010-0000-0000", "서울특별시 00구 00로 00\n00빌딩 000호", true),
    deliveryRequestOptions = listOf("문 앞에 놓아 주세요", "배송 전 연락 바랍니다", "경비실에 맡겨 주세요"),
    deliveryMethodLabel = "일반 배송 (2~3일)",
    products = listOf(
        CheckoutProductUiModel("helmet", "세이프가드 ABS 안전모", "옵션: 사이즈 L · 1개", "₩18,000"),
        CheckoutProductUiModel("shoes", "경량 안전화 4인치", "옵션: 사이즈 L · 1개", "₩54,000"),
    ),
    couponCount = 2,
    pointsAvailableLabel = "1,000원",
    paymentMethod = CheckoutPaymentMethod.CreditCard,
    paymentSummary = CheckoutPaymentSummaryUiModel("₩72,000", "₩0", "-₩0", "₩72,000"),
)

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "주문 배송지")
@Composable
private fun CheckoutAddressPreview() {
    EumShoppingMallTheme { CheckoutAddressScreen(previewCheckoutModel, onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "주문 결제 기본")
@Composable
private fun CheckoutPaymentPreview() {
    EumShoppingMallTheme { CheckoutPaymentScreen(previewCheckoutModel, onEvent = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 852, name = "세금계산서 직접 입력")
@Composable
private fun CheckoutTaxInvoicePreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(
            previewCheckoutModel.copy(
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

@Preview(showBackground = true, widthDp = 320, heightDp = 720, fontScale = 1.2f, name = "현금영수증 입력 좁은 화면")
@Composable
private fun CheckoutCashReceiptNarrowPreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(
            previewCheckoutModel.copy(
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
