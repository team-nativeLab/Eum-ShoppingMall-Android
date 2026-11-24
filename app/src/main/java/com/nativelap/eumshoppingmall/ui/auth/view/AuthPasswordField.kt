package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
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
    isRequired: Boolean = false,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    labelSpacing: Dp = EumSpacing.x8,
    placeholder: String = stringResource(R.string.auth_password_placeholder),
) {
    val requiredDescription = stringResource(R.string.auth_field_required)
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        Column(verticalArrangement = Arrangement.spacedBy(labelSpacing)) {
            AuthFieldLabel(label, isRequired = isRequired, style = labelStyle)
            AuthOutlinedTextField(
                value = value.concatToString(),
                onValueChange = { onValueChange(it.toCharArray()) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).semantics {
                    contentDescription = label
                    if (isRequired) stateDescription = requiredDescription
                },
                placeholder = placeholder,
                isError = errorText != null,
                errorDescription = errorText,
                visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                trailingIcon = {
                    if (value.isNotEmpty()) {
                        IconButton(onClick = onVisibilityChange, modifier = Modifier.size(48.dp)) {
                            Icon(
                                painterResource(if (isVisible) R.drawable.ic_eye else R.drawable.ic_eye_off),
                                contentDescription = stringResource(if (isVisible) R.string.auth_password_hide else R.string.auth_password_show),
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.eumColors.textSecondary,
                            )
                        }
                    }
                },
            )
        }
        when {
            errorText != null -> AuthHelperMessage(errorText, AuthHelperTone.Error)
            successText != null -> AuthHelperMessage(successText, AuthHelperTone.Confirmed)
            helperText != null -> AuthHelperMessage(helperText, AuthHelperTone.Info)
        }
    }
}
