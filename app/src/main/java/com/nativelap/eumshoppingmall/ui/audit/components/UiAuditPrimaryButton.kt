package com.nativelap.eumshoppingmall.ui.audit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun UiAuditPrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().heightIn(min = EumSpacing.x52),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.eumColors.textPrimary,
            contentColor = MaterialTheme.eumColors.inverse,
        ),
        shape = RoundedCornerShape(EumRadius.x10),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun UiAuditPrimaryButton(
    labelRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    UiAuditPrimaryButton(stringResource(labelRes), onClick, modifier)
}
