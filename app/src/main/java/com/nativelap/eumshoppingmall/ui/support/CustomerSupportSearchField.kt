package com.nativelap.eumshoppingmall.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumCapsuleShape
import com.nativelap.eumshoppingmall.ui.theme.EumSpacing
import com.nativelap.eumshoppingmall.ui.theme.eumColors

private val SEARCH_FIELD_VISUAL_HEIGHT = 44.dp
private val SEARCH_FIELD_TOUCH_HEIGHT = 48.dp

/** 시각 높이(44dp)와 터치 영역(최소 48dp)을 분리한 FAQ 검색 입력. */
@Composable
internal fun CustomerSupportSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }
    val searchHint = stringResource(R.string.customer_support_faq_search_hint)
    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier.fillMaxWidth()
            .heightIn(min = SEARCH_FIELD_TOUCH_HEIGHT)
            // 입력 줄 밖의 시각 영역과 터치 여백을 눌러도 포커스가 이동한다. 포인터 입력만 쓰므로 필드 semantics를 합치지 않는다.
            .pointerInput(Unit) {
                detectTapGestures {
                    // 이미 포커스된 상태에서는 requestFocus가 키보드를 다시 띄우지 않으므로 직접 보여 준다.
                    searchFocusRequester.requestFocus()
                    keyboardController?.show()
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = EumCapsuleShape,
            color = MaterialTheme.eumColors.surfaceSecondary,
            border = if (isFocused) BorderStroke(EumSpacing.x1, MaterialTheme.eumColors.lineStrong) else null,
        ) {
            Row(
                Modifier.heightIn(min = SEARCH_FIELD_VISUAL_HEIGHT).padding(horizontal = EumSpacing.x16),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(EumSpacing.x20),
                    tint = MaterialTheme.eumColors.textSecondary,
                )
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f).padding(start = EumSpacing.x12)
                        .focusRequester(searchFocusRequester)
                        .onFocusChanged { isFocused = it.isFocused },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.eumColors.textPrimary),
                    cursorBrush = SolidColor(MaterialTheme.eumColors.textPrimary),
                    // 힌트를 필드 내부 장식으로 두어 입력 텍스트와 함께 필드 semantics에 병합된다.
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (query.isEmpty()) {
                                Text(
                                    text = searchHint,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.eumColors.textTertiary,
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            }
        }
    }
}
