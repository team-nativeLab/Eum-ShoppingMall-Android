package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestField
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel

/**
 * 견적 문의(A-15). 서버 전송 없이 완료 화면으로 넘어간다.
 *
 * "상품 추가"로 상품 선택 화면에 다녀오는 동안 이 화면은 composition에서 빠진다. 그래서 입력값은 rememberSaveable에
 * 저장 가능한 값(문자열·enum·Boolean·ID 목록)으로만 들고, 상품 UiModel은 [productById]로 매번 ID에서 다시 만든다.
 * 선택 화면이 돌려준 상품 ID는 [pickedProductId]로 들어오며, 목록에 반영한 뒤 [onPickedProductConsumed]를 호출한다.
 */
@Composable
fun QuoteRequestRoute(
    uiModel: QuoteRequestUiModel,
    pickedProductId: String?,
    productById: (productId: String) -> CatalogProductUiModel,
    onBackClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onPickedProductConsumed: () -> Unit,
    onSubmit: () -> Unit,
) {
    var productIds by rememberSaveable { mutableStateOf(uiModel.products.map { product -> product.id }) }
    var requestType by rememberSaveable { mutableStateOf(uiModel.requestType) }
    var companyName by rememberSaveable { mutableStateOf(uiModel.companyName) }
    var contactName by rememberSaveable { mutableStateOf(uiModel.contactName) }
    var phoneNumber by rememberSaveable { mutableStateOf(uiModel.phoneNumber) }
    var email by rememberSaveable { mutableStateOf(uiModel.email) }
    var preferredDeliveryDate by rememberSaveable { mutableStateOf(uiModel.preferredDeliveryDate) }
    var deliveryAddress by rememberSaveable { mutableStateOf(uiModel.deliveryAddress) }
    var requestDetails by rememberSaveable { mutableStateOf(uiModel.requestDetails) }
    var removedAttachmentIds by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var isPrivacyConsentChecked by rememberSaveable { mutableStateOf(uiModel.isPrivacyConsentChecked) }

    LaunchedEffect(pickedProductId) {
        if (pickedProductId != null) {
            productIds = productIds.withAddedQuoteProduct(pickedProductId)
            onPickedProductConsumed()
        }
    }

    val products = remember(productIds, productById) { productIds.map(productById) }
    val quoteUiModel = uiModel.copy(
        products = products,
        requestType = requestType,
        companyName = companyName,
        contactName = contactName,
        phoneNumber = phoneNumber,
        email = email,
        preferredDeliveryDate = preferredDeliveryDate,
        deliveryAddress = deliveryAddress,
        requestDetails = requestDetails,
        attachments = uiModel.attachments.filterNot { attachment -> attachment.id in removedAttachmentIds },
        isPrivacyConsentChecked = isPrivacyConsentChecked,
    )
    QuoteRequestScreen(
        uiModel = quoteUiModel.copy(isSubmitEnabled = quoteUiModel.hasRequiredInput()),
        onEvent = { event ->
            when (event) {
                QuoteRequestEvent.BackClicked -> onBackClick()

                is QuoteRequestEvent.RequestTypeSelected -> requestType = event.type

                QuoteRequestEvent.AddProductClicked -> onAddProductClick()

                is QuoteRequestEvent.ProductRemoved -> {
                    productIds = productIds.filterNot { productId -> productId == event.productId }
                }

                is QuoteRequestEvent.FieldChanged -> when (event.field) {
                    QuoteRequestField.Company -> companyName = event.value
                    QuoteRequestField.ContactName -> contactName = event.value
                    QuoteRequestField.Phone -> phoneNumber = event.value
                    QuoteRequestField.Email -> email = event.value
                    QuoteRequestField.DeliveryDate -> preferredDeliveryDate = event.value
                    QuoteRequestField.DeliveryAddress -> deliveryAddress = event.value
                    QuoteRequestField.Details -> requestDetails = event.value
                }

                is QuoteRequestEvent.AttachmentRemoved -> {
                    removedAttachmentIds = removedAttachmentIds + event.attachmentId
                }

                is QuoteRequestEvent.PrivacyConsentChanged -> isPrivacyConsentChecked = event.checked

                QuoteRequestEvent.SubmitClicked -> onSubmit()

                // 날짜 선택·첨부·약관 보기는 시스템 picker나 별도 화면이 필요해 후속 작업에서 연결한다.
                QuoteRequestEvent.DatePickerClicked,
                QuoteRequestEvent.AddAttachmentClicked,
                QuoteRequestEvent.PrivacyDetailsClicked,
                -> Unit
            }
        },
    )
}

/** 이미 담긴 상품이면 그대로 두고, 아니면 맨 뒤에 더한다. */
internal fun List<String>.withAddedQuoteProduct(productId: String): List<String> = if (productId in this) {
    this
} else {
    this + productId
}

// 서버 검증이 생기기 전까지 연락처 4개 항목과 개인정보 동의가 있으면 제출할 수 있게 둔다.
private fun QuoteRequestUiModel.hasRequiredInput(): Boolean = companyName.isNotBlank() &&
    contactName.isNotBlank() &&
    phoneNumber.isNotBlank() &&
    email.isNotBlank() &&
    isPrivacyConsentChecked
