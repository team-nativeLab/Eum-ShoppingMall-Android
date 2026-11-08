package com.nativelap.eumshoppingmall.ui.account.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun AccountPrimaryButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    containerColor: Color? = null,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
        shape = MaterialTheme.shapes.medium,
        colors = containerColor?.let { ButtonDefaults.buttonColors(containerColor = it) }
            ?: ButtonDefaults.buttonColors(),
    ) {
        Text(label)
    }
}
