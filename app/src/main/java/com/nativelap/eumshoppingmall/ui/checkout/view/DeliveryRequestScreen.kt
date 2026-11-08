package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditTextField
import com.nativelap.eumshoppingmall.ui.preview.ResponsiveUiPreview
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

enum class DeliveryRequestOption {
    Door,
    SecurityDesk,
    ParcelLocker,
    Custom,
}

data class DeliveryRequestUiModel(
    val selectedRequest: DeliveryRequestOption?,
    val customRequest: String,
)

sealed interface DeliveryRequestScreenEvent {
    data object BackClicked : DeliveryRequestScreenEvent
    data class RequestSelected(val request: DeliveryRequestOption) : DeliveryRequestScreenEvent
    data class CustomRequestChanged(val request: String) : DeliveryRequestScreenEvent
    data class SelectionConfirmed(
        val selectedRequest: DeliveryRequestOption?,
        val customRequest: String,
    ) : DeliveryRequestScreenEvent
}

@Composable
fun DeliveryRequestScreen(
    uiModel: DeliveryRequestUiModel,
    onEvent: (DeliveryRequestScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val requestOptions = listOf(
        DeliveryRequestOption.Door to stringResource(R.string.ui_audit_request_door),
        DeliveryRequestOption.SecurityDesk to stringResource(R.string.ui_audit_request_guard),
        DeliveryRequestOption.ParcelLocker to stringResource(R.string.ui_audit_request_locker),
        DeliveryRequestOption.Custom to stringResource(R.string.ui_audit_request_custom_label),
    )
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_shipping_request_title),
        onBackClick = { onEvent(DeliveryRequestScreenEvent.BackClicked) },
        modifier = modifier,
        showBottomDivider = true,
        bottomActionLabel = stringResource(R.string.ui_audit_request_complete),
        onBottomActionClick = {
            onEvent(DeliveryRequestScreenEvent.SelectionConfirmed(uiModel.selectedRequest, uiModel.customRequest))
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(top = EumSpacing.x16, bottom = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_shipping_request_helper),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textSecondary,
            )
            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(EumRadius.x8),
                color = MaterialTheme.eumColors.surface,
                border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineNeutral),
            ) {
                Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x16)) {
                    requestOptions.forEachIndexed { index, (option, label) ->
                        val isSelected = uiModel.selectedRequest == option
                        Row(
                            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                                .selectable(
                                    selected = isSelected,
                                    role = Role.RadioButton,
                                    onClick = { onEvent(DeliveryRequestScreenEvent.RequestSelected(option)) },
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                        ) {
                            Text(
                                text = label,
                                modifier = Modifier.weight(1f).padding(vertical = EumSpacing.x12),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.eumColors.textPrimary,
                            )
                            RadioButton(selected = isSelected, onClick = null)
                        }
                        if (index != requestOptions.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.eumColors.line)
                        }
                    }
                }
            }
            if (uiModel.selectedRequest == DeliveryRequestOption.Custom) {
                Text(
                    text = stringResource(R.string.ui_audit_request_custom_detail_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                UiAuditTextField(
                    label = stringResource(R.string.ui_audit_request_custom_detail_label),
                    value = uiModel.customRequest,
                    placeholder = stringResource(R.string.ui_audit_request_custom_placeholder),
                    onValueChange = { request ->
                        onEvent(DeliveryRequestScreenEvent.CustomRequestChanged(request.take(MAX_REQUEST_LENGTH)))
                    },
                    minHeight = 120.dp,
                    maxLines = 4,
                )
                Text(
                    text = stringResource(R.string.ui_audit_request_character_count, uiModel.customRequest.length),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
            }
        }
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun DeliveryRequestScreenPreview() {
    EumShoppingMallTheme {
        DeliveryRequestScreen(
            DeliveryRequestUiModel(DeliveryRequestOption.Door, ""),
            onEvent = {},
        )
    }
}

private const val MAX_REQUEST_LENGTH = 100
