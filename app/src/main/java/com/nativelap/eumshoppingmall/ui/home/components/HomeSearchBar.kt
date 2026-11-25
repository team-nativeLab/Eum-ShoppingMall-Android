package com.nativelap.eumshoppingmall.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.common.EumSearchField

/**
 * 홈 검색바. 입력을 받지 않는 읽기 전용 필드이며, 탭하면 [onSearchClick]으로 검색 탭에 들어간다.
 * 필드의 접근성 노드는 지우고, 탭을 받는 덮개 Button 하나만 접근성 노드로 남긴다. 시각 스타일은 필드 그대로다.
 */
@Composable
fun HomeSearchBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchHint = stringResource(R.string.home_search_hint)
    Box(modifier = modifier) {
        Box(modifier = Modifier.clearAndSetSemantics {}) {
            EumSearchField(
                value = "",
                placeholder = searchHint,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(role = Role.Button, onClickLabel = searchHint, onClick = onSearchClick)
                .semantics { contentDescription = searchHint },
        )
    }
}
