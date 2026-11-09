package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
        color = MaterialTheme.eumColors.surface,
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
                            stringResource(R.string.checkout_business_info_label),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelMedium,
                        )
                        TextButton(
                            onClick = { onEvent(CheckoutScreenEvent.ReceiptDetailsEditClicked) },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
                        ) {
                            Text(stringResource(R.string.checkout_receipt_change))
                        }
                    }
                    DetailRow(stringResource(R.string.business_registration_number), details.registrationNumber)
                    DetailRow(stringResource(R.string.business_name), details.businessName)
                    DetailRow(stringResource(R.string.business_representative), details.representativeName)
                    DetailRow(stringResource(R.string.business_receipt_email), details.email)
                    Text(
                        stringResource(R.string.checkout_business_saved_notice),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                }

                CheckoutReceiptDetailsType.ManualBusinessInformation -> {
                    Text(
                        stringResource(R.string.business_invoice_information),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.eumColors.textPrimary,
                    )
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
                    Text(
                        stringResource(R.string.checkout_invoice_after_payment),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }

                CheckoutReceiptDetailsType.CashReceiptPhone -> {
                    CashReceiptModeSwitch(
                        selectedMode = details.receiptMode,
                        onModeSelected = { onEvent(CheckoutScreenEvent.ReceiptModeSelected(it)) },
                        // Figma A-09c: 분할 선택과 입력 필드 사이는 상위 간격(8dp)에 8dp를 더해 약 16dp로 맞춘다.
                        modifier = Modifier.padding(bottom = EumSpacing.x8),
                    )
                    ReceiptField(
                        label = requiredLabel(R.string.checkout_phone_number),
                        value = details.phoneNumber,
                        placeholder = stringResource(R.string.checkout_phone_hint),
                        keyboardType = KeyboardType.Phone,
                        onValueChanged = { onEvent(CheckoutScreenEvent.ReceiptFieldChanged(CheckoutReceiptField.PhoneNumber, it)) },
                    )
                    Text(
                        stringResource(R.string.checkout_cash_receipt_issued_after_deposit),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )
                }
            }
        }
    }
}

// 소득공제용과 지출증빙용을 한 줄의 분할 선택으로 보여 준다(Figma A-09c).
@Composable
private fun CashReceiptModeSwitch(
    selectedMode: CheckoutReceiptMode,
    onModeSelected: (CheckoutReceiptMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val switchShape = RoundedCornerShape(EumRadius.x8)
    val switchModes = listOf(
        CheckoutReceiptMode.IncomeDeduction to R.string.checkout_cash_receipt_income,
        CheckoutReceiptMode.ExpenseProof to R.string.checkout_cash_receipt_expense,
    )
    // 시각 테두리는 44dp로 두고, 터치 영역은 48dp 이상인 바깥 행이 맡는다(최소 터치 크기 충족).
    Box(modifier.fillMaxWidth().heightIn(min = 48.dp)) {
        Row(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(44.dp)
                .clip(switchShape)
                .border(1.dp, MaterialTheme.eumColors.textPrimary, switchShape),
        ) {
            switchModes.forEach { (mode, _) ->
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (mode == selectedMode) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface),
                )
            }
        }
        Row(Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            switchModes.forEach { (mode, labelResId) ->
                val isSelected = mode == selectedMode
                Box(
                    Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .selectable(selected = isSelected, role = Role.RadioButton) { onModeSelected(mode) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        stringResource(labelResId),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textPrimary,
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
