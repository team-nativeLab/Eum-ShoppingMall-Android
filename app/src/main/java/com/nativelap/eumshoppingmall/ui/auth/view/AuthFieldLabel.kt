package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/** 입력 필드 라벨. 필수 항목이면 라벨 뒤에 danger 색 `*`를 붙인다. */
@Composable
internal fun AuthFieldLabel(
    label: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    style: TextStyle = MaterialTheme.typography.labelMedium,
) {
    val requiredMarkColor = MaterialTheme.eumColors.danger
    val labelText = buildAnnotatedString {
        append(label)
        if (isRequired) {
            withStyle(SpanStyle(color = requiredMarkColor)) { append("*") }
        }
    }
    Text(
        labelText,
        modifier,
        style = style,
        color = MaterialTheme.eumColors.textPrimary,
    )
}
