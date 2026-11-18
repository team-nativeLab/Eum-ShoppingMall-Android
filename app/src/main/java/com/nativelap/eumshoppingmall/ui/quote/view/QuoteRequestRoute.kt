package com.nativelap.eumshoppingmall.ui.quote.view

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.CatalogProductUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestEvent
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestField
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.QuoteRequestUiModel
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.fieldErrors
import com.nativelap.eumshoppingmall.ui.quote.viewmodel.isSubmittable
import java.time.LocalDate
import java.time.ZoneOffset

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
    onSubmit: (QuoteRequestUiModel) -> Unit,
    onUnavailableAction: () -> Unit,
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
    var isDatePickerVisible by remember { mutableStateOf(false) }

    LaunchedEffect(pickedProductId) {
        if (pickedProductId != null) {
            productIds = productIds.withAddedQuoteProduct(pickedProductId)
            onPickedProductConsumed()
        }
    }

    val products = remember(productIds, productById) { productIds.map(productById) }
    val today = LocalDate.now()
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
        uiModel = quoteUiModel.copy(
            errors = quoteUiModel.fieldErrors(today),
            isSubmitEnabled = quoteUiModel.isSubmittable(today),
        ),
        onEvent = { event ->
            when (event) {
                QuoteRequestEvent.BackClicked -> onBackClick()

                is QuoteRequestEvent.RequestTypeSelected -> requestType = event.type

                QuoteRequestEvent.AddProductClicked -> onAddProductClick()

                is QuoteRequestEvent.ProductRemoved -> {
                    productIds = productIds.filterNot { productId -> productId == event.productId }
                }

                is QuoteRequestEvent.FieldChanged -> when (event.field) {
                    QuoteRequestField.Products -> Unit
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

                QuoteRequestEvent.SubmitClicked -> if (quoteUiModel.isSubmittable(today)) onSubmit(quoteUiModel)

                QuoteRequestEvent.DatePickerClicked -> isDatePickerVisible = true

                QuoteRequestEvent.AddAttachmentClicked,
                QuoteRequestEvent.PrivacyDetailsClicked,
                -> onUnavailableAction()
            }
        },
    )
    if (isDatePickerVisible) {
        val initialMillis = preferredDeliveryDate.toDatePickerUtcMillis()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
        DatePickerDialog(
            onDismissRequest = { isDatePickerVisible = false },
            confirmButton = {
                TextButton(onClick = {
                    preferredDeliveryDate = datePickerResult(preferredDeliveryDate, datePickerState.selectedDateMillis, confirmed = true)
                    isDatePickerVisible = false
                }) { androidx.compose.material3.Text(stringResource(R.string.common_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { isDatePickerVisible = false }) { androidx.compose.material3.Text(stringResource(R.string.common_cancel)) }
            },
        ) { DatePicker(state = datePickerState) }
    }
}

internal fun String.toDatePickerUtcMillis(): Long? = runCatching {
    LocalDate.parse(this).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}.getOrNull()

internal fun datePickerUtcMillisToIsoDate(millis: Long): String = java.time.Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().toString()

internal fun datePickerResult(currentValue: String, selectedMillis: Long?, confirmed: Boolean): String = if (confirmed && selectedMillis != null) datePickerUtcMillisToIsoDate(selectedMillis) else currentValue

/** 이미 담긴 상품이면 그대로 두고, 아니면 맨 뒤에 더한다. */
internal fun List<String>.withAddedQuoteProduct(productId: String): List<String> = if (productId in this) {
    this
} else {
    this + productId
}
