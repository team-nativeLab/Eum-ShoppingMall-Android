package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
internal fun AuthPasswordField(
    label: String,
    value: CharArray,
    isVisible: Boolean,
    onValueChange: (CharArray) -> Unit,
    onVisibilityChange: () -> Unit,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    errorText: String? = null,
    successText: String? = null,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        androidx.compose.foundation.layout.Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(EumSpacing.x4)) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value = value.concatToString(),
                onValueChange = { onValueChange(it.toCharArray()) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).semantics { contentDescription = label },
                singleLine = true,
                isError = errorText != null,
                visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                trailingIcon = {
                    IconButton(onClick = onVisibilityChange, modifier = Modifier.size(48.dp)) {
                        Icon(
                            painterResource(if (isVisible) R.drawable.ic_eye else R.drawable.ic_eye_off),
                            contentDescription = if (isVisible) androidx.compose.ui.res.stringResource(R.string.auth_password_hide) else androidx.compose.ui.res.stringResource(R.string.auth_password_show),
                            tint = MaterialTheme.eumColors.textSecondary,
                        )
                    }
                },
                shape = MaterialTheme.shapes.medium,
            )
            val message = errorText ?: successText ?: helperText
            if (message != null) {
                Text(
                    message,
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        errorText != null -> MaterialTheme.eumColors.danger
                        successText != null -> MaterialTheme.eumColors.success
                        else -> MaterialTheme.eumColors.textTertiary
                    },
                )
            }
        }
    }
}
