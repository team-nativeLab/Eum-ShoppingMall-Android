package com.nativelap.eumshoppingmall.ui.ordercomplete.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteScreenEvent
import com.nativelap.eumshoppingmall.ui.ordercomplete.viewmodel.OrderCompleteUiModel
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun OrderCompleteScreen(
    uiModel: OrderCompleteUiModel,
    onEvent: (OrderCompleteScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(Modifier.statusBarsPadding()) {
                CatalogTopBar(
                    title = stringResource(R.string.checkout_order_complete_title),
                    onBackClick = { onEvent(OrderCompleteScreenEvent.CloseClicked) },
                )
                CheckoutStepIndicator(currentStep = 3, modifier = Modifier.padding(horizontal = EumSpacing.x40, vertical = EumSpacing.x12))
            }
        },
        bottomBar = {
            Column(Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surface).navigationBarsPadding().padding(horizontal = EumSpacing.x20, vertical = EumSpacing.x12), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                OutlinedButton(
                    onClick = { onEvent(OrderCompleteScreenEvent.ContinueShoppingClicked) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                ) {
                    Text(stringResource(R.string.checkout_continue_shopping))
                }
                Button(
                    onClick = { onEvent(OrderCompleteScreenEvent.ViewOrderDetailsClicked) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.eumColors.textPrimary,
                        contentColor = MaterialTheme.eumColors.inverse,
                    ),
                ) {
                    Text(stringResource(R.string.orders_detail))
                }
            }
        },
        containerColor = MaterialTheme.eumColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(contentPadding).verticalScroll(rememberScrollState()).padding(horizontal = EumSpacing.x20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EumSpacing.x16),
        ) {
            Surface(
                modifier = Modifier.padding(top = EumSpacing.x24).size(64.dp),
                shape = CircleShape,
                color = if (uiModel.isVirtualAccount) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.success,
            ) {
                Icon(
                    painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = MaterialTheme.eumColors.inverse,
                    modifier = Modifier.padding(EumSpacing.x16),
                )
            }
            Text(
                stringResource(if (uiModel.isVirtualAccount) R.string.checkout_order_received else R.string.checkout_order_complete),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.eumColors.textPrimary,
            )
            Text(
                stringResource(R.string.checkout_order_number, uiModel.orderNumber),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.eumColors.textSecondary,
            )
            if (uiModel.isVirtualAccount) {
                Text(stringResource(R.string.checkout_virtual_account_deadline_notice), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                OrderInformationCard(
                    rows = listOfNotNull(
                        uiModel.bankLabel?.let { stringResource(R.string.checkout_bank) to it },
                        uiModel.accountNumber?.let { stringResource(R.string.checkout_account_number) to it },
                        uiModel.depositorLabel?.let { stringResource(R.string.checkout_depositor) to it },
                        uiModel.depositAmountLabel?.let { stringResource(R.string.checkout_deposit_amount) to it },
                        uiModel.depositDeadlineLabel?.let { stringResource(R.string.checkout_payment_deadline_short) to it },
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
                    stringResource(R.string.checkout_payment_amount) to uiModel.paymentAmountLabel,
                    stringResource(R.string.checkout_payment_method_label) to uiModel.paymentMethodLabel,
                    stringResource(R.string.checkout_delivery_address) to uiModel.addressLabel,
                ),
                onCopyAccount = {},
                isAccountCard = false,
            )
        }
    }
}

@Composable
private fun OrderInformationCard(
    rows: List<Pair<String, String>>,
    onCopyAccount: () -> Unit,
    isAccountCard: Boolean,
) {
    Column(
        Modifier.fillMaxWidth().background(MaterialTheme.eumColors.surfaceSecondary, MaterialTheme.shapes.medium).padding(EumSpacing.x20),
        verticalArrangement = Arrangement.spacedBy(EumSpacing.x12),
    ) {
        rows.forEachIndexed { index, (label, value) ->
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.eumColors.textSecondary)
                Text(
                    value,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textPrimary,
                    maxLines = if (isAccountCard) 1 else 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                )
                if (isAccountCard && label == stringResource(R.string.checkout_account_number)) {
                    TextButton(onClick = onCopyAccount) { Text(stringResource(R.string.common_copy)) }
                }
            }
        }
    }
}
