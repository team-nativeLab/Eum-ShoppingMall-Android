package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun AuthPrimaryAction(label: String, enabled: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.eumColors.textPrimary,
            contentColor = MaterialTheme.eumColors.background,
            disabledContainerColor = MaterialTheme.eumColors.backgroundSubtle,
            disabledContentColor = MaterialTheme.eumColors.textTertiary,
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
    }
}
