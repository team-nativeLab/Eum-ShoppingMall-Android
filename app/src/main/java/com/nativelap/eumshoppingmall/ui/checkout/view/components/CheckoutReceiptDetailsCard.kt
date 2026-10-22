package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptField
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptMode
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutReceiptDetailsCard(
    details: CheckoutReceiptDetailsUiModel,
    onEvent: (CheckoutScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(EumRadius.x12),
        color = MaterialTheme.eumColors.surfaceSecondary,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.line),
    ) {
        Column(
            Modifier.padding(EumSpacing.x16),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            when (details.type) {
                CheckoutReceiptDetailsType.None -> Unit

                CheckoutReceiptDetailsType.SavedBusinessInformation -> {
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            stringResource(R.string.business_invoice_information),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelMedium,
                        )
                        TextButton(onClick = { onEvent(CheckoutScreenEvent.ReceiptDetailsEditClicked) }) {
                            Text(stringResource(R.string.common_edit))
                        }
                    }
                    DetailRow(stringResource(R.string.business_registration_number), details.registrationNumber)
                    DetailRow(stringResource(R.string.business_name), details.businessName)
                    DetailRow(stringResource(R.string.business_representative), details.representativeName)
                    DetailRow(stringResource(R.string.business_receipt_email), details.email)
                }

                CheckoutReceiptDetailsType.ManualBusinessInformation -> {
                    Text(stringResource(R.string.business_invoice_information), style = MaterialTheme.typography.labelMedium)
                    ReceiptField(
                        label = requiredLabel(R.string.business_registration_number),
                        value = details.registrationNumber,
                        placeholder = stringResource(R.string.checkout_registration_number_hint),
                        keyboardType = KeyboardType.Number,
                        onValueChanged = { onEvent(CheckoutScreenEvent.ReceiptFieldChanged(CheckoutReceiptField.RegistrationNumber, it)) },
                    )
                    ReceiptField(
                        label = requiredLabel(R.string.business_name),
                        value = details.businessName,
                        placeholder = stringResource(R.string.checkout_business_name_hint),
                        onValueChanged = { onEvent(CheckoutScreenEvent.ReceiptFieldChanged(CheckoutReceiptField.BusinessName, it)) },
                    )
                    ReceiptField(
                        label = requiredLabel(R.string.business_representative),
                        value = details.representativeName,
                        placeholder = stringResource(R.string.checkout_representative_hint),
                        onValueChanged = { onEvent(CheckoutScreenEvent.ReceiptFieldChanged(CheckoutReceiptField.RepresentativeName, it)) },
                    )
                    ReceiptField(
                        label = requiredLabel(R.string.business_receipt_email),
                        value = details.email,
                        placeholder = stringResource(R.string.checkout_email_hint),
                        keyboardType = KeyboardType.Email,
                        onValueChanged = { onEvent(CheckoutScreenEvent.ReceiptFieldChanged(CheckoutReceiptField.Email, it)) },
                    )
                }

                CheckoutReceiptDetailsType.CashReceiptPhone -> {
                    Text(
                        stringResource(if (details.receiptMode == CheckoutReceiptMode.IncomeDeduction) R.string.checkout_cash_receipt_income else R.string.checkout_cash_receipt_expense),
                        style = MaterialTheme.typography.labelMedium,
                    )
                    ReceiptField(
                        label = requiredLabel(R.string.checkout_phone_number),
                        value = details.phoneNumber,
                        placeholder = stringResource(R.string.checkout_phone_hint),
                        keyboardType = KeyboardType.Phone,
                        onValueChanged = { onEvent(CheckoutScreenEvent.ReceiptFieldChanged(CheckoutReceiptField.PhoneNumber, it)) },
                    )
                    Text(
                        stringResource(R.string.checkout_cash_receipt_after_deposit),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun requiredLabel(labelResId: Int): String = stringResource(R.string.checkout_required_field_label, stringResource(labelResId))

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
    }
}

@Composable
private fun ReceiptField(
    label: String,
    value: String,
    placeholder: String,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textPrimary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, style = MaterialTheme.typography.bodySmall) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = MaterialTheme.typography.bodySmall,
        )
    }
}
