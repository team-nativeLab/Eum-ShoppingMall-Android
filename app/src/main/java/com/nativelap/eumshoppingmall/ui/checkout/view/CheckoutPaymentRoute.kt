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
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.canUseAllPoints
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.withReceiptDetailsTypeSelection
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.withReceiptTypeSelection

/** 주문하기 2 결제(A-09). 결제 수단·영수증·동의 입력은 이 화면에 머무는 동안만 유지한다. */
@Composable
fun CheckoutPaymentRoute(
    uiModel: CheckoutUiModel,
    onBackClick: () -> Unit,
    onPaymentClick: (CheckoutUiModel) -> Unit,
    onUnavailableAction: () -> Unit,
) {
    var checkoutUiModel by remember(uiModel) { mutableStateOf(uiModel) }
    CheckoutPaymentScreen(
        uiModel = checkoutUiModel,
        onEvent = { event ->
            when (event) {
                CheckoutScreenEvent.BackClicked -> onBackClick()

                CheckoutScreenEvent.PaymentClicked -> onPaymentClick(checkoutUiModel)

                CheckoutScreenEvent.UseAllPointsClicked -> {
                    // 비활성 상태(적립금 0원, 사용 중 아님)에서는 토글하지 않는다.
                    if (canUseAllPoints(checkoutUiModel.pointsAvailableWon, checkoutUiModel.isUsingAllPoints)) {
                        checkoutUiModel = checkoutUiModel.copy(isUsingAllPoints = !checkoutUiModel.isUsingAllPoints)
                    }
                }

                is CheckoutScreenEvent.PaymentMethodSelected -> {
                    checkoutUiModel = checkoutUiModel.copy(paymentMethod = event.method)
                }

                is CheckoutScreenEvent.ReceiptTypeSelected -> {
                    checkoutUiModel = checkoutUiModel.withReceiptTypeSelection(event.type)
                }

                is CheckoutScreenEvent.ReceiptDetailsTypeSelected -> {
                    checkoutUiModel = checkoutUiModel.withReceiptDetailsTypeSelection(event.type)
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

                else -> onUnavailableAction()
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
