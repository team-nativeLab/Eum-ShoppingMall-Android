package com.nativelap.eumshoppingmall.ui.audit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun UiAuditTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minHeight: androidx.compose.ui.unit.Dp = EumSpacing.x52,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier.fillMaxWidth().heightIn(min = minHeight)
            .then(
                if (onClick == null) Modifier else Modifier.clickable(role = Role.Button, onClick = onClick),
            ).semantics {
                contentDescription = label
                selected = isSelected
            },
        shape = RoundedCornerShape(EumRadius.x8),
        color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.eumColors.surface,
        border = BorderStroke(
            EumSpacing.x1,
            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.eumColors.lineNeutral,
        ),
    ) {
        Box(Modifier.padding(horizontal = UiAuditTextFieldHorizontalPadding, vertical = EumSpacing.x12)) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.eumColors.textDisabled,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = !readOnly || onClick == null,
                readOnly = readOnly,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = MaterialTheme.eumColors.textPrimary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                ),
                cursorBrush = SolidColor(MaterialTheme.eumColors.textPrimary),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                maxLines = maxLines,
            )
        }
    }
}

// Figma 입력칸 좌우 여백 14dp. EumSpacing에 14dp 토큰이 없어 여기서만 쓴다.
private val UiAuditTextFieldHorizontalPadding = 14.dp
