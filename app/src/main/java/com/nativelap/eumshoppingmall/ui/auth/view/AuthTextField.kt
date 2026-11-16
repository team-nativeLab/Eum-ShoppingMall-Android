package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing

@Composable
internal fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true,
    isRequired: Boolean = false,
) {
    val requiredDescription = stringResource(R.string.auth_field_required)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(EumSpacing.x8)) {
        AuthFieldLabel(label, isRequired = isRequired)
        AuthOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).semantics {
                contentDescription = label
                if (isRequired) stateDescription = requiredDescription
            },
            enabled = enabled,
            placeholder = placeholder,
            isError = errorText != null,
            errorDescription = errorText,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        )
        if (errorText != null) {
            AuthHelperMessage(errorText, AuthHelperTone.Error)
        } else if (helperText != null) {
            AuthHelperMessage(helperText, AuthHelperTone.Info)
        }
    }
}
