package com.nativelap.eumshoppingmall.ui.addresssearch

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

data class AddressSearchResultUiModel(
    val id: String,
    val roadAddress: String,
    val lotAddress: String,
)

data class AddressSearchUiModel(
    val query: String,
    val results: List<AddressSearchResultUiModel>,
    val selectedAddressId: String?,
    val hasSearched: Boolean,
)

sealed interface AddressSearchScreenEvent {
    data object BackClicked : AddressSearchScreenEvent
    data class QueryChanged(val query: String) : AddressSearchScreenEvent
    data class ResultSelected(val addressId: String) : AddressSearchScreenEvent
    data class AddressSelectionConfirmed(val addressId: String) : AddressSearchScreenEvent
}

@Composable
fun AddressSearchScreen(
    uiModel: AddressSearchUiModel,
    onEvent: (AddressSearchScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedAddressId = uiModel.selectedAddressId
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_address_title),
        onBackClick = { onEvent(AddressSearchScreenEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = null,
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(top = EumSpacing.x16, bottom = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
        ) {
            UiAuditTextField(
                label = stringResource(R.string.ui_audit_address_search_label),
                value = uiModel.query,
                placeholder = stringResource(R.string.ui_audit_address_placeholder),
                onValueChange = { onEvent(AddressSearchScreenEvent.QueryChanged(it)) },
            )
            Text(
                text = stringResource(R.string.ui_audit_address_helper),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textTertiary,
            )
            when {
                uiModel.results.isNotEmpty() -> AddressSearchResults(
                    results = uiModel.results,
                    selectedAddressId = selectedAddressId,
                    onAddressSelected = { addressId ->
                        onEvent(AddressSearchScreenEvent.ResultSelected(addressId))
                    },
                )

                uiModel.hasSearched -> Text(
                    text = stringResource(R.string.ui_audit_address_empty),
                    modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x32),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.eumColors.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun AddressSearchResults(
    results: List<AddressSearchResultUiModel>,
    selectedAddressId: String?,
    onAddressSelected: (String) -> Unit,
) {
    Surface(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(EumRadius.x8),
        color = MaterialTheme.eumColors.surface,
        border = BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineNeutral),
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x16)) {
            results.forEachIndexed { index, address ->
                val isSelected = address.id == selectedAddressId
                Row(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp)
                        .selectable(
                            selected = isSelected,
                            role = Role.RadioButton,
                            onClick = { onAddressSelected(address.id) },
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = EumSpacing.x12),
                        verticalArrangement = Arrangement.spacedBy(EumSpacing.x4),
                    ) {
                        Text(
                            text = address.roadAddress,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                        Text(
                            text = stringResource(R.string.ui_audit_address_lot_format, address.lotAddress),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.eumColors.textSecondary,
                        )
                    }
                }
                if (index != results.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
            }
        }
    }
}

@ResponsiveUiPreview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AddressSearchScreenPreview() {
    EumShoppingMallTheme {
        AddressSearchScreen(
            uiModel = AddressSearchUiModel(
                query = "",
                results = emptyList(),
                selectedAddressId = null,
                hasSearched = false,
            ),
            onEvent = {},
        )
    }
}
