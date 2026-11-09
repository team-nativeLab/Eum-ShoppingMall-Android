package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptField
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel

/** 주문하기 2 결제(A-09). 결제 수단·영수증·동의 입력은 이 화면에 머무는 동안만 유지한다. */
@Composable
fun CheckoutPaymentRoute(
    uiModel: CheckoutUiModel,
    onBackClick: () -> Unit,
    onPaymentClick: () -> Unit,
) {
    var checkoutUiModel by remember(uiModel) { mutableStateOf(uiModel) }
    CheckoutPaymentScreen(
        uiModel = checkoutUiModel,
        onEvent = { event ->
            when (event) {
                CheckoutScreenEvent.BackClicked -> onBackClick()

                CheckoutScreenEvent.PaymentClicked -> onPaymentClick()

                CheckoutScreenEvent.UseAllPointsClicked -> {
                    checkoutUiModel = checkoutUiModel.copy(isUsingAllPoints = !checkoutUiModel.isUsingAllPoints)
                }

                is CheckoutScreenEvent.PaymentMethodSelected -> {
                    checkoutUiModel = checkoutUiModel.copy(paymentMethod = event.method)
                }

                is CheckoutScreenEvent.ReceiptTypeSelected -> {
                    checkoutUiModel = checkoutUiModel.copy(receiptType = event.type)
                }

                is CheckoutScreenEvent.ReceiptModeSelected -> {
                    checkoutUiModel = checkoutUiModel.copy(
                        receiptDetails = checkoutUiModel.receiptDetails.copy(receiptMode = event.mode),
                    )
                }

                is CheckoutScreenEvent.ReceiptFieldChanged -> {
                    checkoutUiModel = checkoutUiModel.copy(
                        receiptDetails = checkoutUiModel.receiptDetails.withField(event.field, event.value),
                    )
                }

                is CheckoutScreenEvent.OrderAgreementChanged -> {
                    checkoutUiModel = checkoutUiModel.copy(isOrderAgreementAccepted = event.isAccepted)
                }

                is CheckoutScreenEvent.ProductsExpandedChanged -> {
                    checkoutUiModel = checkoutUiModel.copy(productsExpanded = event.isExpanded)
                }

                // 쿠폰 선택·영수증 정보 편집·약관 보기(A-39)는 이번 범위가 아니다. 나머지는 배송지 화면 이벤트다.
                else -> Unit
            }
        },
    )
}

private fun CheckoutReceiptDetailsUiModel.withField(
    field: CheckoutReceiptField,
    value: String,
): CheckoutReceiptDetailsUiModel = when (field) {
    CheckoutReceiptField.RegistrationNumber -> copy(registrationNumber = value)
    CheckoutReceiptField.BusinessName -> copy(businessName = value)
    CheckoutReceiptField.RepresentativeName -> copy(representativeName = value)
    CheckoutReceiptField.Email -> copy(email = value)
    CheckoutReceiptField.PhoneNumber -> copy(phoneNumber = value)
}
