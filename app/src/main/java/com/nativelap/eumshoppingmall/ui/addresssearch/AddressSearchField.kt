package com.nativelap.eumshoppingmall.ui.addresssearch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val AddressSearchFieldMinHeight = 50.dp
private val AddressSearchFieldHorizontalPadding = 14.dp

@Composable
internal fun AddressSearchField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    val fieldShape = RoundedCornerShape(EumRadius.x8)
    val textColor = MaterialTheme.eumColors.textPrimary
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        // 탭 영역과 포커스 대상이 필드 전체가 되도록 배경·테두리·최소 높이를 BasicTextField 자체에 둔다.
        modifier = Modifier.fillMaxWidth()
            .heightIn(min = AddressSearchFieldMinHeight)
            .background(MaterialTheme.eumColors.surface, fieldShape)
            .border(BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineNeutral), fieldShape)
            // 입력 라벨은 편집 노드 한 곳에만 둔다. 제목과 겹치지 않도록 placeholder 문구를 쓴다.
            .semantics { contentDescription = placeholder },
        textStyle = MaterialTheme.typography.bodySmall.copy(color = textColor),
        cursorBrush = SolidColor(textColor),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                // 패딩을 decoration 안에 둬서 좌우 여백도 탭 영역에 포함되게 한다.
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = AddressSearchFieldHorizontalPadding),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        // 힌트 문구가 라벨과 따로 읽혀 두 번 낭독되지 않도록 의미 정보를 비운다.
                        modifier = Modifier.clearAndSetSemantics {},
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.eumColors.addressPlaceholder,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                innerTextField()
            }
        },
    )
}
