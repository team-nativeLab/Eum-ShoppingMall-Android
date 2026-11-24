package com.nativelap.eumshoppingmall.ui.auth.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 입력칸(AuthOutlinedTextField 최소 높이 48dp)과 같은 높이로 맞춰 입력칸 옆에서 짧아 보이지 않게 한다.
private val AuthFieldActionButtonHeight = 48.dp
private val AuthFieldActionButtonMinWidth = 73.dp
private val AuthFieldActionButtonPadding = PaddingValues(horizontal = 16.dp)

/** 입력칸 오른쪽 보조 버튼(중복 확인·재전송·확인)의 공통 모양. shape·여백·최소 폭·높이를 한 곳에서 정한다. */
@Composable
internal fun AuthFieldActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = AuthFieldActionButtonHeight).defaultMinSize(minWidth = AuthFieldActionButtonMinWidth),
        shape = RoundedCornerShape(8.dp),
        contentPadding = AuthFieldActionButtonPadding,
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}
