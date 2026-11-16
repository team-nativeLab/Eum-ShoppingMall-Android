package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

private val AuthFieldHorizontalPadding = 14.dp
private val AuthFieldVerticalPadding = 16.dp
private val AuthFieldBorderThickness = 1.dp
private val AuthFieldEmphasizedBorderThickness = 1.5.dp

/**
 * 인증 화면 공용 한 줄 입력 필드. M3 OutlinedTextField와 같은 BasicTextField + DecorationBox 구조를 쓰되
 * 문자열 기반 OutlinedTextField가 열어 두지 않는 contentPadding(좌우 14)과 포커스·오류 보더(1.5dp)를 지정한다.
 * 세로 패딩은 M3 기본 16dp를 그대로 두어 필드 높이 결정(48/52 미확정)을 건드리지 않는다.
 * minLines는 singleLine=false일 때만 사용한다.
 */
@Composable
internal fun AuthOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String? = null,
    isError: Boolean = false,
    errorDescription: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val inputTextColor = when {
        !enabled -> colors.disabledTextColor
        isError -> colors.errorTextColor
        isFocused -> colors.focusedTextColor
        else -> colors.unfocusedTextColor
    }
    val inputTextStyle = MaterialTheme.typography.bodyMedium.merge(TextStyle(color = inputTextColor))
    val fieldShape = MaterialTheme.shapes.medium
    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight).semantics {
                if (isError && errorDescription != null) error(errorDescription)
            },
            enabled = enabled,
            textStyle = inputTextStyle,
            cursorBrush = SolidColor(if (isError) colors.errorCursorColor else colors.cursorColor),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            minLines = minLines,
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    isError = isError,
                    placeholder = placeholder?.let { placeholderText ->
                        { Text(placeholderText, style = MaterialTheme.typography.bodyMedium) }
                    },
                    trailingIcon = trailingIcon,
                    colors = colors,
                    contentPadding = PaddingValues(
                        horizontal = AuthFieldHorizontalPadding,
                        vertical = AuthFieldVerticalPadding,
                    ),
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = fieldShape,
                            focusedBorderThickness = AuthFieldEmphasizedBorderThickness,
                            unfocusedBorderThickness = if (isError) {
                                AuthFieldEmphasizedBorderThickness
                            } else {
                                AuthFieldBorderThickness
                            },
                        )
                    },
                )
            },
        )
    }
}
