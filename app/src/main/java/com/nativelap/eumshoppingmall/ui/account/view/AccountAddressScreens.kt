package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressFormScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressFormUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressListScreenEvent
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressListUiModel
import com.nativelap.eumshoppingmall.ui.account.viewmodel.AccountAddressUiModel
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun AccountAddressListScreen(uiModel: AccountAddressListUiModel, onEvent: (AccountAddressListScreenEvent) -> Unit) {
    val hasCapacity = uiModel.addresses.size < uiModel.maximumAddressCount
    Scaffold(
        topBar = { CatalogTopBar(stringResource(R.string.address_title), onBackClick = { onEvent(AccountAddressListScreenEvent.BackClicked) }, modifier = Modifier.statusBarsPadding()) },
        bottomBar = {
            if (uiModel.addresses.isNotEmpty()) {
                Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8)) {
                    Text(stringResource(R.string.address_limit_notice, uiModel.maximumAddressCount), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textTertiary)
                    AccountPrimaryButton(stringResource(R.string.address_add), { onEvent(AccountAddressListScreenEvent.AddClicked) }, enabled = hasCapacity)
                }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        if (uiModel.addresses.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = EumSpacing.x24), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.address_empty_title), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.address_empty_description), Modifier.padding(top = EumSpacing.x8), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                AccountPrimaryButton(stringResource(R.string.address_add), { onEvent(AccountAddressListScreenEvent.AddClicked) }, enabled = hasCapacity)
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = androidx.compose.foundation.layout.PaddingValues(EumSpacing.x20), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
                items(uiModel.addresses, key = { it.id }) { address -> AddressCard(address, onEvent) }
            }
        }
    }
}

@Composable
private fun AddressCard(address: AccountAddressUiModel, onEvent: (AccountAddressListScreenEvent) -> Unit) {
    Column(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.eumColors.line, RoundedCornerShape(EumRadius.x12)).padding(EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
            Text(address.label, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            if (address.isDefault) Text(stringResource(R.string.address_default), color = MaterialTheme.eumColors.inverse, modifier = Modifier.padding(EumSpacing.x4), style = MaterialTheme.typography.labelSmall)
        }
        Text(address.recipient + " · " + address.phoneNumber, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(address.postalCode + " " + address.address + " " + address.addressDetail, style = MaterialTheme.typography.bodySmall)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
            if (!address.isDefault) {
                Text(stringResource(R.string.address_set_default), Modifier.weight(1f).heightIn(min = 48.dp).clickable { onEvent(AccountAddressListScreenEvent.DefaultSelected(address.id)) }.padding(vertical = EumSpacing.x12), style = MaterialTheme.typography.labelMedium)
            } else {
                Spacer(Modifier.weight(1f))
            }
            androidx.compose.material3.OutlinedButton(onClick = { onEvent(AccountAddressListScreenEvent.EditClicked(address.id)) }, modifier = Modifier.heightIn(min = 48.dp)) { Text(stringResource(R.string.address_edit)) }
            if (!address.isDefault) androidx.compose.material3.OutlinedButton(onClick = { onEvent(AccountAddressListScreenEvent.DeleteClicked(address.id)) }, modifier = Modifier.heightIn(min = 48.dp)) { Text(stringResource(R.string.address_delete)) }
        }
    }
}

