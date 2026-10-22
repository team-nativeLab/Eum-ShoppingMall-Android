package com.nativelap.eumshoppingmall.ui.checkout.viewmodel

import androidx.annotation.DrawableRes

data class CheckoutProductUiModel(
    val id: String,
    val name: String,
    val optionAndQuantityLabel: String,
    val priceLabel: String,
    @param:DrawableRes val imageResId: Int? = null,
)

data class CheckoutAddressUiModel(
    val recipientName: String,
    val contactLabel: String,
    val addressLabel: String,
    val isDefaultAddress: Boolean = false,
)

data class CheckoutPaymentSummaryUiModel(
    val productAmountLabel: String,
    val shippingFeeLabel: String,
    val discountLabel: String,
    val finalAmountLabel: String,
)

enum class CheckoutPaymentMethod {
    CreditCard,
    BankTransfer,
    VirtualAccount,
    SimplePayA,
    SimplePayB,
    MobilePayment,
}

enum class CheckoutReceiptType {
    CashReceipt,
    TaxInvoice,
    None,
}

enum class CheckoutReceiptMode {
    IncomeDeduction,
    ExpenseProof,
}

enum class CheckoutReceiptField {
    RegistrationNumber,
    BusinessName,
    RepresentativeName,
    Email,
    PhoneNumber,
}

enum class CheckoutReceiptDetailsType {
    None,
    SavedBusinessInformation,
    ManualBusinessInformation,
    CashReceiptPhone,
}

data class CheckoutReceiptDetailsUiModel(
    val type: CheckoutReceiptDetailsType = CheckoutReceiptDetailsType.None,
    val registrationNumber: String = "",
    val businessName: String = "",
    val representativeName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val receiptMode: CheckoutReceiptMode = CheckoutReceiptMode.IncomeDeduction,
)

data class CheckoutUiModel(
    val address: CheckoutAddressUiModel,
    val deliveryRequestLabel: String? = null,
    val deliveryRequestOptions: List<String> = emptyList(),
    val isDeliveryRequestPickerVisible: Boolean = false,
    val deliveryMethodLabel: String,
    val products: List<CheckoutProductUiModel> = emptyList(),
    val productsExpanded: Boolean = true,
    val couponCount: Int = 0,
    val pointsAvailableLabel: String = "",
    val isUsingAllPoints: Boolean = false,
    val paymentMethod: CheckoutPaymentMethod? = null,
    val receiptType: CheckoutReceiptType = CheckoutReceiptType.CashReceipt,
    val receiptDetails: CheckoutReceiptDetailsUiModel = CheckoutReceiptDetailsUiModel(),
    val paymentSummary: CheckoutPaymentSummaryUiModel,
    val isOrderAgreementAccepted: Boolean = false,
)
