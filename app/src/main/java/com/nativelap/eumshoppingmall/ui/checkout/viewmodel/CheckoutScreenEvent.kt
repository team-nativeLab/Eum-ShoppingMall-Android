package com.nativelap.eumshoppingmall.ui.checkout.viewmodel

sealed interface CheckoutScreenEvent {
    data object BackClicked : CheckoutScreenEvent
    data object AddressChangeClicked : CheckoutScreenEvent
    data object DeliveryRequestPickerToggled : CheckoutScreenEvent
    data class DeliveryRequestSelected(val request: String) : CheckoutScreenEvent
    data class ProductsExpandedChanged(val isExpanded: Boolean) : CheckoutScreenEvent
    data object NextClicked : CheckoutScreenEvent
    data object CouponClicked : CheckoutScreenEvent
    data object UseAllPointsClicked : CheckoutScreenEvent
    data class PaymentMethodSelected(val method: CheckoutPaymentMethod) : CheckoutScreenEvent
    data class ReceiptTypeSelected(val type: CheckoutReceiptType) : CheckoutScreenEvent
    data class ReceiptModeSelected(val mode: CheckoutReceiptMode) : CheckoutScreenEvent
    data object ReceiptDetailsEditClicked : CheckoutScreenEvent
    data class ReceiptFieldChanged(val field: CheckoutReceiptField, val value: String) : CheckoutScreenEvent
    data class OrderAgreementChanged(val isAccepted: Boolean) : CheckoutScreenEvent
    data object AgreementDetailsClicked : CheckoutScreenEvent
    data object PaymentClicked : CheckoutScreenEvent
}
