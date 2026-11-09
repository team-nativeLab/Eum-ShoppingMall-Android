package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.checkout.viewmodel.CheckoutReceiptType
import com.nativelap.eumshoppingmall.ui.theme.eumColors

// 현금영수증·세금계산서·신청 안 함을 한 줄 라디오로 고른다(Figma A-09c).
@Composable
fun CheckoutReceiptTypeSelector(
    selectedType: CheckoutReceiptType,
    onTypeSelected: (CheckoutReceiptType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val receiptTypes = listOf(
        CheckoutReceiptType.CashReceipt to R.string.checkout_cash_receipt,
        CheckoutReceiptType.TaxInvoice to R.string.checkout_tax_invoice,
        CheckoutReceiptType.None to R.string.checkout_not_requested,
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        receiptTypes.forEach { (type, labelResId) ->
            val isSelected = selectedType == type
            Row(
                modifier = Modifier.weight(1f)
                    .clickable(role = Role.RadioButton) { onTypeSelected(type) }
                    .semantics { selected = isSelected },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onTypeSelected(type) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.eumColors.textPrimary,
                        unselectedColor = MaterialTheme.eumColors.textSecondary,
                    ),
                )
                Text(stringResource(labelResId), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
