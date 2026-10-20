package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutPaymentMethod
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutPaymentMethodSelector(
    selectedMethod: CheckoutPaymentMethod?,
    onMethodSelected: (CheckoutPaymentMethod) -> Unit,
    modifier: Modifier = Modifier,
) {
    val methods = listOf(
        CheckoutPaymentMethod.CreditCard to R.string.checkout_payment_card,
        CheckoutPaymentMethod.BankTransfer to R.string.checkout_payment_bank_transfer,
        CheckoutPaymentMethod.VirtualAccount to R.string.checkout_virtual_account,
        CheckoutPaymentMethod.SimplePayA to R.string.checkout_payment_simple_a,
        CheckoutPaymentMethod.SimplePayB to R.string.checkout_payment_simple_b,
        CheckoutPaymentMethod.MobilePayment to R.string.checkout_payment_mobile,
    )
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        methods.chunked(2).forEach { rowMethods ->
            Row(horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
                rowMethods.forEach { (method, labelResId) ->
                    val isSelected = method == selectedMethod
                    Surface(
                        modifier = Modifier.weight(1f)
                            .selectable(selected = isSelected, role = Role.RadioButton) { onMethodSelected(method) }
                            .semantics { selected = isSelected },
                        shape = RoundedCornerShape(EumRadius.x8),
                        color = if (isSelected) MaterialTheme.eumColors.surface else MaterialTheme.eumColors.surfaceSecondary,
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.lineControl,
                        ),
                    ) {
                        Text(
                            stringResource(labelResId),
                            modifier = Modifier.padding(horizontal = EumSpacing.x8, vertical = EumSpacing.x12),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.eumColors.textPrimary,
                        )
                    }
                }
            }
        }
    }
}
