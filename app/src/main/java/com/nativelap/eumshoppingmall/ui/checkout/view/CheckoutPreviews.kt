package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutAddressUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentMethod
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentSummaryUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutProductUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme

private val previewCheckoutUiModel = CheckoutUiModel(
    address = CheckoutAddressUiModel(
        recipientName = "홍길동",
        contactLabel = "010-0000-0000",
        addressLabel = "서울특별시 중구 세종대로 110 3층",
        isDefaultAddress = true,
    ),
    deliveryRequestOptions = listOf("문 앞에 놓아 주세요", "경비실에 맡겨 주세요"),
    deliveryMethodLabel = "택배 배송",
    products = listOf(
        CheckoutProductUiModel("p1", "안전화 방검형", "270 / 1개", "89,000원"),
        CheckoutProductUiModel("p2", "안전모 ABS", "화이트 / 2개", "24,000원"),
    ),
    couponCount = 2,
    pointsAvailableLabel = "0원",
    paymentMethod = CheckoutPaymentMethod.CreditCard,
    receiptType = CheckoutReceiptType.None,
    paymentSummary = CheckoutPaymentSummaryUiModel(
        productAmountLabel = "113,000원",
        shippingFeeLabel = "무료",
        discountLabel = "0원",
        finalAmountLabel = "113,000원",
        shippingFeeWon = 0L,
    ),
)

@ResponsiveUiPreview
@Composable
private fun CheckoutAddressScreenPreview() {
    EumShoppingMallTheme {
        CheckoutAddressScreen(previewCheckoutUiModel, onEvent = {})
    }
}

@ResponsiveUiPreview
@Composable
private fun CheckoutPaymentScreenPreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(previewCheckoutUiModel.copy(isOrderAgreementAccepted = false), onEvent = {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "결제 · 약관 동의 완료")
@Composable
private fun CheckoutPaymentAcceptedPreview() {
    EumShoppingMallTheme {
        CheckoutPaymentScreen(previewCheckoutUiModel.copy(isOrderAgreementAccepted = true), onEvent = {})
    }
}
