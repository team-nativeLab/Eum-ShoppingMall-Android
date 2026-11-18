package com.nativelap.eumshoppingmall.ui.checkout.view

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutOutlineActionButton
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutPaymentMethodSelector
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutPriceSummaryCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutReceiptDetailsCard
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutReceiptTypeSelector
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutSectionHeader
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutStepIndicator
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutOrderSnapshot
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptDetailsType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutScreenEvent
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutUiModel
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.canCompleteCheckoutPayment
import com.nativelap.eumshoppingmall.ui.common.EumPrimaryButton
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.common.ResponsiveWidthContainer
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
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
            }
        },
        bottomBar = {
            val blockReason = checkoutPaymentBlockReason(uiModel)
            // 결제 버튼 영역: 배경·상단 1dp 구분선은 전폭, 버튼 내용만 폭 컨테이너 안에 둔다(Figma A-09).
            Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface)) {
                HorizontalDivider(color = MaterialTheme.eumColors.line)
                ResponsiveWidthContainer {
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
                            Text(
                                stringResource(R.string.checkout_payment_button, uiModel.paymentSummary.finalAmountLabel),
                                style = MaterialTheme.typography.labelLarge,
                                color = if (blockReason == null) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textDisabled,
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        ResponsiveContentContainer(Modifier.fillMaxSize()) {
            // 흰 카드 없이 배경 위 전폭 섹션을 1dp 구분선으로 나눈다(Figma A-09).
            LazyColumn(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                item { CheckoutStepIndicator(currentStep = 2) }
                item { CheckoutSectionHeader(R.string.checkout_discount) }
                item {
                    Column(Modifier.fillMaxWidth().padding(bottom = EumSpacing.x24)) {
                        CouponRow(
                            availableLabel = stringResource(R.string.checkout_available_coupons, uiModel.couponCount),
                            onClick = { onEvent(CheckoutScreenEvent.CouponClicked) },
                        )
                        PointsRow(
                            pointsLabel = uiModel.pointsAvailableLabel.ifBlank { stringResource(R.string.checkout_zero_points) },
                            actionLabel = stringResource(if (uiModel.isUsingAllPoints) R.string.common_cancel else R.string.checkout_use_all_points),
                            onActionClick = { onEvent(CheckoutScreenEvent.UseAllPointsClicked) },
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
                item { CheckoutSectionHeader(R.string.checkout_payment_method) }
                item {
                    CheckoutPaymentMethodSelector(
                        selectedMethod = uiModel.paymentMethod,
                        onMethodSelected = { onEvent(CheckoutScreenEvent.PaymentMethodSelected(it)) },
                        modifier = Modifier.padding(start = EumSpacing.x20, end = EumSpacing.x20, bottom = EumSpacing.x24),
                    )
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
                item { CheckoutSectionHeader(R.string.checkout_receipt_document) }
                item {
                    CheckoutReceiptTypeSelector(
                        selectedType = uiModel.receiptType,
                        onTypeSelected = { onEvent(CheckoutScreenEvent.ReceiptTypeSelected(it)) },
                        modifier = Modifier.padding(horizontal = EumSpacing.x20),
                    )
                    Text(
                        stringResource(R.string.checkout_receipt_change_notice),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.eumColors.textTertiary,
                    )
                }
                if (uiModel.receiptType != CheckoutReceiptType.None) {
                    item {
                        CheckoutReceiptDetailsCard(
                            details = uiModel.receiptDetails,
                            receiptType = uiModel.receiptType,
                            onEvent = onEvent,
                            modifier = Modifier.padding(horizontal = EumSpacing.x20),
                        )
                    }
                }
                item {
                    Spacer(Modifier.height(EumSpacing.x24))

                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
                item { CheckoutSectionHeader(R.string.checkout_payment_summary) }
                item {
                    CheckoutPriceSummaryCard(
                        summary = uiModel.paymentSummary,
                        showDiscount = true,
                        modifier = Modifier.padding(start = EumSpacing.x20, end = EumSpacing.x20, bottom = EumSpacing.x24),
                    )
                    HorizontalDivider(color = MaterialTheme.eumColors.line)
                }
                item {
                    OrderAgreementRow(
                        isAccepted = uiModel.isOrderAgreementAccepted,
                        onAcceptedChange = { onEvent(CheckoutScreenEvent.OrderAgreementChanged(it)) },
                        onDetailsClick = { onEvent(CheckoutScreenEvent.AgreementDetailsClicked) },
                    )
                    Text(
                        stringResource(R.string.checkout_agreement_required_notice),
                        modifier = Modifier.fillMaxWidth().padding(start = 46.dp, bottom = EumSpacing.x16),
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
    UnconfirmedOrderAmount(R.string.checkout_payment_block_amount),
    AgreementNotAccepted(R.string.checkout_payment_block_agreement),
    InvalidReceiptDetails(R.string.checkout_payment_block_receipt),
}

/** 결제 버튼이 비활성인 이유. null이면 결제할 수 있다. */
internal fun checkoutPaymentBlockReason(uiModel: CheckoutUiModel): CheckoutPaymentBlockReason? = when {
    uiModel.paymentSummary.finalAmountWon == null -> CheckoutPaymentBlockReason.UnconfirmedOrderAmount
    uiModel.paymentMethod == null -> CheckoutPaymentBlockReason.NoPaymentMethod
    !uiModel.isOrderAgreementAccepted -> CheckoutPaymentBlockReason.AgreementNotAccepted
    !isReceiptDetailsValid(uiModel) -> CheckoutPaymentBlockReason.InvalidReceiptDetails
    else -> null
}

internal fun checkoutPaymentCanComplete(snapshot: CheckoutOrderSnapshot?, uiModel: CheckoutUiModel): Boolean = canCompleteCheckoutPayment(snapshot) &&
    snapshot?.finalTotalWon == uiModel.paymentSummary.finalAmountWon &&
    checkoutPaymentBlockReason(uiModel) == null

private fun isReceiptDetailsValid(uiModel: CheckoutUiModel): Boolean {
    val receiptDetails = uiModel.receiptDetails
    return when (uiModel.receiptType) {
        CheckoutReceiptType.None -> true

        CheckoutReceiptType.CashReceipt -> when (receiptDetails.type) {
            CheckoutReceiptDetailsType.CashReceiptPhone -> receiptDetails.phoneNumber.hasValidKoreanPhoneNumber()
            else -> false
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

private fun String.hasValidKoreanPhoneNumber(): Boolean {
    if (isEmpty()) return false
    if (all { character -> character in '0'..'9' }) return hasValidKoreanMobileDigits(this)

    val groups = when {
        contains('-') && none(Char::isWhitespace) -> split('-')
        !contains('-') && contains(' ') && all { character -> character in '0'..'9' || character == ' ' } -> split(' ')
        else -> return false
    }
    if (groups.size != 3 || groups.any { group -> group.isEmpty() || group.any { it !in '0'..'9' } }) return false

    val normalizedNumber = groups.joinToString(separator = "")
    if (!hasValidKoreanMobileDigits(normalizedNumber)) return false
    val expectedGroupLengths = if (normalizedNumber.length == 10) listOf(3, 3, 4) else listOf(3, 4, 4)
    return groups.map(String::length) == expectedGroupLengths
}

private fun hasValidKoreanMobileDigits(number: String): Boolean = when {
    number.startsWith("010") -> number.length == 11
    listOf("011", "016", "017", "018", "019").any(number::startsWith) -> number.length in 10..11
    else -> false
}

private fun String.hasOnlyNumericSeparators(): Boolean = isNotBlank() && all { it in '0'..'9' || it == '-' || it.isWhitespace() }

@Composable
private fun CouponRow(
    availableLabel: String,
    onClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable(role = Role.Button, onClick = onClick).padding(horizontal = EumSpacing.x20),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(R.string.checkout_coupon),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
        Text(availableLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)

        Spacer(Modifier.width(EumSpacing.x2))

        Icon(
            painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MaterialTheme.eumColors.textSecondary,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun PointsRow(
    pointsLabel: String,
    actionLabel: String,
    onActionClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(horizontal = EumSpacing.x20),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(R.string.checkout_points),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.eumColors.textPrimary,
        )
        Text(pointsLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.eumColors.textSecondary)

        Spacer(Modifier.width(EumSpacing.x8))

        CheckoutOutlineActionButton(label = actionLabel, onClick = onActionClick, minWidth = 81.dp)
    }
}

// 약관 동의 행: 56dp, 16dp 커스텀 체크 시각. 체크·라벨 영역 전체가 토글 대상이고 chevron은 약관 상세로 이동한다.
@Composable
private fun OrderAgreementRow(
    isAccepted: Boolean,
    onAcceptedChange: (Boolean) -> Unit,
    onDetailsClick: () -> Unit,
) {
    val checkShape = RoundedCornerShape(EumRadius.x4)
    Row(
        // chevron 터치 영역(48dp) 안의 아이콘 20dp가 화면 가장자리에서 20dp 떨어지도록 오른쪽 여백을 줄인다.
        Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(start = EumSpacing.x20, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            Modifier.weight(1f).heightIn(min = 56.dp).toggleable(value = isAccepted, role = Role.Checkbox, onValueChange = onAcceptedChange),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(16.dp)
                    .clip(checkShape)
                    .background(if (isAccepted) MaterialTheme.eumColors.checkboxChecked else MaterialTheme.eumColors.surfaceSecondary)
                    .border(1.5.dp, if (isAccepted) MaterialTheme.eumColors.checkboxChecked else MaterialTheme.eumColors.lineControl, checkShape),
                contentAlignment = Alignment.Center,
            ) {
                if (isAccepted) {
                    Icon(
                        painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = MaterialTheme.eumColors.inverse,
                        modifier = Modifier.size(12.dp),
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Text(
                stringResource(R.string.checkout_order_agreement),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.eumColors.textPrimary,
            )
        }
        IconButton(onClick = onDetailsClick) {
            Icon(
                painterResource(R.drawable.ic_chevron_right),
                contentDescription = stringResource(R.string.common_view_details),
                tint = MaterialTheme.eumColors.textSecondary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
