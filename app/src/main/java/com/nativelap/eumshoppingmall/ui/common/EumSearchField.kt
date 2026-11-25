package com.nativelap.eumshoppingmall.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

/**
 * 캡슐형 검색 입력 필드. 돋보기 아이콘과 검정 1dp 테두리(포커스 시 1.5dp)를 쓰며 최소 높이는 44dp다.
 * [onClear]가 있고 입력값이 있을 때만 48dp 지우기 버튼을 보인다.
 * [contentDescription]이 있으면 입력 칸의 접근성 설명으로 쓴다.
 */
@Composable
fun EumSearchField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {},
    onClear: (() -> Unit)? = null,
    contentDescription: String? = null,
    readOnly: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(false) }
    val fieldDescription = contentDescription
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .background(MaterialTheme.eumColors.surface, RoundedCornerShape(EumRadius.capsule))
            .border(
                width = if (isFocused) 1.5.dp else 1.dp,
                color = MaterialTheme.eumColors.textPrimary,
                shape = RoundedCornerShape(EumRadius.capsule),
            )
            .padding(horizontal = EumSpacing.x16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EumSpacing.x8),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null,
            tint = MaterialTheme.eumColors.textPrimary,
            modifier = Modifier.size(20.dp),
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            modifier = Modifier.weight(1f).heightIn(min = 44.dp)
                .onFocusChanged { focusState -> isFocused = focusState.isFocused }
                .let { base ->
                    if (fieldDescription == null) base else base.semantics { this.contentDescription = fieldDescription }
                },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.eumColors.textPrimary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            decorationBox = { innerTextField ->
                // 플레이스홀더와 입력 텍스트를 같은 상자에서 세로 가운데로 맞춘다.
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.eumColors.textTertiary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    innerTextField()
                }
            },
        )
        if (onClear != null && value.isNotEmpty()) {
            IconButton(onClick = onClear, modifier = Modifier.size(48.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_circle_x),
                    contentDescription = stringResource(R.string.search_clear_query),
                    tint = MaterialTheme.eumColors.textTertiary,
                )
            }
        }
    }
}
