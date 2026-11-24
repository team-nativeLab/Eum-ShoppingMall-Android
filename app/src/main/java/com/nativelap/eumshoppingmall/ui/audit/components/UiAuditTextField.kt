package com.nativelap.eumshoppingmall.ui.audit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
    contentVerticalPadding: androidx.compose.ui.unit.Dp = EumSpacing.x12,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    // 입력 가능한 칸은 보이는 박스 전체가 탭 대상이다. 첫 줄 밖(여러 줄 칸의 아래쪽 등)을 눌러도 포커스와 키보드가 열린다.
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isEditable = !readOnly && onClick == null
    Surface(
        modifier = modifier.fillMaxWidth().heightIn(min = minHeight)
            .then(
                when {
                    onClick != null -> Modifier.clickable(role = Role.Button, onClick = onClick)

                    // 접근성 시맨틱(클릭 동작)을 더하지 않도록 포인터 입력만 쓴다. 입력칸 자체의 접근성은 BasicTextField가 맡는다.
                    isEditable -> Modifier.pointerInput(Unit) {
                        detectTapGestures {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                    }

                    else -> Modifier
                },
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
        Box(Modifier.padding(horizontal = UiAuditTextFieldHorizontalPadding, vertical = contentVerticalPadding)) {
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
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
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
