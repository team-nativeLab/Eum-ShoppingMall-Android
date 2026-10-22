package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditTextField
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

data class DeliveryRequestUiModel(
    val selectedRequest: String?,
    val customRequest: String,
)

sealed interface DeliveryRequestScreenEvent {
    data object BackClicked : DeliveryRequestScreenEvent
    data class RequestSelected(val request: String) : DeliveryRequestScreenEvent
    data class CustomRequestChanged(val request: String) : DeliveryRequestScreenEvent
    data object SelectionConfirmed : DeliveryRequestScreenEvent
}

@Composable
fun DeliveryRequestScreen(
    uiModel: DeliveryRequestUiModel,
    onEvent: (DeliveryRequestScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val requestOptions = listOf(
        stringResource(R.string.ui_audit_request_door),
        stringResource(R.string.ui_audit_request_before_contact),
        stringResource(R.string.ui_audit_request_guard),
    )
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_shipping_request_title),
        onBackClick = { onEvent(DeliveryRequestScreenEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_request_complete),
        onBottomActionClick = { onEvent(DeliveryRequestScreenEvent.SelectionConfirmed) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_shipping_request_helper),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            requestOptions.forEach { request ->
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(
                        text = stringResource(R.string.ui_audit_shipping_request_item),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
                    UiAuditTextField(
                        label = stringResource(R.string.ui_audit_shipping_request_item),
                        value = request,
                        placeholder = "",
                        onValueChange = {},
                        readOnly = true,
                        isSelected = uiModel.selectedRequest == request,
                        onClick = { onEvent(DeliveryRequestScreenEvent.RequestSelected(request)) },
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(
                    text = stringResource(R.string.ui_audit_request_custom_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                UiAuditTextField(
                    label = stringResource(R.string.ui_audit_request_custom_label),
                    value = uiModel.customRequest,
                    placeholder = stringResource(R.string.ui_audit_request_custom_placeholder),
                    onValueChange = { onEvent(DeliveryRequestScreenEvent.CustomRequestChanged(it)) },
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun DeliveryRequestScreenPreview() {
    EumShoppingMallTheme {
        DeliveryRequestScreen(DeliveryRequestUiModel(null, ""), onEvent = {})
    }
}
