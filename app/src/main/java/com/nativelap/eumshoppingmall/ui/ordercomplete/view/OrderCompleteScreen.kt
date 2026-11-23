package com.nativelap.eumshoppingmall.ui.ordercomplete.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.catalog.components.CatalogTopBar
import com.nativelap.eumshoppingmall.ui.checkout.view.components.CheckoutStepIndicator
import com.nativelap.eumshoppingmall.ui.common.EumOutlinedButton
import com.nativelap.eumshoppingmall.ui.common.EumPrimaryButton
import com.nativelap.eumshoppingmall.ui.common.ResponsiveContentContainer
import com.nativelap.eumshoppingmall.ui.common.ResponsiveWidthContainer
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteScreenEvent
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.isOrderDetailsAvailable
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.orderCompleteValueOrNull
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.EumTypographyTokens
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderCompleteScreen(
    uiModel: OrderCompleteUiModel,
    onEvent: (OrderCompleteScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 빈 값은 리소스 문구로 대체한다. 문구를 이어 붙이지 않고 값 하나를 고른다.
    val orderNumberText = orderCompleteValueOrNull(uiModel.orderNumber)
        ?.let { orderNumber -> stringResource(R.string.checkout_order_number, orderNumber) }
        ?: stringResource(R.string.checkout_order_number_unavailable)
    val paymentAmountText = orderCompleteValueOrNull(uiModel.paymentAmountLabel)
        ?: stringResource(R.string.checkout_amount_unconfirmed)
    val paymentMethodText = orderCompleteValueOrNull(uiModel.paymentMethodLabel)
        ?: stringResource(R.string.checkout_order_value_unavailable)
    val addressText = orderCompleteValueOrNull(uiModel.addressLabel)
        ?: stringResource(R.string.checkout_order_value_unavailable)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(Modifier.statusBarsPadding()) {
                CatalogTopBar(
                    title = stringResource(R.string.checkout_order_complete_title),
                    onBackClick = { onEvent(OrderCompleteScreenEvent.CloseClicked) },
                    isCloseAction = true,
                )
            }
        },
        bottomBar = {
            ResponsiveWidthContainer {
                Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface).navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                    EumOutlinedButton(
                        onClick = { onEvent(OrderCompleteScreenEvent.ContinueShoppingClicked) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.checkout_continue_shopping))
                    }
                    EumPrimaryButton(
                        onClick = { onEvent(OrderCompleteScreenEvent.ViewOrderDetailsClicked) },
                        enabled = isOrderDetailsAvailable(uiModel.orderNumber),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.checkout_order_detail_button))
                    }
                }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        ResponsiveContentContainer(Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(contentPadding).verticalScroll(rememberScrollState())) {
                // 스텝퍼는 본문 첫 항목이다(Figma A-10).
                CheckoutStepIndicator(currentStep = 4)
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = EumSpacing.x20),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Surface(
                        modifier = Modifier.padding(top = EumSpacing.x40).size(64.dp),
                        shape = CircleShape,
                        color = MaterialTheme.eumColors.primary,
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            tint = MaterialTheme.eumColors.inverse,
                            modifier = Modifier.padding(EumSpacing.x16),
                        )
                    }

                    Spacer(Modifier.height(EumSpacing.x24))

                    Text(
                        stringResource(if (uiModel.isVirtualAccount) R.string.checkout_order_received else R.string.checkout_order_complete),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.eumColors.textPrimary,
                    )

                    Spacer(Modifier.height(EumSpacing.x8))

                    Text(
                        orderNumberText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.eumColors.textSecondary,
                    )

                    Spacer(Modifier.height(EumSpacing.x8))

                    Text(
                        stringResource(R.string.checkout_demo_order_notice),
                        modifier = Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surfaceSecondary, MaterialTheme.shapes.small).padding(EumSpacing.x12),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.textSecondary,
                    )

                    Spacer(Modifier.height(EumSpacing.x32))

                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EumSpacing.x16)) {
                        if (uiModel.isVirtualAccount) {
                            Text(stringResource(R.string.checkout_virtual_account_deadline_notice), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                            OrderInformationCard(
                                rows = listOfNotNull(
                                    uiModel.bankLabel?.let { OrderInformationRow(stringResource(R.string.checkout_bank), it) },
                                    uiModel.accountNumber?.let { OrderInformationRow(stringResource(R.string.checkout_account_number), it) },
                                    uiModel.depositorLabel?.let { OrderInformationRow(stringResource(R.string.checkout_depositor), it) },
                                    uiModel.depositAmountLabel?.let { OrderInformationRow(stringResource(R.string.checkout_deposit_amount), it) },
                                    uiModel.depositDeadlineLabel?.let { OrderInformationRow(stringResource(R.string.checkout_payment_deadline_short), it) },
                                ),
                                onCopyAccount = { onEvent(OrderCompleteScreenEvent.CopyAccountNumberClicked) },
                                isAccountCard = true,
                            )
                            Text(
                                stringResource(R.string.checkout_virtual_account_expiry_notice),
                                modifier = Modifier.fillMaxWidth().background(MaterialTheme.eumColors.dangerTint, MaterialTheme.shapes.small).padding(EumSpacing.x12),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.eumColors.danger,
                            )
                        }
                        OrderInformationCard(
                            rows = listOf(
                                OrderInformationRow(stringResource(R.string.checkout_payment_total_label), paymentAmountText, isAmount = true),
                                OrderInformationRow(stringResource(R.string.checkout_payment_method), paymentMethodText),
                                OrderInformationRow(stringResource(R.string.checkout_delivery_address), addressText),
                            ),
                            onCopyAccount = {},
                            isAccountCard = false,
                        )
                    }
                }
            }
        }
    }
}

private data class OrderInformationRow(
    val label: String,
    val value: String,
    val isAmount: Boolean = false,
)

@Composable
private fun OrderInformationCard(
    rows: List<OrderInformationRow>,
    onCopyAccount: () -> Unit,
    isAccountCard: Boolean,
) {
    Column(
        Modifier.fillMaxWidth()
            .background(MaterialTheme.eumColors.surface, MaterialTheme.shapes.medium)
            .border(1.dp, MaterialTheme.eumColors.line, MaterialTheme.shapes.medium)
            .padding(EumSpacing.x20),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        rows.forEach { (label, value, isAmount) ->
            // 주문 요약 카드는 라벨 폭 72 + 값 start 정렬, 결제금액 값은 priceM(Figma A-10). 입금 계좌 카드는 기존 배치를 유지한다.
            val isSummaryCard = !isAccountCard
            Row(Modifier.fillMaxWidth(), verticalAlignment = if (isSummaryCard) Alignment.Top else Alignment.CenterVertically) {
                Text(
                    label,
                    modifier = if (isSummaryCard) Modifier.widthIn(min = 72.dp).padding(end = EumSpacing.x8) else Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textSecondary,
                )
                Text(
                    value,
                    modifier = Modifier.weight(1f),
                    style = if (isSummaryCard && isAmount) EumTypographyTokens.priceM else MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textPrimary,
                    maxLines = if (isAccountCard) 1 else 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (isSummaryCard) TextAlign.Start else TextAlign.End,
                )
                if (isAccountCard && label == stringResource(R.string.checkout_account_number)) {
                    TextButton(
                        onClick = onCopyAccount,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.eumColors.textPrimary),
                    ) { Text(stringResource(R.string.common_copy)) }
                }
            }
        }
    }
}
