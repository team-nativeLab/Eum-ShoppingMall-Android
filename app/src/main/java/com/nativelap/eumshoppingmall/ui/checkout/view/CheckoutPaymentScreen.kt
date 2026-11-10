package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutPaymentMethodSelector
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutPriceSummaryCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutReceiptDetailsCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutReceiptTypeSelector
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutSectionHeader
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutStepIndicator
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.common.EumPrimaryButton
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.common.ResponsiveWidthContainer
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumCheckboxColors
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutPaymentScreen(
    uiModel: CheckoutUiModel,
    onEvent: (CheckoutScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(Modifier.statusBarsPadding()) {
                CatalogTopBar(title = stringResource(R.string.checkout_title), onBackClick = { onEvent(CheckoutScreenEvent.BackClicked) })
                CheckoutStepIndicator(currentStep = 2, modifier = Modifier.padding(horizontal = EumSpacing.x40, vertical = EumSpacing.x12))
            }
        },
        bottomBar = {
            ResponsiveWidthContainer {
                val blockReason = checkoutPaymentBlockReason(uiModel)
                Column(
                    Modifier.fillMaxWidth().imePadding().navigationBarsPadding()
                        .padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12),
                    verticalArrangement = Arrangement.spacedBy(EumSpacing.x8),
                ) {
                    // 버튼이 비활성인 이유를 버튼 위에 보여 준다(U-03). 활성이면 문구를 숨긴다.
                    if (blockReason != null) {
                        Text(
                            stringResource(blockReason.messageResId),
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.eumColors.danger,
                        )
                    }
                    EumPrimaryButton(
                        onClick = { onEvent(CheckoutScreenEvent.PaymentClicked) },
                        enabled = blockReason == null,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.checkout_payment_button, uiModel.paymentSummary.finalAmountLabel))
                    }
                }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        ResponsiveContentContainer(Modifier.fillMaxSize()) {
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
                            actionLabel = stringResource(R.string.common_view_details),
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
                    CheckoutReceiptTypeSelector(
                        selectedType = uiModel.receiptType,
                        onTypeSelected = { onEvent(CheckoutScreenEvent.ReceiptTypeSelected(it)) },
                        modifier = Modifier.padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x4),
                    )
                }
                // 현금영수증 소득공제·지출증빙 전환은 증빙 상세 카드 안에 둔다(Figma A-09c).
                // 상세 유형이 없으면 빈 카드를 그리지 않는다.
                if (uiModel.receiptType != CheckoutReceiptType.None && uiModel.receiptDetails.type != CheckoutReceiptDetailsType.None) {
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
                            colors = eumCheckboxColors(),
                            checked = uiModel.isOrderAgreementAccepted,
                            onCheckedChange = { onEvent(CheckoutScreenEvent.OrderAgreementChanged(it)) },
                        )
                        Text(stringResource(R.string.checkout_order_agreement), modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
                        IconButton(onClick = { onEvent(CheckoutScreenEvent.AgreementDetailsClicked) }) {
                            Icon(
                                painterResource(R.drawable.ic_chevron_right),
                                contentDescription = stringResource(R.string.common_view_details),
                                tint = MaterialTheme.eumColors.textSecondary,
                                modifier = Modifier.size(20.dp),
                            )
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
}

/** 결제 버튼을 막는 이유. 여러 개 해당하면 위에서부터 하나만 보여 준다. */
internal enum class CheckoutPaymentBlockReason(@param:StringRes val messageResId: Int) {
    NoPaymentMethod(R.string.checkout_payment_block_method),
    AgreementNotAccepted(R.string.checkout_payment_block_agreement),
    InvalidReceiptDetails(R.string.checkout_payment_block_receipt),
}

/** 결제 버튼이 비활성인 이유. null이면 결제할 수 있다. */
internal fun checkoutPaymentBlockReason(uiModel: CheckoutUiModel): CheckoutPaymentBlockReason? = when {
    uiModel.paymentMethod == null -> CheckoutPaymentBlockReason.NoPaymentMethod
    !uiModel.isOrderAgreementAccepted -> CheckoutPaymentBlockReason.AgreementNotAccepted
    !isReceiptDetailsValid(uiModel) -> CheckoutPaymentBlockReason.InvalidReceiptDetails
    else -> null
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
    actionLabel: String,
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textPrimary)
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.padding(start = EumSpacing.x8),
            contentPadding = PaddingValues(horizontal = EumSpacing.x12, vertical = EumSpacing.x4),
        ) {
            Text(actionLabel, style = MaterialTheme.typography.labelMedium)
        }
    }
}
