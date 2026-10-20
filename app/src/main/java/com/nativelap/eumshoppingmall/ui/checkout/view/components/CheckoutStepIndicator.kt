package com.nativelap.eumshoppingmall.ui.checkout.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CheckoutStepIndicator(
    currentStep: Int,
    modifier: Modifier = Modifier,
) {
    val labels = listOf(
        stringResource(R.string.checkout_step_address),
        stringResource(R.string.checkout_step_payment),
        stringResource(R.string.checkout_step_complete),
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        labels.forEachIndexed { index, label ->
            if (index > 0) {
                Box(
                    Modifier.weight(1f).height(1.dp)
                        .background(if (index < currentStep) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.line),
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(24.dp).background(
                        color = if (index < currentStep) MaterialTheme.eumColors.textPrimary else MaterialTheme.eumColors.surface,
                        shape = CircleShape,
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        if (index < currentStep) "✓" else (index + 1).toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (index < currentStep) MaterialTheme.eumColors.inverse else MaterialTheme.eumColors.textSecondary,
                    )
                }
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.eumColors.textSecondary)
            }
        }
    }
}