@Composable
fun AccountAddressFormScreen(uiModel: AccountAddressFormUiModel, onEvent: (AccountAddressFormScreenEvent) -> Unit) {
    val title = stringResource(if (uiModel.isEditMode) R.string.address_form_edit_title else R.string.address_form_add_title)
    val doorDeliveryRequest = stringResource(R.string.address_delivery_request_door)
    val noDeliveryRequest = stringResource(R.string.address_delivery_request_none)
    Scaffold(
        topBar = { CatalogTopBar(title, onBackClick = { onEvent(AccountAddressFormScreenEvent.BackClicked) }, modifier = Modifier.statusBarsPadding()) },
        bottomBar = {
            AccountStickyPrimaryAction(
                stringResource(R.string.common_save),
                { onEvent(AccountAddressFormScreenEvent.SaveClicked) },
                enabled = uiModel.isSaveEnabled,
            )
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).imePadding(), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = EumSpacing.x20, vertical = EumSpacing.x16), verticalArrangement = Arrangement.spacedBy(EumSpacing.x12)) {
            item { AddressInput(stringResource(R.string.delivery_address_name), uiModel.address.label, stringResource(R.string.address_label_placeholder), { onEvent(AccountAddressFormScreenEvent.LabelChanged(it)) }) }
            item { AddressInput(stringResource(R.string.address_recipient), uiModel.address.recipient, stringResource(R.string.address_recipient_placeholder), { onEvent(AccountAddressFormScreenEvent.RecipientChanged(it)) }) }
            item { AddressInput(stringResource(R.string.account_phone_number), uiModel.address.phoneNumber, stringResource(R.string.account_phone_placeholder), { onEvent(AccountAddressFormScreenEvent.PhoneChanged(it)) }, keyboardType = KeyboardType.Phone, isError = uiModel.isPhoneInvalid, errorMessage = if (uiModel.isPhoneInvalid) stringResource(R.string.address_phone_invalid) else null) }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(stringResource(R.string.address_placeholder), style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8), verticalAlignment = Alignment.CenterVertically) {
                        AddressInput(stringResource(R.string.address_postal_code), uiModel.address.postalCode, stringResource(R.string.address_postal_placeholder), { onEvent(AccountAddressFormScreenEvent.PostalCodeChanged(it)) }, modifier = Modifier.weight(1f))
                        androidx.compose.material3.OutlinedButton(onClick = { onEvent(AccountAddressFormScreenEvent.AddressSearchClicked) }, modifier = Modifier.heightIn(min = 48.dp)) { Text(stringResource(R.string.address_search)) }
                    }
                    AddressInput(stringResource(R.string.address_placeholder), uiModel.address.address, stringResource(R.string.address_placeholder), { onEvent(AccountAddressFormScreenEvent.AddressChanged(it)) }, enabled = false)
                    AddressInput(stringResource(R.string.address_detail), uiModel.address.addressDetail, stringResource(R.string.address_detail_placeholder), { onEvent(AccountAddressFormScreenEvent.AddressDetailChanged(it)) })
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    Text(stringResource(R.string.address_delivery_request), style = MaterialTheme.typography.labelMedium)
                    Text(
                        uiModel.address.deliveryRequest.ifBlank { stringResource(R.string.address_delivery_request_placeholder) },
                        Modifier.fillMaxWidth().heightIn(min = 48.dp).border(1.dp, MaterialTheme.eumColors.lineControl, RoundedCornerShape(EumRadius.x8)).clickable {
                            onEvent(AccountAddressFormScreenEvent.DeliveryRequestChanged(if (uiModel.address.deliveryRequest.isBlank()) doorDeliveryRequest else noDeliveryRequest))
                        }.padding(EumSpacing.x12),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable { onEvent(AccountAddressFormScreenEvent.DefaultChanged(!uiModel.address.isDefault)) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    androidx.compose.material3.Checkbox(
                        checked = uiModel.address.isDefault,
                        onCheckedChange = { onEvent(AccountAddressFormScreenEvent.DefaultChanged(it)) },
                    )
                    Text(stringResource(R.string.address_default_checkbox), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun AddressInput(label: String, value: String, placeholder: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, isError: Boolean = false, errorMessage: String? = null, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(EumSpacing.x6)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(value, onValueChange, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), placeholder = { Text(placeholder) }, keyboardOptions = KeyboardOptions(keyboardType = keyboardType), isError = isError, supportingText = errorMessage?.let { { Text(it) } }, enabled = enabled, singleLine = true)
    }
}
