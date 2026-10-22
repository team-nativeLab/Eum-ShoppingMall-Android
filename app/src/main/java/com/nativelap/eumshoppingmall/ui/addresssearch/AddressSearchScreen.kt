package com.nativelap.eumshoppingmall.ui.addresssearch

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
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditScreenScaffold
import com.nativelap.eumshoppingmall.ui.audit.components.UiAuditTextField
import com.nativelap.eumshoppingmall.ui.theme.EumShoppingMallTheme
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

sealed interface AddressSearchScreenEvent {
    data object BackClicked : AddressSearchScreenEvent
    data class QueryChanged(val query: String) : AddressSearchScreenEvent
    data object AddressSelected : AddressSearchScreenEvent
}

@Composable
fun AddressSearchScreen(
    query: String,
    resultAddress: String,
    onEvent: (AddressSearchScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditScreenScaffold(
        title = stringResource(R.string.ui_audit_address_title),
        onBackClick = { onEvent(AddressSearchScreenEvent.BackClicked) },
        modifier = modifier,
        bottomActionLabel = stringResource(R.string.ui_audit_address_select),
        onBottomActionClick = { onEvent(AddressSearchScreenEvent.AddressSelected) },
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(contentPadding).padding(top = EumSpacing.x20),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x20),
        ) {
            Text(
                text = stringResource(R.string.ui_audit_address_helper),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.eumColors.textDisabled,
            )
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(
                    text = stringResource(R.string.ui_audit_address_search_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                UiAuditTextField(
                    label = stringResource(R.string.ui_audit_address_search_label),
                    value = query,
                    placeholder = stringResource(R.string.ui_audit_address_placeholder),
                    onValueChange = { onEvent(AddressSearchScreenEvent.QueryChanged(it)) },
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                Text(
                    text = stringResource(R.string.ui_audit_address_results),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.eumColors.textPrimary,
                )
                UiAuditTextField(
                    label = stringResource(R.string.ui_audit_address_results),
                    value = resultAddress,
                    placeholder = "",
                    onValueChange = {},
                    readOnly = true,
                    minHeight = EumSpacing.x52,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AddressSearchScreenPreview() {
    EumShoppingMallTheme {
        AddressSearchScreen(
            query = "",
            resultAddress = stringResource(R.string.ui_audit_address_example),
            onEvent = {},
        )
    }
}
