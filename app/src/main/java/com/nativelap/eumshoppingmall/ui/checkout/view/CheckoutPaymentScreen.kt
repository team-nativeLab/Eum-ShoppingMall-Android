package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutPaymentMethodSelector
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutPriceSummaryCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutReceiptDetailsCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutSectionHeader
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutStepIndicator
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentMethod
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptMode
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutPaymentScreen(
    uiModel: CheckoutUiModel,
    onEvent: (CheckoutScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val receiptTypes = listOf(
        CheckoutReceiptType.CashReceipt to R.string.checkout_cash_receipt,
        CheckoutReceiptType.TaxInvoice to R.string.checkout_tax_invoice,
        CheckoutReceiptType.None to R.string.checkout_not_requested,
    )
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(Modifier.statusBarsPadding()) {
                CatalogTopBar(title = stringResource(R.string.checkout_title), onBackClick = { onEvent(CheckoutScreenEvent.BackClicked) })
                CheckoutStepIndicator(currentStep = 2, modifier = Modifier.padding(horizontal = EumSpacing.x40, vertical = EumSpacing.x12))
            }
        },
        bottomBar = {
            androidx.compose.material3.Button(
                onClick = { onEvent(CheckoutScreenEvent.PaymentClicked) },
                enabled = uiModel.paymentMethod != null && uiModel.isOrderAgreementAccepted && isReceiptDetailsValid(uiModel),
                modifier = Modifier.fillMaxWidth().imePadding().navigationBarsPadding()
                    .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12).heightIn(min = 52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.eumColors.textPrimary,
                    contentColor = MaterialTheme.eumColors.inverse,
                    disabledContainerColor = MaterialTheme.eumColors.surfaceSecondary,
                    disabledContentColor = MaterialTheme.eumColors.textTertiary,
                ),
            ) {
                Text(stringResource(R.string.checkout_payment_button, uiModel.paymentSummary.finalAmountLabel))
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
        ) {
            item { CheckoutSectionHeader(R.string.checkout_discount) }
            item {
                Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    InfoActionRow(
                        title = stringResource(R.string.checkout_coupon),
                        value = stringResource(R.string.checkout_available_coupons, uiModel.couponCount),
                        onClick = { onEvent(CheckoutScreenEvent.CouponClicked) },
                    )
                    InfoActionRow(
                        title = stringResource(R.string.checkout_points),
                        value = uiModel.pointsAvailableLabel.ifBlank { stringResource(R.string.checkout_zero_points) },
                        onClick = { onEvent(CheckoutScreenEvent.UseAllPointsClicked) },
                        actionLabel = stringResource(if (uiModel.isUsingAllPoints) R.string.common_cancel else R.string.checkout_use_all_points),
                    )
                }
            }
            item { CheckoutSectionHeader(R.string.checkout_payment_method) }
            item {
                CheckoutPaymentMethodSelector(
                    selectedMethod = uiModel.paymentMethod,
                    onMethodSelected = { onEvent(CheckoutScreenEvent.PaymentMethodSelected(it)) },
                    modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
                )
            }
            item { CheckoutSectionHeader(R.string.checkout_receipt_document) }
            item {
                Column(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x4)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x4), verticalAlignment = Alignment.CenterVertically) {
                        receiptTypes.forEach { (type, labelResId) ->
                            val isSelected = uiModel.receiptType == type
                            Row(
                                modifier = Modifier.weight(1f)
                                    .clickable(role = Role.RadioButton) { onEvent(CheckoutScreenEvent.ReceiptTypeSelected(type)) }
                                    .semantics { selected = isSelected },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(selected = isSelected, onClick = { onEvent(CheckoutScreenEvent.ReceiptTypeSelected(type)) })
                                Text(stringResource(labelResId), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
            if (uiModel.receiptType == CheckoutReceiptType.CashReceipt && uiModel.receiptDetails.type == CheckoutReceiptDetailsType.CashReceiptPhone) {
                item {
                    Row(Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20)) {
                        ReceiptModeButton(
                            label = stringResource(R.string.checkout_cash_receipt_income),
                            isSelected = uiModel.receiptDetails.receiptMode == CheckoutReceiptMode.IncomeDeduction,
                            onClick = { onEvent(CheckoutScreenEvent.ReceiptModeSelected(CheckoutReceiptMode.IncomeDeduction)) },
                            modifier = Modifier.weight(1f),
                        )
                        ReceiptModeButton(
                            label = stringResource(R.string.checkout_cash_receipt_expense),
                            isSelected = uiModel.receiptDetails.receiptMode == CheckoutReceiptMode.ExpenseProof,
                            onClick = { onEvent(CheckoutScreenEvent.ReceiptModeSelected(CheckoutReceiptMode.ExpenseProof)) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
            if (uiModel.receiptType != CheckoutReceiptType.None) {
                item {
                    CheckoutReceiptDetailsCard(
                        details = uiModel.receiptDetails,
                        onEvent = onEvent,
                        modifier = Modifier.padding(horizontal = EumSpacing.x20),
                    )
                }
            }
            item { CheckoutSectionHeader(R.string.checkout_payment_summary) }
            item {
                CheckoutPriceSummaryCard(
                    summary = uiModel.paymentSummary,
                    showDiscount = true,
                    modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x8),
                )
            }
            item {
                Row(
                    Modifier.fillMaxWidth().clickable { onEvent(CheckoutScreenEvent.OrderAgreementChanged(!uiModel.isOrderAgreementAccepted)) }
                        .padding(horizontal = EumSpacing.x12, vertical = EumSpacing.x8),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = uiModel.isOrderAgreementAccepted,
                        onCheckedChange = { onEvent(CheckoutScreenEvent.OrderAgreementChanged(it)) },
                    )
                    Text(stringResource(R.string.checkout_order_agreement), modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
                    TextButton(onClick = { onEvent(CheckoutScreenEvent.AgreementDetailsClicked) }) {
                        Text(stringResource(R.string.common_view_details))
                    }
                }
                Text(
                    stringResource(R.string.checkout_agreement_required_notice),
                    modifier = Modifier.fillMaxWidth().padding(start = EumSpacing.x40, bottom = EumSpacing.x16),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.eumColors.textTertiary,
                )
            }
        }
    }
}

private fun isReceiptDetailsValid(uiModel: CheckoutUiModel): Boolean {
    val receiptDetails = uiModel.receiptDetails
    return when (uiModel.receiptType) {
        CheckoutReceiptType.None -> true

        CheckoutReceiptType.CashReceipt -> when (receiptDetails.type) {
            CheckoutReceiptDetailsType.CashReceiptPhone -> receiptDetails.phoneNumber.hasValidKoreanPhoneNumber()
            else -> true
        }

        CheckoutReceiptType.TaxInvoice -> when (receiptDetails.type) {
            CheckoutReceiptDetailsType.SavedBusinessInformation,
            CheckoutReceiptDetailsType.ManualBusinessInformation,
            -> receiptDetails.registrationNumber.hasValidBusinessRegistrationNumber() &&
                receiptDetails.businessName.isNotBlank() &&
                receiptDetails.representativeName.isNotBlank() &&
                receiptDetails.email.isValidReceiptEmail()

            else -> false
        }
    }
}

private fun String.isValidReceiptEmail(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

private fun String.hasValidBusinessRegistrationNumber(): Boolean = hasOnlyNumericSeparators() && count(Char::isDigit) == 10

private fun String.hasValidKoreanPhoneNumber(): Boolean = hasOnlyNumericSeparators() && count(Char::isDigit) in 10..11

private fun String.hasOnlyNumericSeparators(): Boolean = isNotBlank() && all { it in '0'..'9' || it == '-' || it.isWhitespace() }

@Composable
private fun InfoActionRow(
    title: String,
    value: String,
    onClick: () -> Unit,
    actionLabel: String? = null,
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
        if (actionLabel != null) {
            TextButton(onClick = onClick) { Text(actionLabel) }
        } else {
            TextButton(onClick = onClick) { Text(stringResource(R.string.common_view_details)) }
        }
    }
}

@Composable
private fun ReceiptModeButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(role = Role.RadioButton, onClick = onClick).semantics { selected = isSelected },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = isSelected, onClick = onClick)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
