package com.nativelap.eumshoppingmall.ui.quote.viewmodel

import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel

enum class QuoteRequestType {
    CustomManufacturing,
    BulkPurchase,
    RegularDelivery,
    Other,
}

enum class QuoteRequestField {
    Company,
    ContactName,
    Phone,
    Email,
    DeliveryDate,
    DeliveryAddress,
    Details,
}

data class QuoteAttachmentUiModel(
    val id: String,
    val fileName: String,
    val formatLabel: String,
    val sizeLabel: String,
)

data class QuoteRequestUiModel(
    val products: List<CatalogProductUiModel> = emptyList(),
    val requestType: QuoteRequestType = QuoteRequestType.CustomManufacturing,
    val companyName: String = "",
    val contactName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val preferredDeliveryDate: String = "",
    val deliveryAddress: String = "",
    val requestDetails: String = "",
    val attachments: List<QuoteAttachmentUiModel> = emptyList(),
    val isPrivacyConsentChecked: Boolean = false,
    val errors: Set<QuoteRequestField> = emptySet(),
    val isSubmitEnabled: Boolean = false,
)

sealed interface QuoteRequestEvent {
    data object BackClicked : QuoteRequestEvent
    data class RequestTypeSelected(val type: QuoteRequestType) : QuoteRequestEvent
    data object AddProductClicked : QuoteRequestEvent
    data class ProductRemoved(val productId: String) : QuoteRequestEvent
    data class FieldChanged(val field: QuoteRequestField, val value: String) : QuoteRequestEvent
    data object DatePickerClicked : QuoteRequestEvent
    data object AddAttachmentClicked : QuoteRequestEvent
    data class AttachmentRemoved(val attachmentId: String) : QuoteRequestEvent
    data class PrivacyConsentChanged(val checked: Boolean) : QuoteRequestEvent
    data object PrivacyDetailsClicked : QuoteRequestEvent
    data object SubmitClicked : QuoteRequestEvent
}
